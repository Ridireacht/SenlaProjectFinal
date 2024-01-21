package com.senla.project.mapper;

import com.senla.project.dto.ProposalReceivedResponse;
import com.senla.project.dto.ProposalRequest;
import com.senla.project.dto.ProposalSentResponse;
import com.senla.project.entities.Ad;
import com.senla.project.entities.Proposal;
import com.senla.project.services.AdService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public interface ProposalMapper {

  @Autowired
  AdService adService = null;

  ProposalMapper MAPPER = Mappers.getMapper(ProposalMapper.class);


  @Mapping(source = "ad.id", target = "adId")
  ProposalSentResponse mapToProposalSentResponse(Proposal proposal);

  @Mapping(source = "ad.id", target = "adId")
  @Mapping(source = "sender.id", target = "senderId")
  ProposalReceivedResponse mapToReceivedResponse(Proposal proposal);

  @Mapping(source = "adId", target = "ad", qualifiedByName = "adIdToAd")
  Proposal mapToProposal(ProposalRequest proposalRequest);

  @Named("adIdToAd")
  static Ad adIdToAd(Long adId) {
    return adService.getAdEntityById(adId);
  }
}
