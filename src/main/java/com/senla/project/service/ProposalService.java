package com.senla.project.service;

import com.senla.project.dto.request.ProposalRequest;
import com.senla.project.dto.response.ProposalReceivedResponse;
import com.senla.project.dto.response.ProposalSentResponse;
import java.util.List;

public interface ProposalService {

  List<ProposalSentResponse> getSentProposalsOfUser(long userId);

  List<ProposalReceivedResponse> getReceivedProposalsOfUser(long userId);

  ProposalSentResponse createProposal(long userId, ProposalRequest proposalRequest);

  boolean acceptProposalById(long proposalId);

  boolean declineProposalById(long proposalId);

  boolean doesProposalExist(long proposalId);

  boolean isProposalSentToUser(long proposalId, long userId);
}
