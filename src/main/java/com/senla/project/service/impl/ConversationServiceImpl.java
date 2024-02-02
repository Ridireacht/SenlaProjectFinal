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
import java.time.LocalDateTime;
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
        .map(conversationMapper::mapToConversationInfoResponse)
        .collect(Collectors.toList());
  }

  @Override
  public ConversationResponse getConversation(long conversationId) {
    Conversation conversation = conversationRepository.findById(conversationId).get();
    return conversationMapper.mapToConversationResponse(conversation);
  }

  @Transactional
  @Override
  public ConversationResponse createConversationByAd(long userId, long adId) {
    Ad ad = adRepository.findById(adId).get();
    User buyer = userRepository.findById(userId).get();

    Conversation conversation = new Conversation();
    conversation.setAd(ad);
    conversation.setInitiator(buyer);
    conversation.setReceiver(ad.getSeller());
    conversation.setUpdatedAt(LocalDateTime.now());

    Conversation savedConversation = conversationRepository.save(conversation);
    return conversationMapper.mapToConversationResponse(savedConversation);
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
  public boolean doesConversationBelongToUser(long conversationId, long userId) {
    Conversation conversation = conversationRepository.findById(conversationId).get();

    return conversation.getInitiator().getId() == userId
        || conversation.getReceiver().getId() == userId;
  }

}
