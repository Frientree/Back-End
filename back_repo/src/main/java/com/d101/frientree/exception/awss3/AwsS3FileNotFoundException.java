package com.d101.frientree.exception.awss3;


public class AwsS3FileNotFoundException extends RuntimeException{
    public AwsS3FileNotFoundException (String message) {
        super(message);
    }
}
