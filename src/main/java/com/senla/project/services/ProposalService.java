package com.senla.project.services;

import com.senla.project.dto.ProposalReceivedResponse;
import com.senla.project.dto.ProposalRequest;
import com.senla.project.dto.ProposalSentResponse;
import java.util.List;

public interface ProposalService {

  List<ProposalSentResponse> getAllSentProposalsByUserId(Long userId);

  List<ProposalReceivedResponse> getAllReceivedProposalsByUserId(Long userId);

  ProposalSentResponse createProposal(Long userId, ProposalRequest proposalRequest);

  boolean declineProposalById(Long proposalId);

  boolean doesProposalExist(Long proposalId);

  boolean wasProposalSentToUser(Long proposalId, Long currentUserId);
}
