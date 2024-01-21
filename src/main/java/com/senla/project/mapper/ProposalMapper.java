package com.senla.project.mapper;

import com.senla.project.dto.ProposalReceivedResponse;
import com.senla.project.dto.ProposalRequest;
import com.senla.project.dto.ProposalSentResponse;
import com.senla.project.entities.Proposal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProposalMapper {

  ProposalMapper MAPPER = Mappers.getMapper(ProposalMapper.class);


  @Mapping(source = "ad.id", target = "adId")
  ProposalSentResponse mapToProposalSentResponse(Proposal proposal);

  @Mapping(source = "ad.id", target = "adId")
  @Mapping(source = "sender.id", target = "senderId")
  ProposalReceivedResponse mapToReceivedResponse(Proposal proposal);

  //@Mapping(source = "adId", target = "ad")
  //Proposal mapToProposal(ProposalRequest proposalRequest);
}
