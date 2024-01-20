package com.senla.project.controllers;

import com.senla.project.dto.ProposalReceivedResponse;
import com.senla.project.dto.ProposalRequest;
import com.senla.project.dto.ProposalSentResponse;
import com.senla.project.services.ProposalService;
import com.senla.project.services.UserService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
  public ProposalSentResponse sendProposal(ProposalRequest proposalRequest) {
    return proposalService.createProposal(proposalRequest);
  }

  @DeleteMapping("/received/{id}")
  public boolean declineProposalById(@PathVariable("{id}") Long id) {
    return proposalService.declineProposalById(id);
  }

  private Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return userService.getUserIdByUsername(authentication.getName());
  }
}
