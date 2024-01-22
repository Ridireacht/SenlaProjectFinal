package com.senla.project.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.senla.project.dto.ProposalReceivedResponse;
import com.senla.project.dto.ProposalSentResponse;
import com.senla.project.entities.Ad;
import com.senla.project.entities.Proposal;
import com.senla.project.entities.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProposalMapperTest {

  private final ProposalMapper mapper = Mappers.getMapper(ProposalMapper.class);


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
}
