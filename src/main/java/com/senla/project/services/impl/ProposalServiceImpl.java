package com.senla.project.services.impl;

import com.senla.project.dto.ProposalReceivedResponse;
import com.senla.project.dto.ProposalRequest;
import com.senla.project.dto.ProposalSentResponse;
import com.senla.project.repositories.ProposalRepository;
import com.senla.project.services.ProposalService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProposalServiceImpl implements ProposalService {

  private final ProposalRepository proposalRepository;


  @Override
  public List<ProposalSentResponse> getAllSentProposalsByUserId(Long userId) {
    return null;
  }

  @Override
  public List<ProposalReceivedResponse> getAllReceivedProposalsByUserId(Long userId) {
    return null;
  }

  @Override
  public ProposalSentResponse createProposal(Long userId, ProposalRequest proposalRequest) {
    return null;
  }

  @Override
  public boolean declineProposalById(Long proposalId) {
    return false;
  }

  @Override
  public boolean doesProposalExist(Long proposalId) {
    return false;
  }

  @Override
  public boolean wasProposalSentToUser(Long proposalId, Long currentUserId) {
    return false;
  }
}
