package com.senla.project.controllers;

import com.senla.project.dto.ConversationResponse;
import com.senla.project.dto.MessageRequest;
import com.senla.project.services.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class MessageController {

  private final MessageService messageService;


  @PostMapping("/conversations/{id}/messages")
  public ConversationResponse sendMessage(@PathVariable("id") Long id, MessageRequest messageRequest) {
    return messageService.sendMessageWithConversationId(id, messageRequest);
  }
}
