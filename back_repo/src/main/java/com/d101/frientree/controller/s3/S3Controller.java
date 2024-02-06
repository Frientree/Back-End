package com.d101.frientree.controller.s3;

import com.d101.frientree.dto.s3.request.CsvFileDeleteRequest;
import com.d101.frientree.serviceImpl.userfruit.objectstorage.AwsS3ObjectStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("s3")
@CrossOrigin("*")
@RequiredArgsConstructor
public class S3Controller {
    private final AwsS3ObjectStorage awsS3ObjectStorage;

    @PostMapping
    public ResponseEntity<?> csvFileDelete(@RequestBody CsvFileDeleteRequest request){
        if(awsS3ObjectStorage.deleteFile(request.getS3Url()) > 0){
            return ResponseEntity.ok("ok");
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error");
        }
    }
}
