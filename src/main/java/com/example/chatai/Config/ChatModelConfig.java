package com.example.chatai.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class ChatModelConfig {
    public ChatModelConfig(){
    }

    @Value("${rag.api}")
    private  String api ;

    @Value("${rag.modelName}")
    private  String modelName ;


    @Value("${rag.timeout}")
    private int timeout ;

    @Value("${rag.max_response_length}")
    private int maxResponseLength ;

    @Value("${whatsapp.api_url}")
    private String apiUrl;

    @Value("${whatsapp.access_token}")
    private String apiToken;


    public String getApi() {
        return api;
    }

    public void setApi(String api){this.api=api ;}

    public String getModelName(){
        return modelName;
    }


    public void setModelName(String modelName){
        this.modelName=modelName;
    }

    public int getTimeout(){
        return timeout;
    }


    public void setTimeout(int timeout){
        this.timeout=timeout;
    }

    public int getMaxResponseLength() {
        return maxResponseLength;
    }

    public void setMaxResponseLength(int maxResponseLength) {
        this.maxResponseLength = maxResponseLength;
    }

    public String getApiToken() {
        return apiToken;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }
}


