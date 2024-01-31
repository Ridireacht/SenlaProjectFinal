package com.senla.project.service;

import com.senla.project.dto.request.ProposalRequest;
import com.senla.project.dto.response.ProposalReceivedResponse;
import com.senla.project.dto.response.ProposalSentResponse;
import java.util.List;

public interface ProposalService {

  List<ProposalSentResponse> getSentProposalsOfUser(Long userId);

  List<ProposalReceivedResponse> getReceivedProposalsOfUser(Long userId);

  ProposalSentResponse createProposal(Long userId, ProposalRequest proposalRequest);

  boolean acceptProposalById(Long proposalId);

  boolean declineProposalById(Long proposalId);

  boolean doesProposalExist(Long proposalId);

  boolean isProposalSentToUser(Long proposalId, Long currentUserId);
}
