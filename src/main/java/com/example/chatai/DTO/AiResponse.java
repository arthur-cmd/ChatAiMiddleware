package com.example.chatai.DTO;

import java.io.Serializable;

public class AiResponse<T> implements Serializable {

    private String code;

    private String message;

    protected T rsponse ;

    public AiResponse(){

    }

    public String getMessage(){return message;}

    public String getCode(){return code;}

    public T getRsponse(){return rsponse;}

    public void setCode(String code){this.code=code ;}

    public void setMessage(String message){this.message=message;}


}
