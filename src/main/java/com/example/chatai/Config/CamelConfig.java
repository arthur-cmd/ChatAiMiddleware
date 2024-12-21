package com.example.chatai.Config;

import jakarta.annotation.PostConstruct;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.chatai.Config.ChatModelConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static java.lang.invoke.MethodHandles.constant;

@Component
public class CamelConfig {

   private static Logger logger = LoggerFactory.getLogger(CamelConfig.class);

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    private ChatModelConfig chatModelConfig;

    @PostConstruct
    public void init() throws Exception {
        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                logger.info("Configured API URL: {}", chatModelConfig.getApiUrl());
                JacksonDataFormat jacksonDataFormat = new JacksonDataFormat();
                jacksonDataFormat.setPrettyPrint(true);

                from("direct:sendWhatsAppMessage")
                        .setHeader("Authorization", constant("Bearer " + chatModelConfig.getApiToken()))
                        .setHeader("Content-Type", constant("application/json"))
                        .marshal(jacksonDataFormat)
                        .process(exchange -> {
                            logger.info("the used api url {}",chatModelConfig.getApiUrl());
                            logger.info("Sending JSON: {}", exchange.getIn().getBody(String.class));
                        }).to(chatModelConfig.getApiUrl()).process(exchange -> {

                            logger.info("Response: {}", exchange.getIn().getBody(String.class));

                        });
            }
        });
    }




    //to send template message

    public void sendWhatsTemplateAppMessage(String toNumber, String templateName) {

        logger.info("Initiating sending WhatsApp message");

        // Create the body map
        Map<String, Object> body = new HashMap<>();
        body.put("messaging_product", "whatsapp");
        body.put("to", toNumber);
        body.put("type", "template");

        // Create the template map
        Map<String, Object> template = new HashMap<>();
        template.put("name", templateName);

        // Create the language map
        Map<String, String> language = new HashMap<>();
        language.put("code", "en_US");

        // Add the language map to the template map
        template.put("language", language);

        // Add the template map to the body map
        body.put("template", template);

        logger.info("Sending body: {}", body);

        // Send the message to the Camel route
        producerTemplate.sendBody("direct:sendWhatsAppMessage", body);
    }



    //to send non template message
    public void sendWhatsAppMessage(String toNumber, String messageBody) {
        logger.info("Initiating sending non-template WhatsApp message");

        // Prepare the JSON body
        Map<String, Object> body = new HashMap<>();
        body.put("messaging_product", "whatsapp");
        body.put("recipient_type", "individual");
        body.put("to", toNumber);
        body.put("type", "text");

        // Add text object with message body
        Map<String, Object> text = new HashMap<>();
        text.put("preview_url", false);
        text.put("body", messageBody);

        body.put("text", text);

        // Log the constructed body
        logger.info("Prepared Body: {}", body);

        // Send the body to the Camel route
        producerTemplate.sendBody("direct:sendWhatsAppMessage", body);
    }




}
