package com.senla.project.mapper;

import com.senla.project.dto.ProposalReceivedResponse;
import com.senla.project.dto.ProposalRequest;
import com.senla.project.dto.ProposalSentResponse;
import com.senla.project.entities.Proposal;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProposalMapper {

  ProposalMapper MAPPER = Mappers.getMapper(ProposalMapper.class);


  ProposalSentResponse mapToProposalSentResponse(Proposal proposal);

  ProposalReceivedResponse mapToReceivedResponse(Proposal proposal);

  Proposal mapToProposal(ProposalRequest proposalRequest);
}
