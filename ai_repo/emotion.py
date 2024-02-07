# koBERT
from kobert.utils import get_tokenizer
from kobert.pytorch_kobert import get_pytorch_kobert_model

# Transformers
from transformers import AdamW
from transformers.optimization import get_cosine_schedule_with_warmup

# Setting Library
import torch
from torch import nn
import torch.nn.functional as F
import torch.optim as optim
from torch.utils.data import Dataset, DataLoader
import gluonnlp as nlp
import numpy as np
from tqdm import tqdm
import pandas as pd
from sklearn.model_selection import train_test_split

# 학습시 로컬에 있는 gpu 하나만 사용할거라서 cuda:0으로 설정
device = torch.device("cuda:0")

# cache 폴더에서 사용할 bert모델과 단어를 토큰으로 변환하는 vocab 객체를 가져온다
bertmodel, vocab = get_pytorch_kobert_model(cachedir=".cache")

# 학습시킬 csv 데이터 경로
data = pd.read_csv("./.cache/output2.csv")

# 라벨링 데이터 숫자로 변환
data.loc[(data['상황'] == "happy"), '상황'] = 0
data.loc[(data['상황'] == "exciting"), '상황'] = 1
data.loc[(data['상황'] == "soso"), '상황'] = 2
data.loc[(data['상황'] == "sad"), '상황'] = 3
data.loc[(data['상황'] == "annoying"), '상황'] = 4
data.loc[(data['상황'] == "tired"), '상황'] = 5
data.loc[(data['상황'] == "worried"), '상황'] = 6

data_list = []
# 발화문과 상황을 묶어서 리스트에 넣어줌
for ques, label in zip(data['발화문'], data['상황']):
    data = []
    data.append(ques)
    data.append(str(label))
    data_list.append(data)

# 리스트에서 랜덤으로 20% 테스트 데이터로 추출
dataset_train, dataset_test = train_test_split(data_list, test_size=0.2, shuffle=True, random_state=32)

# 토크나이저는 데이터 텍스트를 토큰으로 변환하는데 사용함. lower는 텍스트를 소문자로 변경하지않고 보존하겠다는 의미. 대부분의 데이터가 한글이라서 큰 의미는 없을 것 같다.
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

# 들어올수있는 데이터의 최대 길이가 150, batch_size는 학습에 사용되는 gpu스펙에 따라 적절하게 조절하면 된다.
# 학습 데이터셋의 크기가 8000개라면 batch_size가 8일때 로그에는 사이즈로 나뉜 1000으로 찍힘. 한번에 처리하는 데이터 양
# num_epochs는 현재 데이터셋 학습을 몇번 반복할지 정하는 파라미터. 너무 많이 반복하면 overfitting 문제가 발생할 수 있다.
# 로그 인터벌은 step 몇개 진행할때마다 로그찍을지 설정하는것
# max_grad_norm과 learning_rate는 각각 gradient clipping과 gradient descent에 사용되는 파라미터고, 튜토리얼에 들어간 기본 설정값을 그대로 사용함
max_len = 150
batch_size = 8
warmup_ratio = 0.1
num_epochs = 5
max_grad_norm = 1
log_interval = 200
learning_rate = 5e-5

data_train = BERTDataset(dataset_train, 0, 1, tok, vocab, max_len, True, False)
data_test = BERTDataset(dataset_test, 0, 1, tok, vocab, max_len, True, False)

train_dataloader = torch.utils.data.DataLoader(data_train, batch_size=batch_size, num_workers=5)
test_dataloader = torch.utils.data.DataLoader(data_test, batch_size=batch_size, num_workers=5)


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

# BERTClassifier 모델을 가져온다음 이미 학습시켜놓은 cache에 있는 모델로 교체해서 이어서 학습시킨다.
# 만약 첫번째 학습이라면 load_state_dict는 주석처리한다.
model = BERTClassifier(bertmodel, dr_rate=0.5).to(device)
model.load_state_dict(torch.load("./.cache/trained_model.pth"))

no_decay = ['bias', 'LayerNorm.weight']
optimizer_grouped_parameters = [
    {'params': [p for n, p in model.named_parameters() if not any(nd in n for nd in no_decay)], 'weight_decay': 0.01},
    {'params': [p for n, p in model.named_parameters() if any(nd in n for nd in no_decay)], 'weight_decay': 0.0}
]

