package com.senla.project.controllers;

import com.senla.project.dto.ProposalReceivedResponse;
import com.senla.project.dto.ProposalRequest;
import com.senla.project.dto.ProposalSentResponse;
import com.senla.project.services.ProposalService;
import com.senla.project.services.UserService;
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

@RestController
@RequestMapping("/proposals")
@AllArgsConstructor
public class ProposalController {

  private final ProposalService proposalService;
  private final UserService userService;


  @GetMapping("/sent")
  public List<ProposalSentResponse> getAllSentProposals() {
    return proposalService.getAllSentProposalsByUserId(getCurrentUserId());
  }

  @GetMapping("/received")
  public List<ProposalReceivedResponse> getAllReceivedProposals() {
    return proposalService.getAllReceivedProposalsByUserId(getCurrentUserId());
  }

  @PostMapping
  public ProposalSentResponse sendProposal(@Valid @RequestBody ProposalRequest proposalRequest) {
    return proposalService.createProposal(getCurrentUserId(), proposalRequest);
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
