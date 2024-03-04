package com.d101.frientree.serviceImpl.userfruit.objectstorage;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.d101.frientree.exception.awss3.AwsS3FileNotFoundException;
import com.d101.frientree.exception.awss3.AwsS3InternalServerErrorException;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

@Log4j2
@Data
public class AwsS3ObjectStorage {
    private AmazonS3 amazonS3; //AmazonS3 config 미리 빈 주입

    private String bucket; //빈 주입 시 setter

    private String aiS3Url; //빈 주입 시 setter

    public AwsS3ObjectStorage(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public String uploadFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, originalFilename, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, originalFilename).toString();
    }
    public int deleteFile(String fileUrl)  {
        try {
            // URL에서 객체 키 추출
            URL url = new URL(fileUrl);
            // URL의 첫 번째 '/'를 제거하여 객체 키 얻기
            String key = url.getPath().substring(1);

            // 파일 존재 여부 확인
            if (amazonS3.doesObjectExist(bucket, key)) {
                // S3에서 파일 삭제
                amazonS3.deleteObject(bucket, key);
                log.info("File deleted successfully: {}", key);
                return 1;
            } else { // file not found
                log.warn("File not found: {}", key);
                throw new AwsS3FileNotFoundException("m4a Not Found");
            }
        } catch (Exception e) { //error
            log.error("Failed to delete file: {}", fileUrl, e);
            throw new AwsS3InternalServerErrorException("Aws Server Error");
        }
    }

    // File 객체를 받아서 S3에 업로드하는 메소드 추가
    public String uploadCsvFile(File file, String keyName) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.length());
        metadata.setContentType("text/csv"); // MIME 타입 설정

        // S3에 파일 업로드
        amazonS3.putObject(new PutObjectRequest(bucket, keyName, fileInputStream, metadata));

        // 업로드된 파일의 URL 반환
        URL url = amazonS3.getUrl(bucket, keyName);
        return url.toString();
    }
}