optimizer = AdamW(optimizer_grouped_parameters, lr=learning_rate)
loss_fn = nn.CrossEntropyLoss()  # 다중분류를 위한 loss function

t_total = len(train_dataloader) * num_epochs
warmup_step = int(t_total * warmup_ratio)

scheduler = get_cosine_schedule_with_warmup(optimizer, num_warmup_steps=warmup_step, num_training_steps=t_total)


# calc_accuracy : 정확도 측정을 위한 함수
def calc_accuracy(X, Y):
    max_vals, max_indices = torch.max(X, 1)
    train_acc = (max_indices == Y).sum().data.cpu().numpy() / max_indices.size()[0]
    return train_acc


train_history = []
test_history = []
loss_history = []

# 실제 학습 및 학습 정확도 테스트 로직 코드
for e in range(num_epochs):
    train_acc = 0.0
    test_acc = 0.0
    model.train()
    for batch_id, (token_ids, valid_length, segment_ids, label) in enumerate(tqdm(train_dataloader)):
        optimizer.zero_grad()
        token_ids = token_ids.long().to(device)
        segment_ids = segment_ids.long().to(device)
        valid_length = valid_length
        label = label.long().to(device)
        out = model(token_ids, valid_length, segment_ids)

        loss = loss_fn(out, label)
        loss.backward()
        torch.nn.utils.clip_grad_norm_(model.parameters(), max_grad_norm)
        optimizer.step()
        scheduler.step()
        train_acc += calc_accuracy(out, label)
        if batch_id % log_interval == 0:
            print("epoch {} batch id {} loss {} train acc {}".format(e + 1, batch_id + 1, loss.data.cpu().numpy(),
                                                                     train_acc / (batch_id + 1)))
            train_history.append(train_acc / (batch_id + 1))
            loss_history.append(loss.data.cpu().numpy())
    print("epoch {} train acc {}".format(e + 1, train_acc / (batch_id + 1)))
    # train_history.append(train_acc / (batch_id+1))

    # .eval() : nn.Module에서 train time과 eval time에서 수행하는 다른 작업을 수행할 수 있도록 switching 하는 함수
    # 즉, model이 Dropout이나 BatNorm2d를 사용하는 경우, train 시에는 사용하지만 evaluation을 할 때에는 사용하지 않도록 설정해주는 함수
    model.eval()
    for batch_id, (token_ids, valid_length, segment_ids, label) in enumerate(tqdm(test_dataloader)):
        token_ids = token_ids.long().to(device)
        segment_ids = segment_ids.long().to(device)
        valid_length = valid_length
        label = label.long().to(device)
        out = model(token_ids, valid_length, segment_ids)
        test_acc += calc_accuracy(out, label)
    print("epoch {} test acc {}".format(e + 1, test_acc / (batch_id + 1)))
    test_history.append(test_acc / (batch_id + 1))

    torch.save(model.state_dict(), "./.cache/trained_model.pth")


# 테스트 메서드
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

        # happy exciting soso sad annoying tired worried
        emotions = ["happy", "exciting", "soso", "sad", "annoying", "tired", "worried"]
        top3_emotions = []
        for i in out:
            logits = i
            logits = logits.detach().cpu().numpy()

            probabilities = F.softmax(torch.from_numpy(logits), dim=0)  # 확률로 변환
            top3_idx = np.argsort(logits)[-3:]  # 상위 3개 인덱스 가져오기

            for idx in reversed(top3_idx):  # 높은 순서로 출력
                top3_emotions.append((emotions[idx], probabilities[idx].item()))  # 감정 라벨과 확률 함께 저장

        print(">> 입력하신 내용에서 다음의 감정이 느껴집니다:")
        for emotion, prob in top3_emotions:
            print(f"{emotion}: {prob * 100:.2f}%")  # 감정과 그 감정일 확률을 함께 출력

# 사용자로부터 입력을 받아 예측 수행
# end = 1
# while end == 1:
#     sentence = input("하고싶은 말을 입력해주세요 : ")
#     if sentence == "0":
#         break
#     predict(sentence)
#     print("\n")
