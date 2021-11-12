package com.yapp.project.aux.fcm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import com.google.gson.JsonParseException;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FireBaseCloudMessageService {

    @Value("${property.fcm.project.id}")
    private String PROJECT_ID;
    private final String FIREBASE_CONFIG_PATH = "firebase/firebase_service_key.json";
    private final String GOOGLE_AUTH_API = "https://www.googleapis.com/auth/cloud-platform";
    private final String CONTENT_TYPE = "application/json; UTF-8";
    private final String CONTENT_MEDIA_TYPE = "application/json; charset=utf-8";
    private final ObjectMapper objectMapper;

    public void sendMessageTo(String targetToken, String title, String body) throws IOException {
        String API_URL = "https://fcm.googleapis.com/v1/projects/" + PROJECT_ID + "/messages:send";
        String message = makeMessage(targetToken, title, body);
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message,
                MediaType.get(CONTENT_MEDIA_TYPE));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE)
                .build();
        Response response = client.newCall(request).execute();
    }

    private String makeMessage(String targetToken, String title, String body) throws JsonParseException, JsonProcessingException {
        FireBaseCloudMessage fcmMessage = FireBaseCloudMessage.builder()
                .message(FireBaseCloudMessage.Message.builder()
                        .token(targetToken)
                        .notification(FireBaseCloudMessage.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();
        return objectMapper.writeValueAsString(fcmMessage);
    }

    private String getAccessToken() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(FIREBASE_CONFIG_PATH).getInputStream())
                .createScoped(List.of(GOOGLE_AUTH_API));
        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
