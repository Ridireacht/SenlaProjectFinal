package com.senla.project.service.impl;

import com.senla.project.dto.response.ProposalReceivedResponse;
import com.senla.project.dto.request.ProposalRequest;
import com.senla.project.dto.response.ProposalSentResponse;
import com.senla.project.entity.Ad;
import com.senla.project.entity.Proposal;
import com.senla.project.entity.User;
import com.senla.project.mapper.ProposalMapper;
import com.senla.project.repository.AdRepository;
import com.senla.project.repository.ProposalRepository;
import com.senla.project.repository.UserRepository;
import com.senla.project.service.ProposalService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProposalServiceImpl implements ProposalService {

  private final ProposalRepository proposalRepository;
  private final AdRepository adRepository;
  private final UserRepository userRepository;

  private final ProposalMapper proposalMapper;


  @Override
  public List<ProposalSentResponse> getAllSentProposalsByUserId(Long userId) {
    List<Proposal> sentProposals = proposalRepository.findAllBySenderId(userId);

    return sentProposals.stream()
        .map(proposalMapper::mapToProposalSentResponse)
        .collect(Collectors.toList());
  }

  @Override
  public List<ProposalReceivedResponse> getAllReceivedProposalsByUserId(Long userId) {
    List<Ad> userAds = adRepository.findAllByUserId(userId);
    List<Proposal> receivedProposals = proposalRepository.findAllByAdIn(userAds);

    return receivedProposals.stream()
        .map(proposalMapper::mapToProposalReceivedResponse)
        .collect(Collectors.toList());
  }

  @Override
  public ProposalSentResponse createProposal(Long userId, ProposalRequest proposalRequest) {
    User sender = userRepository.findById(userId).get();
    Ad ad = adRepository.findById(proposalRequest.getAdId()).get();

    Proposal proposal = new Proposal();
    proposal.setSender(sender);
    proposal.setAd(ad);
    proposal.setPrice(proposalRequest.getPrice());

    Proposal savedProposal = proposalRepository.save(proposal);
    return proposalMapper.mapToProposalSentResponse(savedProposal);
  }

  @Override
  public boolean acceptProposalById(Long proposalId) {
    Proposal proposal = proposalRepository.findById(proposalId).get();

    Ad ad = proposal.getAd();
    ad.setClosed(true);
    adRepository.save(ad);

    proposalRepository.deleteAllByAd(ad);
    return true;
  }

  @Override
  public boolean declineProposalById(Long proposalId) {
    proposalRepository.deleteById(proposalId);
    return true;
  }

  @Override
  public boolean doesProposalExist(Long proposalId) {
    return proposalRepository.existsById(proposalId);
  }

  @Override
  public boolean wasProposalSentToUser(Long proposalId, Long currentUserId) {
    Proposal proposal = proposalRepository.findById(proposalId).get();

    return proposal.getAd().getSeller().getId().equals(currentUserId);
  }
}
