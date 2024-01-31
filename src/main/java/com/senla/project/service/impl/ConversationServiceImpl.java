package com.senla.project.service.impl;

import com.senla.project.dto.response.ConversationResponse;
import com.senla.project.dto.response.ConversationInfoResponse;
import com.senla.project.entity.Ad;
import com.senla.project.entity.Conversation;
import com.senla.project.entity.User;
import com.senla.project.mapper.ConversationMapper;
import com.senla.project.repository.AdRepository;
import com.senla.project.repository.ConversationRepository;
import com.senla.project.repository.UserRepository;
import com.senla.project.service.ConversationService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ConversationServiceImpl implements ConversationService {

  private final ConversationRepository conversationRepository;
  private final AdRepository adRepository;
  private final UserRepository userRepository;

  private final ConversationMapper conversationMapper;


  @Override
  public long getAdId(long conversationId) {
    Conversation conversation = conversationRepository.findById(conversationId).get();
    return conversation.getAd().getId();
  }

  @Override
  public List<ConversationInfoResponse> getConversationsOfUser(long userId) {
    List<Conversation> conversations = conversationRepository.findByBuyerIdOrSellerId(userId);

    return conversations.stream()
        .map(conversationMapper::mapToConversationResponse)
        .collect(Collectors.toList());
  }

  @Override
  public ConversationResponse getConversation(long conversationId) {
    Conversation conversation = conversationRepository.findById(conversationId).get();
    return conversationMapper.mapToConversationFullResponse(conversation);
  }

  @Transactional
  @Override
  public ConversationResponse createConversationByAd(long userId, long adId) {
    Ad ad = adRepository.findById(adId).get();
    User buyer = userRepository.findById(userId).get();

    Conversation conversation = new Conversation();
    conversation.setAd(ad);
    conversation.setBuyer(buyer);
    conversation.setSeller(ad.getSeller());

    Conversation savedConversation = conversationRepository.save(conversation);
    return conversationMapper.mapToConversationFullResponse(savedConversation);
  }

  @Transactional
  @Override
  public boolean deleteConversation(long conversationId) {
    if (conversationRepository.existsById(conversationId)) {
      conversationRepository.deleteById(conversationId);
      return true;
    }

    return false;
  }

  @Override
  public boolean doesConversationExist(long conversationId) {
    return conversationRepository.existsById(conversationId);
  }

  @Override
  public boolean doesConversationBelongToUser(long conversationId, long currentUserId) {
    Conversation conversation = conversationRepository.findById(conversationId).get();

    return conversation.getBuyer().getId().equals(currentUserId)
        || conversation.getSeller().getId().equals(currentUserId);
  }

}
