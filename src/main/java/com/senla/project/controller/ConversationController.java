package com.senla.project.controller;

import com.senla.project.dto.response.ConversationInfoResponse;
import com.senla.project.dto.response.ConversationResponse;
import com.senla.project.exception.ForbiddenException;
import com.senla.project.exception.NotFoundException;
import com.senla.project.service.AdService;
import com.senla.project.service.ConversationService;
import com.senla.project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Conversation", description = "Предоставляет API для управления переписками")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@AllArgsConstructor
public class ConversationController {

  private final ConversationService conversationService;
  private final UserService userService;
  private final AdService adService;


  @Operation(summary = "Получить все переписки пользователя", description = "Возвращает список всех переписок пользователя.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Успешное выполнение операции"),
      @ApiResponse(responseCode = "400", description = "Некорректный запрос или ошибка валидации данных", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
  })
  @GetMapping("/conversations")
  public List<ConversationInfoResponse> getCurrentUserConversations() {
    return conversationService.getConversationsOfUser(getCurrentUserId());
  }

  @Operation(summary = "Получить конкретную переписку", description = "Возвращает конкретную переписку.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Успешное выполнение операции"),
      @ApiResponse(responseCode = "400", description = "Некорректный запрос или ошибка валидации данных", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "403", description = "Доступ запрещён", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "404", description = "Сущность не найдена", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
  })
  @GetMapping("/conversations/{id}")
  public ConversationResponse getConversation(@PathVariable("id") Long conversationId) {
    if (!conversationService.doesConversationExist(conversationId)) {
      throw new NotFoundException("Conversation", conversationId);
    }

    if (!conversationService.doesConversationBelongToUser(conversationId, getCurrentUserId())) {
      throw new ForbiddenException("This conversation is not available to you.");
    }

    return conversationService.getConversation(conversationId);
  }

  @Operation(summary = "Начать переписку по объявлению", description = "Создает новую переписку по указанному объявлению. Возвращает эту переписку.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Успешное выполнение операции"),
      @ApiResponse(responseCode = "400", description = "Некорректный запрос или ошибка валидации данных", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "403", description = "Доступ запрещён", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "404", description = "Сущность не найдена", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
  })
  @PostMapping("/ads/{adId}/discuss")
  public ConversationResponse createConversationByAd(@PathVariable("adId") Long adId) {
    if (!adService.doesAdExist(adId)) {
      throw new NotFoundException("Ad", adId);
    }

    if (adService.doesAdBelongToUser(adId, getCurrentUserId())) {
      throw new ForbiddenException("You can't start a conversation on your own ad.");
    }

    if (adService.isAdClosed(adId)) {
      throw new ForbiddenException("You can't start a conversation on a closed ad.");
    }

    if (adService.doesUserHaveConversationAlready(adId, getCurrentUserId())) {
      throw new ForbiddenException("You can't start a new conversation as you have an active one for this ad.");
    }

    return conversationService.createConversationByAd(getCurrentUserId(), adId);
  }

  @Operation(summary = "Удалить переписку", description = "Полностью удаляет указанную переписку. Возвращает true, если операция удалась; false, если к моменту исполнения кода сущность перестала существовать; и 500 Internal Server Error, если возникло исключение.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Успешное выполнение операции"),
      @ApiResponse(responseCode = "400", description = "Некорректный запрос или ошибка валидации данных", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "403", description = "Доступ запрещён", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "404", description = "Сущность не найдена", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
  })
  @DeleteMapping("/conversations/{id}")
  public Boolean deleteConversation(@PathVariable("id") Long conversationId) {
    if (!conversationService.doesConversationExist(conversationId)) {
      throw new NotFoundException("Conversation", conversationId);
    }

    if (!conversationService.doesConversationBelongToUser(conversationId, getCurrentUserId())) {
      throw new ForbiddenException("You can't delete someone else's conversation.");
    }

    return conversationService.deleteConversation(conversationId);
  }

  private Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return userService.getUserIdByUsername(authentication.getName());
  }

}
