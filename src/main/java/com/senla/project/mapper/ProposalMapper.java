package com.senla.project.mapper;

import com.senla.project.dto.response.ProposalReceivedResponse;
import com.senla.project.dto.request.ProposalRequest;
import com.senla.project.dto.response.ProposalSentResponse;
import com.senla.project.entity.Proposal;
import com.senla.project.repository.AdRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ProposalMapper {

  @Autowired
  protected AdRepository adRepository;


  @Mapping(source = "ad.id", target = "adId")
  public abstract ProposalSentResponse mapToProposalSentResponse(Proposal proposal);

  @Mapping(source = "ad.id", target = "adId")
  @Mapping(source = "sender.id", target = "senderId")
  public abstract ProposalReceivedResponse mapToProposalReceivedResponse(Proposal proposal);

  @Mapping(target = "ad", expression = "java(adRepository.findById(proposalRequest.getAdId()).get())")
  public abstract Proposal mapToProposal(ProposalRequest proposalRequest);
}
