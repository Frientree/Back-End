package com.d101.frientree.serviceImpl.userfruit.clova;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
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
import org.apache.http.entity.StringEntity;
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
     * recognize media using URL
     * @param url required, the media URL
     * @param nestRequestEntity optional
     * @return string
     */
    public String url(String url, NestRequestEntity nestRequestEntity) {
        HttpPost httpPost = new HttpPost(INVOKE_URL + "/recognizer/url");
                Header[] HEADERS = new Header[] {
                new BasicHeader("Accept", "application/json"),
                new BasicHeader("X-CLOVASPEECH-API-KEY", SECRET),
        };
        httpPost.setHeaders(HEADERS);
        Map<String, Object> body = new HashMap<>();
        body.put("url", url);
        body.put("language", nestRequestEntity.getLanguage());
        body.put("completion", nestRequestEntity.getCompletion());
        body.put("callback", nestRequestEntity.getCallback());
        body.put("userdata", nestRequestEntity.getCallback());
        body.put("wordAlignment", nestRequestEntity.getWordAlignment());
        body.put("fullText", nestRequestEntity.getFullText());
        body.put("forbiddens", nestRequestEntity.getForbiddens());
        body.put("boostings", nestRequestEntity.getBoostings());
        body.put("diarization", nestRequestEntity.getDiarization());
        body.put("sed", nestRequestEntity.getSed());
        HttpEntity httpEntity = new StringEntity(gson.toJson(body), ContentType.APPLICATION_JSON);
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
}