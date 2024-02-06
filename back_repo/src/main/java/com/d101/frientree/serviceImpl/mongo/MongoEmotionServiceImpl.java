package com.d101.frientree.serviceImpl.mongo;

import com.d101.frientree.entity.mongo.emotion.Emotion;
import com.d101.frientree.repository.mongo.MongoEmotionRepository;
import com.d101.frientree.service.mongo.MongoEmotionService;
import com.d101.frientree.serviceImpl.userfruit.objectstorage.AwsS3ObjectStorage;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MongoEmotionServiceImpl implements MongoEmotionService {
    private final MongoEmotionRepository mongoEmotionRepository;
    private final AwsS3ObjectStorage awsS3ObjectStorage;

    @Override
    public void createEmotion(String userPK, String text, String emotionResult){
        Emotion emotion = new Emotion(userPK, text, emotionResult, Instant.now());
        mongoEmotionRepository.save(emotion);
    }

    @Override
    public String makeFileCsv() throws IOException {
        List<Emotion> emotions = mongoEmotionRepository.findAll();

        // CSV 파일을 저장할 임시 파일 생성
        File csvFile = Files.createTempFile("emotions-", ".csv").toFile();

        // BufferedWriter와 CSVPrinter를 사용하여 CSV 파일 작성 (UTF-8 인코딩 및 BOM 추가)
        try (
                OutputStreamWriter writer = new OutputStreamWriter(
                        Files.newOutputStream(csvFile.toPath(), StandardOpenOption.WRITE),
                        StandardCharsets.UTF_8.newEncoder()
                );
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader("text", "result"))
        ) {
            // UTF-8 BOM 추가
            writer.write('\ufeff');

            for (Emotion item : emotions) {
                // 각 데이터 항목을 CSV 레코드로 변환하여 파일에 쓰기
                csvPrinter.printRecord(item.getText(), item.getEmotionResult());
            }
            csvPrinter.flush(); // 변경사항을 파일에 반영

            // 파일 업로드를 위한 키 이름 생성 (예: "emotions-" + System.currentTimeMillis() + ".csv")
            String keyName = "emotions-" + System.currentTimeMillis() + ".csv";

            // AWS S3에 파일 업로드하고, 업로드된 파일의 URL 반환
            return awsS3ObjectStorage.uploadCsvFile(csvFile, keyName);
        } catch (IOException e) {
            throw new RuntimeException("CSV 파일 생성 중 오류 발생", e);
        } finally {
            if (!csvFile.delete()) {
                System.out.println("Error deleting " + csvFile); // 실패한 파일 삭제에 대한 로깅
            }
        }
    }

    @Override
    public void deleteEmotion() {
        mongoEmotionRepository.deleteAll();
    }
}
