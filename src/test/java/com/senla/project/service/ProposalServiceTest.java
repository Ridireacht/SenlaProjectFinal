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

@SpringBootTest(classes = { ProposalRepository.class, AdRepository.class, UserRepository.class, ConversationRepository.class, ProposalMapperImpl.class, ProposalServiceImpl.class})
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
    long userId = 1L;
    List<Proposal> sentProposals = Arrays.asList(createProposal(), createProposal());
    when(proposalRepository.findAllBySenderId(userId)).thenReturn(sentProposals);
    when(proposalMapper.mapToProposalSentResponse(any())).thenReturn(createProposalSentResponse());

    List<ProposalSentResponse> result = proposalService.getSentProposalsOfUser(userId);

    assertEquals(sentProposals.size(), result.size());
  }

  @Test
  public void testGetReceivedProposalsOfUser() {
    long userId = 1L;
    List<Ad> userAds = Arrays.asList(createAd(), createAd());
    List<Proposal> receivedProposals = Arrays.asList(createProposal(), createProposal());
    when(adRepository.findAllBySellerIdAndIsClosedFalse(userId)).thenReturn(userAds);
    when(proposalRepository.findAllByAds(userAds)).thenReturn(receivedProposals);
    when(proposalMapper.mapToProposalReceivedResponse(any())).thenReturn(createProposalReceivedResponse());

    List<ProposalReceivedResponse> result = proposalService.getReceivedProposalsOfUser(userId);

    assertEquals(receivedProposals.size(), result.size());
  }

  @Test
  public void testCreateProposal() {
    long userId = 1L;
    ProposalRequest proposalRequest = createProposalRequest();
    User sender = createUser();
    Proposal proposal = createProposal();
    ProposalSentResponse expectedResponse = createProposalSentResponse();

    when(userRepository.findById(userId)).thenReturn(Optional.of(sender));
    when(proposalMapper.mapToProposal(proposalRequest)).thenReturn(proposal);
    when(proposalRepository.save(proposal)).thenReturn(proposal);
    when(proposalMapper.mapToProposalSentResponse(proposal)).thenReturn(expectedResponse);

    ProposalSentResponse result = proposalService.createProposal(userId, proposalRequest);

    assertEquals(expectedResponse, result);
  }

  @Test
  public void testAcceptProposalById() {
    long proposalId = 1L;
    Proposal proposal = createProposal();
    User buyer = createUser();
    proposal.setSender(buyer);
    Ad ad = createAd();
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
    long userId = 2L;
    Proposal proposal = createProposal();
    Ad ad = createAd();
    User seller = createUser();
    seller.setId(userId);
    ad.setSeller(seller);
    proposal.setAd(ad);

    when(proposalRepository.findById(proposalId)).thenReturn(Optional.of(proposal));

    boolean result = proposalService.isProposalSentToUser(proposalId, userId);

    assertTrue(result);
  }

  private Proposal createProposal() {
    Proposal proposal = new Proposal();
    proposal.setId(1L);
    proposal.setPrice(100);
    proposal.setAd(createAd());
    proposal.setSender(createUser());
    return proposal;
  }

  private Ad createAd() {
    Ad ad = new Ad();
    ad.setId(2L);
    ad.setSeller(createUser());
    return ad;
  }

  private User createUser() {
    User user = new User();
    user.setId(3L);
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

