package com.senla.project.controller;

import com.senla.project.dto.request.MessageRequest;
import com.senla.project.dto.response.ConversationResponse;
import com.senla.project.exception.ForbiddenException;
import com.senla.project.exception.NotFoundException;
import com.senla.project.service.ConversationService;
import com.senla.project.service.MessageService;
import com.senla.project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Message", description = "Предоставляет API для управления сообщениями")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@AllArgsConstructor
public class MessageController {

  private final MessageService messageService;
  private final UserService userService;
  private final ConversationService conversationService;


  @Operation(summary = "Отправить сообщение в переписку", description = "Отправляет новое сообщение в указанной переписке. Возвращает true, если операция удалась; false, если к моменту исполнения кода сущность перестала существовать; и 500 Internal Server Error, если возникло исключение.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Успешное выполнение операции"),
      @ApiResponse(responseCode = "400", description = "Некорректный запрос или ошибка валидации данных", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "403", description = "Доступ запрещён", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "404", description = "Сущность не найдена", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
  })
  @PostMapping("/conversations/{id}/messages")
  public ConversationResponse sendMessageToConversation(@PathVariable("id") Long conversationId, @Valid @RequestBody MessageRequest messageRequest) {
    if (!conversationService.doesConversationExist(conversationId)) {
      throw new NotFoundException("Conversation", conversationId);
    }

    if (!conversationService.doesConversationBelongToUser(conversationId, getCurrentUserId())) {
      throw new ForbiddenException("You can't push messages to conversation not available for you.");
    }

    return messageService.sendMessageToConversation(getCurrentUserId(), conversationId, messageRequest);
  }

  private Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return userService.getUserIdByUsername(authentication.getName());
  }
}
