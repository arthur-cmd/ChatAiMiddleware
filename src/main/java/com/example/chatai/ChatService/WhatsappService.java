package com.example.chatai.ChatService;

import com.example.chatai.Config.CamelConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class WhatsappService {

    private static Logger logger = LoggerFactory.getLogger(WhatsappService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    PromptService promptService;

    @Autowired
    CamelConfig camelConfig;


    public  void processIncomingMessage(String payload) throws JsonProcessingException {
        logger.info("the passed payload {}",payload);
        try {

            JsonNode jsonNode = objectMapper.readTree(payload);
            JsonNode messages = jsonNode.at("/entry/0/changes/0/value/messages");
            if (messages.isArray() && messages.size() > 0) {
                String receivedText = messages.get(0).at("/text/body").asText();
                String fromNumber = messages.get(0).at("/from").asText();
                logger.info("entered the whatsapp service");
                logger.info(fromNumber + " sent the message: " + receivedText);
                camelConfig.sendWhatsAppMessage(fromNumber, promptService.getResponse(receivedText));
            }

        }
        catch (Exception e) {
            logger.error("Error processing incoming payload: {} ", payload, e);
        }


    }
}
