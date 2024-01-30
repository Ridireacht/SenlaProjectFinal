package com.senla.project.controller;

import com.senla.project.dto.response.ProposalReceivedResponse;
import com.senla.project.dto.request.ProposalRequest;
import com.senla.project.dto.response.ProposalSentResponse;
import com.senla.project.exception.ForbiddenException;
import com.senla.project.exception.NotFoundException;
import com.senla.project.service.AdService;
import com.senla.project.service.ProposalService;
import com.senla.project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Proposal", description = "Предоставляет API для управления предложениями")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/proposals")
@AllArgsConstructor
public class ProposalController {

  private final ProposalService proposalService;
  private final UserService userService;
  private final AdService adService;


  @Operation(summary = "Получить все отправленные предложения", description = "Возвращает список всех отправленных пользователем предложений.")
  @GetMapping("/sent")
  public List<ProposalSentResponse> getAllSentProposals() {
    return proposalService.getAllSentProposalsByUserId(getCurrentUserId());
  }

  @Operation(summary = "Получить все полученные предложения", description = "Возвращает список всех полученных пользователем предложений.")
  @GetMapping("/received")
  public List<ProposalReceivedResponse> getAllReceivedProposals() {
    return proposalService.getAllReceivedProposalsByUserId(getCurrentUserId());
  }

  @Operation(summary = "Отправить предложение", description = "Отправляет новое предложение по указанному объявлению. Возвращает информацию о посланном предложении.")
  @PostMapping
  public ProposalSentResponse sendProposal(@Valid @RequestBody ProposalRequest proposalRequest) {
    if (!adService.doesAdExist(proposalRequest.getAdId())) {
      throw new NotFoundException("Ad", proposalRequest.getAdId());
    }

    if(adService.doesAdBelongToUser(proposalRequest.getAdId(), getCurrentUserId())) {
      throw new NotFoundException("Ad", proposalRequest.getAdId());
    }

    if (adService.isAdClosed(proposalRequest.getAdId())) {
      throw new ForbiddenException("You can't make proposals for a closed ad.");
    }
    
    return proposalService.createProposal(getCurrentUserId(), proposalRequest);
  }

  @Operation(summary = "Принять предложение", description = "Принимает предложение по его id. Возвращает boolean-результат операции.")
  @PostMapping("/received/{id}")
  public Boolean acceptProposal(@PathVariable("{id}") Long proposalId) {
    if (!proposalService.doesProposalExist(proposalId)) {
      throw new NotFoundException("Proposal", proposalId);
    }

    if (!proposalService.wasProposalSentToUser(proposalId, getCurrentUserId())) {
      throw new ForbiddenException("You can't accept someone else's proposal.");
    }

    return proposalService.acceptProposalById(proposalId);
  }

  @Operation(summary = "Отклонить предложение", description = "Отклоняет предложение по его id. Возвращает boolean-результат операции.")
  @DeleteMapping("/received/{id}")
  public Boolean declineProposal(@PathVariable("{id}") Long proposalId) {
    if (!proposalService.doesProposalExist(proposalId)) {
      throw new NotFoundException("Proposal", proposalId);
    }

    if (!proposalService.wasProposalSentToUser(proposalId, getCurrentUserId())) {
      throw new ForbiddenException("You can't decline someone else's proposal.");
    }

    return proposalService.declineProposalById(proposalId);
  }

  private Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return userService.getUserIdByUsername(authentication.getName());
  }
}
