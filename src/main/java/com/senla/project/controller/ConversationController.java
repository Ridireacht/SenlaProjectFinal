package com.senla.project.controller;

import com.senla.project.dto.response.ConversationFullResponse;
import com.senla.project.dto.response.ConversationResponse;
import com.senla.project.exception.ForbiddenException;
import com.senla.project.exception.NotFoundException;
import com.senla.project.service.AdService;
import com.senla.project.service.ConversationService;
import com.senla.project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
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
  @GetMapping("/conversations")
  public List<ConversationResponse> getAllCurrentUserConversations() {
    return conversationService.getConversationsByUserId(getCurrentUserId());
  }

  @Operation(summary = "Получить конкретную переписку", description = "Возвращает конкретную переписку.")
  @GetMapping("/conversations/{id}")
  public ConversationFullResponse getConversation(@PathVariable("id") Long conversationId) {
    if (!conversationService.doesConversationExist(conversationId)) {
      throw new NotFoundException("Conversation", conversationId);
    }

    if (!conversationService.doesConversationBelongToUser(conversationId, getCurrentUserId())) {
      throw new ForbiddenException("This conversation is not available to you.");
    }

    return conversationService.getConversation(conversationId);
  }

  @Operation(summary = "Начать переписку по объявлению", description = "Создает новую переписку по указанному объявлению. Возвращает эту переписку.")
  @PostMapping("/ads/{adId}/discuss")
  public ConversationFullResponse createConversationByAd(@PathVariable("adId") Long adId) {
    if (!adService.doesAdExist(adId)) {
      throw new NotFoundException("Ad", adId);
    }

    if (adService.doesAdBelongToUser(adId, getCurrentUserId())) {
      throw new ForbiddenException("You can't start a conversation on your own ad.");
    }

    if (adService.isAdClosed(adId)) {
      throw new ForbiddenException("You can't start a conversation on a closed ad.");
    }

    return conversationService.createConversationByAdId(getCurrentUserId(), adId);
  }

  @Operation(summary = "Удалить переписку", description = "Полностью удаляет указанную переписку. Возвращает boolean-результат операции.")
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
