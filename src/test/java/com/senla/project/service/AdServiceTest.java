package com.senla.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.senla.project.dto.request.AdRequest;
import com.senla.project.dto.response.AdCurrentResponse;
import com.senla.project.entity.Ad;
import com.senla.project.entity.Conversation;
import com.senla.project.entity.Score;
import com.senla.project.entity.User;
import com.senla.project.mapper.AdMapperImpl;
import com.senla.project.repository.AdRepository;
import com.senla.project.repository.RatingRepository;
import com.senla.project.repository.UserRepository;
import com.senla.project.service.impl.AdServiceImpl;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(classes = { AdServiceImpl.class, AdMapperImpl.class })
@ExtendWith(MockitoExtension.class)
public class AdServiceTest {

  @MockBean
  private AdRepository adRepository;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private RatingRepository ratingRepository;

  @Autowired
  private AdServiceImpl adService;


  @Test
  public void testGetFilteredAdsForUser() {
    long userId = 1L;
    String searchString = "Test";
    String category = "open";
    Integer minPrice = 10;
    Integer maxPrice = 100;

    List<Ad> ads = Collections.singletonList(createAd(userId));

    when(adRepository.findAllByNotSellerIdAndIsClosedFalse(userId)).thenReturn(ads);

    ResponseEntity<?> responseEntity = adService.getFilteredAdsForUser(userId, searchString, category, minPrice, maxPrice, null);

    assertNotNull(responseEntity);
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
  }

  @Test
  public void testGetAd() {
    long adId = 1L;
    long userId = 1L;

    Ad ad = createAd(userId);

    when(adRepository.findById(adId)).thenReturn(Optional.of(ad));

    ResponseEntity<?> responseEntity = adService.getAd(adId, userId);

    assertNotNull(responseEntity);
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
  }

  @Test
  public void testCreateAd() {
    long userId = 1L;

    AdRequest adRequest = createAdRequest();
    User seller = createUser(userId);

    when(userRepository.findById(userId)).thenReturn(Optional.of(seller));
    when(adRepository.save(any(Ad.class))).thenAnswer(invocation -> invocation.getArgument(0));

    AdCurrentResponse response = adService.createAd(userId, adRequest);

    assertNotNull(response);
    assertEquals(adRequest.getTitle(), response.getTitle());
  }

  @Test
  public void testUpdateAd() {
    long adId = 1L;

    AdRequest adRequest = createAdRequest();
    Ad ad = createAd(adId);

    when(adRepository.existsById(adId)).thenReturn(true);
    when(adRepository.findById(adId)).thenReturn(Optional.of(ad));

    boolean result = adService.updateAd(adId, adRequest);

    assertTrue(result);
    assertEquals(adRequest.getTitle(), ad.getTitle());
  }

  @Test
  public void testMakeAdPremium() {
    long adId = 1L;

    Ad ad = createAd(adId);

    when(adRepository.existsById(adId)).thenReturn(true);
    when(adRepository.findById(adId)).thenReturn(Optional.of(ad));

    boolean result = adService.makeAdPremium(adId);

    assertTrue(result);
    assertTrue(ad.isPremium());
  }

  @Test
  public void testDeleteAd() {
    long adId = 1L;

    when(adRepository.existsById(adId)).thenReturn(true);

    boolean result = adService.deleteAd(adId);

    assertTrue(result);
    verify(adRepository, times(1)).deleteById(adId);
  }

  @Test
  public void testDoesAdExist() {
    long adId = 1L;

    when(adRepository.existsById(adId)).thenReturn(true);

    assertTrue(adService.doesAdExist(adId));
  }

  @Test
  public void testDoesAdBelongToUser() {
    long adId = 1L;
    long userId = 1L;

    Ad ad = createAd(userId);

    when(adRepository.findById(adId)).thenReturn(Optional.of(ad));

    assertTrue(adService.doesAdBelongToUser(adId, userId));
  }

  @Test
  public void testDoesUserHaveConversationAlready() {
    long adId = 1L;
    long userId = 1L;

    User user = createUser(userId);

    Conversation conversation = createConversation(adId);
    user.getConversationsAsInitiator().add(conversation);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    assertTrue(adService.doesUserHaveConversationAlready(adId, userId));
  }

  @Test
  public void testIsAdSoldToUser() {
    long adId = 1L;
    long sellerId = 1L;
    long buyerId = 2L;

    Ad ad = createAd(sellerId);
    ad.setBuyer(createUser(buyerId));

    when(adRepository.findById(adId)).thenReturn(Optional.of(ad));

    assertTrue(adService.isAdSoldToUser(adId, buyerId));
  }

  @Test
  public void testIsAdScored() {
    long adId = 1L;

    Ad ad = createAd(1L);
    ad.setScore(new Score());

    when(adRepository.findById(adId)).thenReturn(Optional.of(ad));

    assertTrue(adService.isAdScored(adId));
  }

  @Test
  public void testIsAdClosed() {
    long adId = 1L;

    Ad ad = createAd(1L);
    ad.setClosed(true);

    when(adRepository.findById(adId)).thenReturn(Optional.of(ad));

    assertTrue(adService.isAdClosed(adId));
  }

  @Test
  public void testIsAdPremium() {
    long adId = 1L;

    Ad ad = createAd(1L);
    ad.setPremium(true);

    when(adRepository.findById(adId)).thenReturn(Optional.of(ad));

    assertTrue(adService.isAdPremium(adId));
  }

  private Ad createAd(long userId) {
    Ad ad = new Ad();
    ad.setId(1L);
    ad.setSeller(createUser(userId));
    ad.setTitle("Test Ad");
    ad.setContent("Test content");
    ad.setPrice(50);
    ad.setPostedAt(LocalDateTime.now());
    return ad;
  }

  private User createUser(long userId) {
    User user = new User();
    user.setId(userId);
    user.setUsername("TestUser");
    user.setEmail("test@example.com");
    user.setAddress("Test Address");
    return user;
  }

  private AdRequest createAdRequest() {
    AdRequest request = new AdRequest();
    request.setTitle("Test Ad");
    request.setContent("Test content");
    request.setPrice(50);
    return request;
  }

  private Conversation createConversation(long adId) {
    Conversation conversation = new Conversation();
    conversation.setId(1L);
    conversation.setAd(createAd(adId));
    return conversation;
  }
}

