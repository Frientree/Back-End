package com.d101.frientree.serviceImpl.userfruit.clova;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Log4j2
@Data
public class ClovaSpeechClient {

    // Clova Speech secret key (수동 빈 등록으로 값 가져옴)
    private String SECRET;
    // Clova Speech invoke URL (수동 빈 등록으로 값 가져옴)
    private String INVOKE_URL;

    private CloseableHttpClient httpClient = HttpClients.createDefault();
    private Gson gson = new Gson();

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Boosting {
        private String words;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Diarization {
        private Boolean enable = Boolean.FALSE;
        private Integer speakerCountMin;
        private Integer speakerCountMax;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Sed {
        private Boolean enable = Boolean.FALSE;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class NestRequestEntity {
        private String language = "ko-KR";
        //completion optional, sync/async
        private String completion = "sync";
        //optional, used to receive the analyzed results
        private String callback;
        //optional, any data
        private Map<String, Object> userdata;
        private Boolean wordAlignment = Boolean.TRUE;
        private Boolean fullText = Boolean.TRUE;
        //boosting object array
        private List<Boosting> boostings;
        //comma separated words
        private String forbiddens;
        private Diarization diarization;
        private Sed sed;
    }

    /**
     *
     * recognize media using a file
     * @param file required, the media file
     * @param nestRequestEntity optional
     * @return string
     */
    public String upload(File file, NestRequestEntity nestRequestEntity) {
        HttpPost httpPost = new HttpPost(INVOKE_URL + "/recognizer/upload");

        Header[] HEADERS = new Header[] {
                new BasicHeader("Accept", "application/json"),
                new BasicHeader("X-CLOVASPEECH-API-KEY", SECRET),
        };

        httpPost.setHeaders(HEADERS);
        HttpEntity httpEntity = MultipartEntityBuilder.create()
                .addTextBody("params", gson.toJson(nestRequestEntity), ContentType.APPLICATION_JSON)
                .addBinaryBody("media", file, ContentType.MULTIPART_FORM_DATA, file.getName())
                .build();
        httpPost.setEntity(httpEntity);
        return execute(httpPost);
    }

    private String execute(HttpPost httpPost) {
        try (final CloseableHttpResponse httpResponse = httpClient.execute(httpPost)) {
            final HttpEntity entity = httpResponse.getEntity();
            return EntityUtils.toString(entity, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        final ClovaSpeechClient clovaSpeechClient = new ClovaSpeechClient();
        NestRequestEntity requestEntity = new NestRequestEntity();
        final String result =
                clovaSpeechClient.upload(new File("/data/sample.mp4"), requestEntity);
        //final String result = clovaSpeechClient.url("file URL", requestEntity);
        //final String result = clovaSpeechClient.objectStorage("Object Storage key", requestEntity);
        System.out.println(result);
    }
}