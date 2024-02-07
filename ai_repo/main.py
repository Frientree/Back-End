import uvicorn
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel

# koBERT
from kobert.utils import get_tokenizer
from kobert.pytorch_kobert import get_pytorch_kobert_model

# Transformers
from transformers import AdamW

# Setting Library
import torch
from torch import nn
from torch.utils.data import Dataset
import gluonnlp as nlp
import numpy as np



## CPU
device = torch.device("cpu")

## GPU
# device = torch.device("cuda:0")

bertmodel, vocab = get_pytorch_kobert_model(cachedir=".cache")
tokenizer = get_tokenizer()
tok = nlp.data.BERTSPTokenizer(tokenizer, vocab, lower=False)


# BERTDataset : 각 데이터가 BERT 모델의 입력으로 들어갈 수 있도록 tokenization, int encoding, padding하는 함수

class BERTDataset(Dataset):
    def __init__(self, dataset, sent_idx, label_idx, bert_tokenizer, vocab, max_len, pad, pair):
        transform = nlp.data.BERTSentenceTransform(
            bert_tokenizer, max_seq_length=max_len, vocab=vocab, pad=pad, pair=pair)

        self.sentences = [transform([i[sent_idx]]) for i in dataset]
        self.labels = [np.int32(i[label_idx]) for i in dataset]

    def __getitem__(self, i):
        return (self.sentences[i] + (self.labels[i],))

    def __len__(self):
        return (len(self.labels))


max_len = 150
batch_size = 16
warmup_ratio = 0.1
num_epochs = 10
max_grad_norm = 1
log_interval = 200
learning_rate = 5e-5

class BERTClassifier(nn.Module):
    def __init__(self,
                 bert,
                 hidden_size=768,
                 num_classes=7,  # 감정 클래스 수로 조정
                 dr_rate=None,
                 params=None):
        super(BERTClassifier, self).__init__()
        self.bert = bert
        self.dr_rate = dr_rate

        self.classifier = nn.Linear(hidden_size, num_classes)
        if dr_rate:
            self.dropout = nn.Dropout(p=dr_rate)

    def gen_attention_mask(self, token_ids, valid_length):
        attention_mask = torch.zeros_like(token_ids)
        for i, v in enumerate(valid_length):
            attention_mask[i][:v] = 1
        return attention_mask.float()

    def forward(self, token_ids, valid_length, segment_ids):
        attention_mask = self.gen_attention_mask(token_ids, valid_length)

        _, pooler = self.bert(input_ids=token_ids, token_type_ids=segment_ids.long(),
                              attention_mask=attention_mask.float().to(token_ids.device), return_dict=False)
        if self.dr_rate:
            out = self.dropout(pooler)
        return self.classifier(out)


model = BERTClassifier(bertmodel, dr_rate=0.5).to(device)
model.load_state_dict(torch.load("./.cache/trained_model.pth", map_location=torch.device('cpu')))

no_decay = ['bias', 'LayerNorm.weight']
optimizer_grouped_parameters = [
    {'params': [p for n, p in model.named_parameters() if not any(nd in n for nd in no_decay)], 'weight_decay': 0.01},
    {'params': [p for n, p in model.named_parameters() if any(nd in n for nd in no_decay)], 'weight_decay': 0.0}
]

optimizer = AdamW(optimizer_grouped_parameters, lr=learning_rate)
loss_fn = nn.CrossEntropyLoss()  # 다중분류를 위한 loss function


# calc_accuracy : 정확도 측정을 위한 함수
def calc_accuracy(X, Y):
    max_vals, max_indices = torch.max(X, 1)
    train_acc = (max_indices == Y).sum().data.cpu().numpy() / max_indices.size()[0]
    return train_acc


def predict(predict_sentence):
    # input 데이터 변환
    data = [predict_sentence, '0']
    dataset_another = [data]

    another_test = BERTDataset(dataset_another, 0, 1, tok, vocab, max_len, True, False)
    test_dataloader = torch.utils.data.DataLoader(another_test, batch_size=batch_size, num_workers=5)

    model.eval()

    for batch_id, (token_ids, valid_length, segment_ids, label) in enumerate(test_dataloader):
        token_ids = token_ids.long().to(device)
        segment_ids = segment_ids.long().to(device)

        valid_length = valid_length

        out = model(token_ids, valid_length, segment_ids)

        emotions = ["happy", "exciting", "soso", "sad", "annoying", "tired", "worried"]
        top3_emotions = []
        for i in out:
            logits = i
            logits = logits.detach().cpu().numpy()

            top3_idx = np.argsort(logits)[-3:]  # 상위 3개 인덱스 가져오기

            for idx in reversed(top3_idx):  # 높은 순서로 출력
                top3_emotions.append(emotions[idx])
        return top3_emotions  # 결과 반환


app = FastAPI()


class Item(BaseModel):
    sentence: str

import requests
import os

class DownloadRequest(BaseModel):
    s3Url: str

class BaseRequest(BaseModel):
    s3Url: str



@app.post("/download_csv")
async def download_csv(request: BaseRequest):
    try:
        response = requests.get(request.s3Url)
        response.raise_for_status()
    except requests.RequestException as e:
        raise HTTPException(status_code=500, detail=f"Error during file download request: {str(e)}")

    try:
        current_dir = os.path.dirname(os.path.realpath(__file__))
        directory = os.path.join(current_dir, "csvDir")
        os.makedirs(directory, exist_ok=True)
        filename = os.path.basename(request.s3Url)
        filepath = os.path.join(directory, filename)
        with open(filepath, 'wb') as f:
            f.write(response.content)
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error during file saving: {str(e)}")

    try:
        spring_boot_url = "http://http://3.37.240.106/s3"
        data = {"s3Url": request.s3Url}
        spring_boot_response = requests.post(spring_boot_url, json=data)
        spring_boot_response.raise_for_status()
    except requests.RequestException as e:
        raise HTTPException(status_code=500, detail=f"Error during HTTP post request to Spring Boot: {str(e)}")

    return {"status": 200, "message": "File download, deletion and post request completed successfully."}


@app.post("/predict")
async def predict_emotion(item: Item):
    sentence = item.sentence
    result = predict(sentence)
    return {"result": result}


if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8004)
