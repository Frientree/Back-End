package com.d101.frientree.dto.s3.request;

import lombok.Data;

@Data
public class CsvFileDeleteRequest {
    private String s3Url;
}
