package com.senla.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.senla.project.dto.request.ProposalRequest;
import com.senla.project.dto.response.ProposalReceivedResponse;
import com.senla.project.dto.response.ProposalSentResponse;
import com.senla.project.entity.Ad;
import com.senla.project.entity.Proposal;
import com.senla.project.entity.User;
import com.senla.project.mapper.ProposalMapper;
import com.senla.project.mapper.ProposalMapperImpl;
import com.senla.project.repository.AdRepository;
import com.senla.project.repository.ConversationRepository;
import com.senla.project.repository.ProposalRepository;
import com.senla.project.repository.UserRepository;
import com.senla.project.service.impl.ProposalServiceImpl;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = { ProposalMapperImpl.class, ProposalServiceImpl.class})
@ExtendWith(MockitoExtension.class)
public class ProposalServiceTest {

  @MockBean
  private ProposalRepository proposalRepository;

  @MockBean
  private AdRepository adRepository;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private ConversationRepository conversationRepository;

  @MockBean
  private ProposalMapper proposalMapper;

  @Autowired
  private ProposalService proposalService;


  @Test
  public void testGetSentProposalsOfUser() {
    long senderId = 1L;

    List<Proposal> sentProposals = Arrays.asList(createProposal(1L, 2, 3L, senderId, 5L), createProposal(2L, 3, 4L, senderId, 6L));

    when(proposalRepository.findAllBySenderId(senderId)).thenReturn(sentProposals);
    when(proposalMapper.mapToProposalSentResponse(any())).thenReturn(createProposalSentResponse());

    List<ProposalSentResponse> result = proposalService.getSentProposalsOfUser(senderId);

    assertEquals(sentProposals.size(), result.size());
  }

  @Test
  public void testGetReceivedProposalsOfUser() {
    long sellerId = 1L;

    List<Ad> userAds = Arrays.asList(createAd(1L), createAd(2L));
    List<Proposal> receivedProposals = Arrays.asList(createProposal(1L, 2, 1L, 2L, sellerId), createProposal(2L, 3, 1L, 3L, sellerId));

    when(adRepository.findAllBySellerIdAndIsClosedFalse(sellerId)).thenReturn(userAds);
    when(proposalRepository.findAllByAds(userAds)).thenReturn(receivedProposals);
    when(proposalMapper.mapToProposalReceivedResponse(any())).thenReturn(createProposalReceivedResponse());

    List<ProposalReceivedResponse> result = proposalService.getReceivedProposalsOfUser(sellerId);

    assertEquals(receivedProposals.size(), result.size());
  }

  @Test
  public void testCreateProposal() {
    long senderId = 1L;

    ProposalRequest proposalRequest = createProposalRequest();
    User sender = createUser(senderId);
    Proposal proposal = createProposal(1L, 2, 3L, senderId, 4L);
    ProposalSentResponse expectedResponse = createProposalSentResponse();

    when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
    when(proposalMapper.mapToProposal(proposalRequest)).thenReturn(proposal);
    when(proposalRepository.save(proposal)).thenReturn(proposal);
    when(proposalMapper.mapToProposalSentResponse(proposal)).thenReturn(expectedResponse);

    ProposalSentResponse result = proposalService.createProposal(senderId, proposalRequest);

    assertEquals(expectedResponse, result);
  }

  @Test
  public void testAcceptProposalById() {
    long proposalId = 1L;

    Proposal proposal = createProposal(proposalId, 2, 2L, 2L, 3L);

    User buyer = createUser(2L);
    proposal.setSender(buyer);

    Ad ad = createAd(2L);
    proposal.setAd(ad);

    when(proposalRepository.existsById(proposalId)).thenReturn(true);
    when(proposalRepository.findById(proposalId)).thenReturn(Optional.of(proposal));
    when(adRepository.save(ad)).thenReturn(ad);

    boolean result = proposalService.acceptProposalById(proposalId);

    assertTrue(result);
  }

  @Test
  public void testDeclineProposalById() {
    long proposalId = 1L;

    when(proposalRepository.existsById(proposalId)).thenReturn(true);

    boolean result = proposalService.declineProposalById(proposalId);

    assertTrue(result);
  }

  @Test
  public void testDoesProposalExist() {
    long proposalId = 1L;

    when(proposalRepository.existsById(proposalId)).thenReturn(true);

    boolean result = proposalService.doesProposalExist(proposalId);

    assertTrue(result);
  }

  @Test
  public void testIsProposalSentToUser() {
    long proposalId = 1L;
    long sellerId = 2L;

    Proposal proposal = createProposal(proposalId, 2, 3L, 4L, sellerId);

    when(proposalRepository.findById(proposalId)).thenReturn(Optional.of(proposal));

    boolean result = proposalService.isProposalSentToUser(proposalId, sellerId);

    assertTrue(result);
  }

  private Proposal createProposal(long proposalId, int price, long adId, long senderId, long sellerId) {
    Proposal proposal = new Proposal();
    proposal.setId(proposalId);
    proposal.setPrice(price);
    proposal.setAd(createAd(adId));
    proposal.setSender(createUser(senderId));

    proposal.getAd().setSeller(createUser(sellerId));

    return proposal;
  }

  private Ad createAd(long adId) {
    Ad ad = new Ad();
    ad.setId(adId);
    return ad;
  }

  private User createUser(long userId) {
    User user = new User();
    user.setId(userId);
    return user;
  }

  private ProposalRequest createProposalRequest() {
    ProposalRequest proposalRequest = new ProposalRequest();
    proposalRequest.setAdId(4L);
    proposalRequest.setPrice(150);
    return proposalRequest;
  }

  private ProposalSentResponse createProposalSentResponse() {
    ProposalSentResponse response = new ProposalSentResponse();
    response.setAdId(5L);
    response.setPrice(200);
    return response;
  }

  private ProposalReceivedResponse createProposalReceivedResponse() {
    ProposalReceivedResponse response = new ProposalReceivedResponse();
    response.setAdId(6L);
    response.setSenderId(7L);
    response.setPrice(250);
    return response;
  }
}

