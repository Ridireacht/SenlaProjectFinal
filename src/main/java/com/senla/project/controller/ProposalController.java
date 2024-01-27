package com.senla.project.controller;

import com.senla.project.dto.response.ProposalReceivedResponse;
import com.senla.project.dto.request.ProposalRequest;
import com.senla.project.dto.response.ProposalSentResponse;
import com.senla.project.service.AdService;
import com.senla.project.service.ProposalService;
import com.senla.project.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Предложение", description = "API для управления предложениями")
@RestController
@RequestMapping("/proposals")
@AllArgsConstructor
public class ProposalController {

  private final ProposalService proposalService;
  private final UserService userService;
  private final AdService adService;


  @GetMapping("/sent")
  public List<ProposalSentResponse> getAllSentProposals() {
    return proposalService.getAllSentProposalsByUserId(getCurrentUserId());
  }

  @GetMapping("/received")
  public List<ProposalReceivedResponse> getAllReceivedProposals() {
    return proposalService.getAllReceivedProposalsByUserId(getCurrentUserId());
  }

  @PostMapping
  public ResponseEntity<ProposalSentResponse> sendProposal(@Valid @RequestBody ProposalRequest proposalRequest) {
    if(adService.doesAdBelongToUser(proposalRequest.getAdId(), getCurrentUserId())) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    
    return ResponseEntity.ok(proposalService.createProposal(getCurrentUserId(), proposalRequest));
  }

  @PostMapping("/received/{id}")
  public ResponseEntity<Boolean> acceptProposal(@PathVariable("{id}") Long proposalId) {
    if (!proposalService.doesProposalExist(proposalId)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    if (!proposalService.wasProposalSentToUser(proposalId, getCurrentUserId())) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    return ResponseEntity.ok(proposalService.acceptProposalById(proposalId));
  }

  @DeleteMapping("/received/{id}")
  public ResponseEntity<Boolean> declineProposal(@PathVariable("{id}") Long proposalId) {
    if (!proposalService.doesProposalExist(proposalId)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    if (!proposalService.wasProposalSentToUser(proposalId, getCurrentUserId())) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    return ResponseEntity.ok(proposalService.declineProposalById(proposalId));
  }

  private Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return userService.getUserIdByUsername(authentication.getName());
  }
}
