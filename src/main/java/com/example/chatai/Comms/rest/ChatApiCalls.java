package com.example.chatai.Comms.rest;

import com.example.chatai.ChatService.PromptService;
import com.example.chatai.Config.CamelConfig;
import com.example.chatai.Config.ChatModelConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import com.example.chatai.ChatService.WhatsappService;


@RestController
@RequestMapping("/api/v1/chatAi")
public class ChatApiCalls {

  private static Logger logger = LoggerFactory.getLogger(ChatModelConfig.class);


  @Autowired
  private final PromptService promptService = new PromptService();

  @Autowired
  private WhatsappService whatsappService;

  @Autowired
  private CamelConfig camelConfig;



  @Value("${whatsapp.verify_token}")
  private String verifyToken;


  @GetMapping("/getAnswer/{question}")
  public String getAnswer(@PathVariable("question") String question){
    String defaultAnswer= "Dear customer,kindly write your question again";
    String defaultErrAnswer="Dear customer,there is no question sent,can kindly resend your question";

    if (question != null){
      logger.info("received customer question,from external party");
      String answer = promptService.getResponse(question);
      if (answer!=null && !answer.isEmpty()){
          logger.info("here is the answer returned back");
          return  answer;
      }
      else {
        logger.info("failed returned answer ");
        return defaultAnswer;
      }

    }
    else{
      logger.info("the question empty");
      return defaultErrAnswer;

    }

  }


  @GetMapping("/webhook")
  public String verifyWebhook(@RequestParam("hub.mode") String mode,
                              @RequestParam("hub.verify_token") String token,
                              @RequestParam("hub.challenge") String challenge) {
    if ("subscribe".equals(mode) && verifyToken.equals(token)) {
      return challenge;
    } else {
      return "Verification failed";
    }
  }

  @PostMapping("/api/whatsapp/send")
  public String sendWhatsAppMessage(@RequestParam String to, @RequestParam String message) {
     camelConfig.sendWhatsAppMessage(to,message);
    return "Message sent";
  }

  @PostMapping("/webhook")
  public void receiveMessage(@RequestBody String payload) throws JsonProcessingException {
    whatsappService.processIncomingMessage(payload);
  }








}
