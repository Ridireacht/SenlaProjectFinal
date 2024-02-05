package com.senla.project.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.senla.project.dto.request.ProposalRequest;
import com.senla.project.dto.response.ProposalReceivedResponse;
import com.senla.project.dto.response.ProposalSentResponse;
import com.senla.project.entity.Ad;
import com.senla.project.entity.Proposal;
import com.senla.project.entity.User;
import com.senla.project.repository.AdRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = { ProposalMapperImpl.class, AdRepository.class })
public class ProposalMapperTest {

  @MockBean
  private AdRepository adRepository;

  @Autowired
  private ProposalMapperImpl proposalMapper;


  @Test
  public void testMapToProposalSentResponse() {
    Proposal proposal = createProposal();
    ProposalSentResponse response = proposalMapper.mapToProposalSentResponse(proposal);

    assertEquals(proposal.getAd().getId(), response.getAdId());
    assertEquals(proposal.getPrice(), response.getPrice());
  }

  @Test
  public void testMapToProposalReceivedResponse() {
    Proposal proposal = createProposal();
    ProposalReceivedResponse response = proposalMapper.mapToProposalReceivedResponse(proposal);

    assertEquals(proposal.getAd().getId(), response.getAdId());
    assertEquals(proposal.getSender().getId(), response.getSenderId());
    assertEquals(proposal.getPrice(), response.getPrice());
  }

  @Test
  public void testMapToProposal() {
    ProposalRequest proposalRequest = createProposalRequest();

    Ad mockAd = createAd();
    when(adRepository.findById(proposalRequest.getAdId())).thenReturn(Optional.of(mockAd));

    Proposal proposal = proposalMapper.mapToProposal(proposalRequest);

    assertEquals(proposalRequest.getAdId(), proposal.getAd().getId());
    assertEquals(proposalRequest.getPrice(), proposal.getPrice());
  }

  private Proposal createProposal() {
    Proposal proposal = new Proposal();
    proposal.setId(1L);
    proposal.setPrice(100);

    Ad ad = createAd();
    proposal.setAd(ad);

    User sender = createUser();
    proposal.setSender(sender);

    return proposal;
  }

  private ProposalRequest createProposalRequest() {
    ProposalRequest proposalRequest = new ProposalRequest();
    proposalRequest.setAdId(1L);
    proposalRequest.setPrice(100);
    return proposalRequest;
  }

  private Ad createAd() {
    Ad ad = new Ad();
    ad.setId(1L);
    ad.setTitle("Test Ad");
    ad.setContent("Test Content");
    ad.setPrice(100);
    return ad;
  }

  private User createUser() {
    User user = new User();
    user.setId(1L);
    user.setUsername("TestUser");
    return user;
  }
}