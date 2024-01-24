package com.senla.project.service;

import com.senla.project.dto.response.ProposalReceivedResponse;
import com.senla.project.dto.request.ProposalRequest;
import com.senla.project.dto.response.ProposalSentResponse;
import java.util.List;

public interface ProposalService {

  List<ProposalSentResponse> getAllSentProposalsByUserId(Long userId);

  List<ProposalReceivedResponse> getAllReceivedProposalsByUserId(Long userId);

  ProposalSentResponse createProposal(Long userId, ProposalRequest proposalRequest);

  boolean declineProposalById(Long proposalId);

  boolean doesProposalExist(Long proposalId);

  boolean wasProposalSentToUser(Long proposalId, Long currentUserId);
}
