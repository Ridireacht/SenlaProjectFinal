package com.senla.project.mapper;

import com.senla.project.dto.ProposalReceivedResponse;
import com.senla.project.dto.ProposalRequest;
import com.senla.project.dto.ProposalSentResponse;
import com.senla.project.entities.Ad;
import com.senla.project.entities.Proposal;
import com.senla.project.repositories.AdRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ProposalMapper {

  @Autowired
  protected AdRepository adRepository;

  @Mapping(source = "ad.id", target = "adId")
  abstract ProposalSentResponse mapToProposalSentResponse(Proposal proposal);

  @Mapping(source = "ad.id", target = "adId")
  @Mapping(source = "sender.id", target = "senderId")
  abstract ProposalReceivedResponse mapToReceivedResponse(Proposal proposal);

  @Mapping(source = "adId", target = "ad", qualifiedByName = "IdToAd")
  abstract Proposal mapToProposal(ProposalRequest proposalRequest);

  @Named("IdToAd")
  Ad idToAd(Long adId) {
    return adRepository.findById(adId).orElse(null);
  }
}
