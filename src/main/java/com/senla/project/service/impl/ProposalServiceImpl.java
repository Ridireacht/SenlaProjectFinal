package com.senla.project.service.impl;

import com.senla.project.dto.request.ProposalRequest;
import com.senla.project.dto.response.ProposalReceivedResponse;
import com.senla.project.dto.response.ProposalSentResponse;
import com.senla.project.entity.Ad;
import com.senla.project.entity.Proposal;
import com.senla.project.entity.User;
import com.senla.project.mapper.ProposalMapper;
import com.senla.project.repository.AdRepository;
import com.senla.project.repository.ConversationRepository;
import com.senla.project.repository.ProposalRepository;
import com.senla.project.repository.UserRepository;
import com.senla.project.service.ProposalService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ProposalServiceImpl implements ProposalService {

  private final ProposalRepository proposalRepository;
  private final AdRepository adRepository;
  private final UserRepository userRepository;
  private final ConversationRepository conversationRepository;

  private final ProposalMapper proposalMapper;


  @Override
  public List<ProposalSentResponse> getSentProposalsOfUser(long userId) {
    List<Proposal> sentProposals = proposalRepository.findAllBySenderId(userId);
    return sentProposals.stream()
        .map(proposalMapper::mapToProposalSentResponse)
        .collect(Collectors.toList());
  }

  @Override
  public List<ProposalReceivedResponse> getReceivedProposalsOfUser(long userId) {
    List<Ad> userAds = adRepository.findAllBySellerIdAndIsClosedFalse(userId);
    List<Proposal> receivedProposals = proposalRepository.findAllByAds(userAds);

    return receivedProposals.stream()
        .map(proposalMapper::mapToProposalReceivedResponse)
        .collect(Collectors.toList());
  }

  @Transactional
  @Override
  public ProposalSentResponse createProposal(long userId, ProposalRequest proposalRequest) {
    User sender = userRepository.findById(userId).get();
    Ad ad = adRepository.findById(proposalRequest.getAdId()).get();

    Proposal proposal = new Proposal();
    proposal.setSender(sender);
    proposal.setAd(ad);
    proposal.setPrice(proposalRequest.getPrice());

    Proposal savedProposal = proposalRepository.save(proposal);
    return proposalMapper.mapToProposalSentResponse(savedProposal);
  }

  @Transactional
  @Override
  public boolean acceptProposalById(long proposalId) {
    if (proposalRepository.existsById(proposalId)) {
      Proposal proposal = proposalRepository.findById(proposalId).get();
      User buyer = userRepository.findById(proposal.getSender().getId()).get();

      Ad ad = proposal.getAd();
      ad.setClosed(true);
      ad.setPrice(proposal.getPrice());
      ad.setBuyer(buyer);
      adRepository.save(ad);

      proposalRepository.deleteAllByAd(ad);
      conversationRepository.deleteAllByAd(ad);

      return true;
    }

    return false;
  }

  @Transactional
  @Override
  public boolean declineProposalById(long proposalId) {
    if (proposalRepository.existsById(proposalId)) {
      proposalRepository.deleteById(proposalId);
      return true;
    }

    return false;
  }

  @Override
  public boolean doesProposalExist(long proposalId) {
    return proposalRepository.existsById(proposalId);
  }

  @Override
  public boolean isProposalSentToUser(long proposalId, long userId) {
    Proposal proposal = proposalRepository.findById(proposalId).get();

    return proposal.getAd().getSeller().getId() == userId;
  }

}
