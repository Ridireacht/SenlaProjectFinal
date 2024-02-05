package com.senla.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import com.senla.project.dto.response.AdFullOpenResponse;
import com.senla.project.dto.response.UserFullProfileResponse;
import com.senla.project.entity.Ad;
import com.senla.project.entity.Comment;
import com.senla.project.entity.User;
import com.senla.project.mapper.AdMapperImpl;
import com.senla.project.mapper.UserMapperImpl;
import com.senla.project.repository.AdRepository;
import com.senla.project.repository.CommentRepository;
import com.senla.project.repository.RatingRepository;
import com.senla.project.repository.UserRepository;
import com.senla.project.service.impl.AdminServiceImpl;
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

@SpringBootTest(classes = { AdminServiceImpl.class, UserMapperImpl.class, AdMapperImpl.class })
@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private AdRepository adRepository;

  @MockBean
  private CommentRepository commentRepository;

  @MockBean
  private RatingRepository ratingRepository;

  @Autowired
  private AdminServiceImpl adminService;


  @Test
  public void testGetFilteredAdsForUser() {
    long userId = 1L;
    String searchString = "test";
    String category = "current";
    Integer minPrice = 10;
    Integer maxPrice = 100;

    List<Ad> ads = Collections.singletonList(createAd(userId));

    when(adRepository.findAllBySellerIdAndIsClosedFalse(userId)).thenReturn(ads);

    ResponseEntity<?> responseEntity = adminService.getFilteredAdsForUser(userId, searchString, category, minPrice, maxPrice);

    assertNotNull(responseEntity);
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
  }

  @Test
  public void testGetAllOpenAdsFull() {
    String searchString = "Test";
    Integer minPrice = 10;
    Integer maxPrice = 100;

    List<Ad> ads = Collections.singletonList(createAd(1L));

    when(adRepository.findAllByIsClosedFalse()).thenReturn(ads);

    List<AdFullOpenResponse> response = adminService.getAllOpenAdsFull(searchString, minPrice, maxPrice);

    assertNotNull(response);
    assertEquals(1, response.size());
  }

  @Test
  public void testGetUserFullProfiles() {
    List<User> users = Collections.singletonList(createUser(1L));

    when(userRepository.findAll()).thenReturn(users);

    List<UserFullProfileResponse> response = adminService.getUserFullProfiles();

    assertNotNull(response);
    assertEquals(1, response.size());
  }

  @Test
  public void testGetUserFullProfile() {
    long userId = 1L;
    User user = createUser(userId);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    UserFullProfileResponse response = adminService.getUserFullProfile(userId);

    assertNotNull(response);
    assertEquals(userId, response.getId());
  }

  @Test
  public void testGetAd() {
    long adId = 1L;
    Ad ad = createAd(1L);

    when(adRepository.findById(adId)).thenReturn(Optional.of(ad));

    ResponseEntity<?> responseEntity = adminService.getAd(adId);

    assertNotNull(responseEntity);
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
  }

  @Test
  public void testGetAdId() {
    long commentId = 1L;

    Comment comment = createComment(commentId);
    comment.setAd(createAd(2L));

    when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

    long response = adminService.getAdId(commentId);

    assertEquals(comment.getAd().getId(), response);
  }

  @Test
  public void testUnmakeAdPremium() {
    long adId = 1L;
    Ad ad = createAd(adId);

    when(adRepository.findById(adId)).thenReturn(Optional.of(ad));

    boolean result = adminService.unmakeAdPremium(adId);

    assertTrue(result);
    assertFalse(ad.isPremium());
  }

  @Test
  public void testDeleteUser() {
    long userId = 1L;

    when(userRepository.existsById(userId)).thenReturn(true);

    boolean result = adminService.deleteUser(userId);

    assertTrue(result);
    verify(userRepository, times(1)).deleteById(userId);
    verify(adRepository, times(1)).deleteAllBySellerIdAndIsClosedFalse(userId);
  }

  @Test
  public void testDeleteComment() {
    long commentId = 1L;

    when(commentRepository.existsById(commentId)).thenReturn(true);

    boolean result = adminService.deleteComment(commentId);

    assertTrue(result);
    verify(commentRepository, times(1)).deleteById(commentId);
  }

  @Test
  public void testDeleteAd() {
    long adId = 1L;

    when(adRepository.existsById(adId)).thenReturn(true);

    boolean result = adminService.deleteAd(adId);

    assertTrue(result);
    verify(adRepository, times(1)).deleteById(adId);
  }

  @Test
  public void testDoesUserExist() {
    long userId = 1L;

    when(userRepository.existsById(userId)).thenReturn(true);

    assertTrue(adminService.doesUserExist(userId));
  }

  @Test
  public void testDoesCommentExist() {
    long commentId = 1L;

    when(commentRepository.existsById(commentId)).thenReturn(true);

    assertTrue(adminService.doesCommentExist(commentId));
  }

  @Test
  public void testDoesAdExist() {
    long adId = 1L;

    when(adRepository.existsById(adId)).thenReturn(true);

    assertTrue(adminService.doesAdExist(adId));
  }

  @Test
  public void testIsAdClosed() {
    long adId = 1L;
    Ad ad = createAd(1L);
    ad.setClosed(true);

    when(adRepository.findById(adId)).thenReturn(Optional.of(ad));

    assertTrue(adminService.isAdClosed(adId));
  }

  @Test
  public void testIsAdPremium() {
    long adId = 1L;
    Ad ad = createAd(1L);
    ad.setPremium(true);
    
    when(adRepository.findById(adId)).thenReturn(Optional.of(ad));

    assertTrue(adminService.isAdPremium(adId));
  }

  private Ad createAd(long adId) {
    Ad ad = new Ad();
    ad.setId(adId);
    ad.setPremium(false);
    ad.setPostedAt(LocalDateTime.now());
    ad.setClosed(false);
    ad.setTitle("Test Ad");
    ad.setContent("Test content");
    ad.setPrice(50);
    return ad;
  }

  private User createUser(long userId) {
    User user = new User();
    user.setId(userId);
    user.setUsername("TestUser");
    user.setEmail("test@example.com");
    user.setAddress("Test City");
    user.setPassword("password");
    return user;
  }

  private Comment createComment(long commentId) {
    Comment comment = new Comment();
    comment.setId(commentId);
    comment.setPostedAt(LocalDateTime.now());
    comment.setContent("Test Comment");
    return comment;
  }
}

