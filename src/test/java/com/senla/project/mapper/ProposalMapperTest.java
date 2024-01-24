package com.senla.project.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.senla.project.dto.response.ProposalReceivedResponse;
import com.senla.project.dto.request.ProposalRequest;
import com.senla.project.dto.response.ProposalSentResponse;
import com.senla.project.entity.Ad;
import com.senla.project.entity.Proposal;
import com.senla.project.entity.User;
import com.senla.project.repository.AdRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProposalMapperTest {

  @Autowired
  ProposalMapper mapper;

  @Autowired
  AdRepository adRepository;


  @Test
  public void testMapToProposalSentResponse() {
    Ad ad = new Ad();
    ad.setId(2L);

    Proposal proposal = new Proposal();
    proposal.setAd(ad);
    proposal.setPrice(100);

    ProposalSentResponse expectedResponse = new ProposalSentResponse();
    expectedResponse.setAdId(2L);
    expectedResponse.setPrice(100);

    ProposalSentResponse actualResponse = mapper.mapToProposalSentResponse(proposal);

    assertEquals(expectedResponse.getAdId(), actualResponse.getAdId());
    assertEquals(expectedResponse.getPrice(), actualResponse.getPrice());
  }

  @Test
  public void testMapToProposalReceivedResponse() {
    Ad ad = new Ad();
    ad.setId(2L);

    User sender = new User();
    sender.setId(3L);

    Proposal proposal = new Proposal();
    proposal.setAd(ad);
    proposal.setSender(sender);
    proposal.setPrice(100);

    ProposalReceivedResponse expectedResponse = new ProposalReceivedResponse();
    expectedResponse.setAdId(2L);
    expectedResponse.setSenderId(3L);
    expectedResponse.setPrice(100);

    ProposalReceivedResponse actualResponse = mapper.mapToReceivedResponse(proposal);

    assertEquals(expectedResponse.getAdId(), actualResponse.getAdId());
    assertEquals(expectedResponse.getSenderId(), actualResponse.getSenderId());
    assertEquals(expectedResponse.getPrice(), actualResponse.getPrice());
  }

  @Test
  public void testMapToProposal() {
    Ad ad = new Ad();
    ad.setContent("testContent");
    adRepository.save(ad);

    ProposalRequest proposalRequest = new ProposalRequest();
    proposalRequest.setAdId(ad.getId());
    proposalRequest.setPrice(100);

    Proposal expectedEntity = new Proposal();
    expectedEntity.setAd(ad);
    expectedEntity.setPrice(100);

    Proposal actualEntity = mapper.mapToProposal(proposalRequest);

    adRepository.deleteById(ad.getId());

    assertEquals(expectedEntity.getAd().getId(), actualEntity.getAd().getId());
    assertEquals(expectedEntity.getAd().getContent(), actualEntity.getAd().getContent());
    assertEquals(expectedEntity.getPrice(), actualEntity.getPrice());
  }
}
