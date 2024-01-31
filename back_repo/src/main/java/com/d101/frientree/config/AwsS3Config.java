package com.d101.frientree.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.d101.frientree.serviceImpl.userfruit.objectstorage.AwsS3ObjectStorageUpload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsS3Config {

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Bean
    public AmazonS3 amazonS3Client() {
        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
    }

    @Bean
    public AwsS3ObjectStorageUpload awsS3ObjectStorageUpload(AmazonS3 amazonS3) {
        AwsS3ObjectStorageUpload awsS3ObjectStorageUpload = new AwsS3ObjectStorageUpload(amazonS3);
        awsS3ObjectStorageUpload.setBucket(bucket);
        return awsS3ObjectStorageUpload;
    }
}
