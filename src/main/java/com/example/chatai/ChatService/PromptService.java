package com.example.chatai.ChatService;

import com.example.chatai.Config.ChatModelConfig;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class PromptService {
   private static Logger logger = LoggerFactory.getLogger(PromptService.class);


   //private final RestTemplate restTemplate = new RestTemplate();
   @Autowired
   private RestTemplate restTemplate;

   @Autowired
   private ChatModelConfig chatModelConfig;

   @Autowired
   private CamelContext camelContext;

   @Autowired
   private ProducerTemplate producerTemplate;

    public String ModelCall(String message) throws InterruptedException {
        logger.info("Sending message to RAG LLM model,with message {} ",message);

        String api= chatModelConfig.getApi();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        //headers.setContentType(MediaType.APPLICATION_JSON);

//        Map<String, String> body = new LinkedHashMap<>();
//        body.put("question", message);

        String payload = "{\"question\": \"" + message + "\"}";

        HttpEntity<String> request = new HttpEntity<>(payload, headers);

        //HttpEntity<String> request= new HttpEntity<>(message,headers);

        logger.info("the request sent {}",request);
        logger.info("api call used {}",api);
        logger.info("the model use {}", chatModelConfig.getModelName());
        logger.info("the request used {}",request);

        ResponseEntity<Map> response = restTemplate.exchange(api, HttpMethod.POST,request,Map.class);
        //Thread.sleep(Long.parseLong("50000"));

         logger.info("response from an api,{}",response.getBody());

        Map<String,Object> responsBody = response.getBody();

        Object answer = responsBody.get("response");

        logger.info("The returned answer {}",answer);
        return answer.toString();



    }


    public String getResponse(String message){
        logger.info("initiating response genaration");


        if (message != null){
            try {
                String answer = this.ModelCall(message);
                if (answer != null && !answer.isEmpty()) {
                    logger.info("the answer is returned successful,answer {}", answer);

                    return answer;

                } else {

                    logger.info("Answer not returned for question {}",message);
                    return null;

                }
            }catch (Exception e){
                logger.error(e.getMessage());
            }

        }
        else
        {
            logger.error("Message is empty,kindly write your message");

        }

        return null;

    }






}
