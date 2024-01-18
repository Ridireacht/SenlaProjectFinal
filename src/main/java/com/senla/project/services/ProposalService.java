package com.senla.project.services;

import com.senla.project.dto.ProposalReceivedResponse;
import com.senla.project.dto.ProposalRequest;
import com.senla.project.dto.ProposalSentResponse;
import java.util.List;

public interface ProposalService {

  List<ProposalSentResponse> getAllSentProposalsByUserId(Long id);

  List<ProposalReceivedResponse> getAllReceivedProposalsByUserId(Long id);

  ProposalSentResponse createProposal(ProposalRequest proposalRequest);

  boolean declineProposalById(Long id);
}
