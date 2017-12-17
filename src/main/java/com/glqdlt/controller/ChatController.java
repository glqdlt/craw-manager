package com.glqdlt.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.glqdlt.persistence.vo.ChatObj;


@Controller
public class ChatController {

	
	private static final Logger log = LoggerFactory.getLogger(ChatController.class);

	@MessageMapping("/chat")
	@SendTo("/push/chat")
	public ChatObj chatManager(ChatObj message) throws Exception {
		log.info(message.toString());
		return message;
	}

}
