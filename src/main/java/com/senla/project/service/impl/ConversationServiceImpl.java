package com.senla.project.service.impl;

import com.senla.project.dto.response.ConversationResponse;
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
  public List<ConversationResponse> getConversationsByUserId(Long userId) {
    List<Conversation> conversations = conversationRepository.findByBuyerIdOrSellerId(userId);

    return conversations.stream()
        .map(conversationMapper::mapToConversationResponse)
        .collect(Collectors.toList());
  }

  @Override
  public ConversationResponse getConversation(Long conversationId) {
    Conversation conversation = conversationRepository.findById(conversationId).get();
    return conversationMapper.mapToConversationResponse(conversation);
  }

  @Transactional
  @Override
  public ConversationResponse createConversationByAdId(Long userId, Long adId) {
    Ad ad = adRepository.findById(adId).get();
    User buyer = userRepository.findById(userId).get();

    Conversation conversation = new Conversation();
    conversation.setAd(ad);
    conversation.setBuyer(buyer);
    conversation.setSeller(ad.getSeller());

    Conversation savedConversation = conversationRepository.save(conversation);
    return conversationMapper.mapToConversationResponse(savedConversation);
  }

  @Transactional
  @Override
  public boolean deleteConversation(Long conversationId) {
    if (conversationRepository.existsById(conversationId)) {
      conversationRepository.deleteById(conversationId);
      return true;
    }

    return false;
  }

  @Override
  public boolean doesConversationExist(Long conversationId) {
    return conversationRepository.existsById(conversationId);
  }

  @Override
  public boolean doesConversationBelongToUser(Long conversationId, Long currentUserId) {
    Conversation conversation = conversationRepository.findById(conversationId).get();

    return conversation.getBuyer().getId().equals(currentUserId)
        || conversation.getSeller().getId().equals(currentUserId);
  }

}
