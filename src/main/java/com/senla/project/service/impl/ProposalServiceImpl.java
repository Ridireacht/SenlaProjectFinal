package com.senla.project.service.impl;

import com.senla.project.dto.response.ProposalReceivedResponse;
import com.senla.project.dto.request.ProposalRequest;
import com.senla.project.dto.response.ProposalSentResponse;
import com.senla.project.repository.ProposalRepository;
import com.senla.project.service.ProposalService;
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
