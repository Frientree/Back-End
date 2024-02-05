package com.d101.frientree.exception.awss3;

public class AwsS3InternalServerErrorException extends RuntimeException{
    public AwsS3InternalServerErrorException(String message){
        super(message);
    }
}
