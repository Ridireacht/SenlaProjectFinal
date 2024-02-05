package com.senla.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.senla.project.dto.response.ConversationInfoResponse;
import com.senla.project.dto.response.ConversationResponse;
import com.senla.project.entity.Ad;
import com.senla.project.entity.Conversation;
import com.senla.project.entity.User;
import com.senla.project.mapper.ConversationMapperImpl;
import com.senla.project.mapper.MessageMapperImpl;
import com.senla.project.repository.AdRepository;
import com.senla.project.repository.ConversationRepository;
import com.senla.project.repository.UserRepository;
import com.senla.project.service.impl.ConversationServiceImpl;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = { ConversationRepository.class, AdRepository.class, UserRepository.class, ConversationServiceImpl.class, ConversationMapperImpl.class, MessageMapperImpl.class })
@ExtendWith(MockitoExtension.class)
public class ConversationServiceTest {

  @MockBean
  private ConversationRepository conversationRepository;

  @MockBean
  private AdRepository adRepository;

  @MockBean
  private UserRepository userRepository;

  @Autowired
  private ConversationService conversationService;


  @Test
  public void testGetAdId() {
    long conversationId = 1L;

    Conversation conversation = createConversation(conversationId);

    when(conversationRepository.findById(conversationId)).thenReturn(Optional.of(conversation));

    long result = conversationService.getAdId(conversationId);

    assertEquals(conversation.getAd().getId(), result);
  }

  @Test
  public void testGetConversationsOfUser() {
    long userId = 2L;

    List<Conversation> conversations = Arrays.asList(createConversation(1L, userId, 3L), createConversation(2L, 3L, userId));

    when(conversationRepository.findByBuyerIdOrSellerId(userId)).thenReturn(conversations);

    List<ConversationInfoResponse> result = conversationService.getConversationsOfUser(userId);

    assertEquals(conversations.size(), result.size());
  }

  @Test
  public void testGetConversation() {
    long conversationId = 3L;

    Conversation conversation = createConversation(conversationId);

    when(conversationRepository.findById(conversationId)).thenReturn(Optional.of(conversation));

    ConversationResponse result = conversationService.getConversation(conversationId);

    assertNotNull(result);
    assertEquals(conversation.getId(), result.getId());
    assertEquals(conversation.getAd().getId(), result.getAdId());
  }

  @Test
  public void testCreateConversationByAd() {
    long userId = 4L;
    long adId = 5L;

    User buyer = createUser(userId);
    Ad ad = createAd(adId);

    when(adRepository.findById(adId)).thenReturn(Optional.of(ad));
    when(userRepository.findById(userId)).thenReturn(Optional.of(buyer));
    when(conversationRepository.save(any(Conversation.class))).thenAnswer(invocation -> invocation.getArgument(0));

    ConversationResponse result = conversationService.createConversationByAd(userId, adId);

    assertNotNull(result);
    assertEquals(ad.getId(), result.getAdId());
  }

  @Test
  public void testDeleteConversation() {
    long conversationId = 6L;

    when(conversationRepository.existsById(conversationId)).thenReturn(true);

    boolean result = conversationService.deleteConversation(conversationId);

    assertTrue(result);
  }

  @Test
  public void testDoesConversationExist() {
    long conversationId = 7L;

    when(conversationRepository.existsById(conversationId)).thenReturn(true);

    boolean result = conversationService.doesConversationExist(conversationId);

    assertTrue(result);
  }

  @Test
  public void testDoesConversationBelongToUser() {
    long conversationId = 8L;
    long userId = 9L;

    Conversation conversation = createConversation(conversationId, userId, 10L);
    when(conversationRepository.findById(conversationId)).thenReturn(Optional.of(conversation));

    boolean result = conversationService.doesConversationBelongToUser(conversationId, userId);

    assertTrue(result);
  }

  private User createUser(long userId) {
    User user = new User();
    user.setId(userId);
    return user;
  }

  private Conversation createConversation(long conversationId) {
    Conversation conversation = new Conversation();
    conversation.setId(conversationId);
    conversation.setAd(createAd(15L));
    conversation.setInitiator(createUser(13L));
    conversation.setReceiver(createUser(14L));
    conversation.setUpdatedAt(LocalDateTime.now());
    return conversation;
  }

  private Conversation createConversation(long conversationId, long initiatorId, long receivedId) {
    Conversation conversation = new Conversation();
    conversation.setId(conversationId);
    conversation.setAd(createAd(15L));
    conversation.setInitiator(createUser(initiatorId));
    conversation.setReceiver(createUser(receivedId));
    conversation.setUpdatedAt(LocalDateTime.now());
    return conversation;
  }

  private Ad createAd(long adId) {
    Ad ad = new Ad();
    ad.setId(adId);
    return ad;
  }
}

