package com.yapp.project.aux.request;

import com.yapp.project.account.domain.oauth.apple.AppleKeyStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Service
public class ApiService<T> {

    private final RestTemplate restTemplate;

    @Autowired
    public ApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<AppleKeyStorage> getAppleKeys(String url, HttpEntity<MultiValueMap<String,String>> httpHeaders, Class<AppleKeyStorage> clazz){
        return restTemplate.exchange(url,HttpMethod.GET,httpHeaders,clazz);
    }

    public ResponseEntity<T> HttpEntityPost(String url, HttpEntity<MultiValueMap<String,String>> httpHeaders, Class<T> clazz) {
        return restTemplate.exchange(url, HttpMethod.POST, httpHeaders, clazz);
    }

}
