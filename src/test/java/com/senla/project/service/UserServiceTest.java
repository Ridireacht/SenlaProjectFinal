package com.senla.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.senla.project.dto.request.UserProfileRequest;
import com.senla.project.dto.response.UserBriefProfileResponse;
import com.senla.project.dto.response.UserFullProfileResponse;
import com.senla.project.entity.Ad;
import com.senla.project.entity.User;
import com.senla.project.mapper.UserMapperImpl;
import com.senla.project.repository.AdRepository;
import com.senla.project.repository.UserRepository;
import com.senla.project.service.impl.UserDetailsServiceImpl;
import com.senla.project.service.impl.UserServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = { UserDetailsServiceImpl.class, UserRepository.class, AdRepository.class, UserServiceImpl.class, UserMapperImpl.class })
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @MockBean
  private UserDetailsServiceImpl userDetailsService;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private AdRepository adRepository;

  @Autowired
  private UserService userService;


  @Test
  public void testGetUserBriefProfile() {
    long userId = 1L;

    User user = createUser(userId);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    UserBriefProfileResponse result = userService.getUserBriefProfile(userId);

    assertNotNull(result);
    assertEquals(user.getId(), result.getId());
    assertEquals(user.getUsername(), result.getUsername());
    assertEquals(user.getAddress(), result.getAddress());
  }

  @Test
  public void testGetUserIdByUsername() {
    long userId = 1L;
    String username = "testUser";

    User user = createUser(userId);

    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

    long result = userService.getUserIdByUsername(username);

    assertEquals(user.getId(), result);
  }

  @Test
  public void testGetUserFullProfile() {
    long userId = 1L;

    User user = createUser(userId);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    UserFullProfileResponse result = userService.getUserFullProfile(userId);

    assertNotNull(result);
    assertEquals(user.getId(), result.getId());
    assertEquals(user.getUsername(), result.getUsername());
    assertEquals(user.getEmail(), result.getEmail());
    assertEquals(user.getAddress(), result.getAddress());
  }

  @Test
  public void testUpdateUserProfile() {
    long userId = 1L;

    UserProfileRequest userProfileRequest = createUserProfileRequest();
    User user = createUser(userId);

    when(userRepository.existsById(userId)).thenReturn(true);
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userDetailsService.encodePassword(userProfileRequest.getPassword())).thenReturn("encodedPassword");
    when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

    boolean result = userService.updateUserProfile(userProfileRequest, userId);

    assertTrue(result);
    assertEquals(userProfileRequest.getEmail(), user.getEmail());
    assertEquals(userProfileRequest.getAddress(), user.getAddress());
    assertEquals("encodedPassword", user.getPassword());
  }

  @Test
  public void testUpdateUserProfile_UserNotFound() {
    long userId = 1L;

    UserProfileRequest userProfileRequest = createUserProfileRequest();

    when(userRepository.existsById(userId)).thenReturn(false);

    boolean result = userService.updateUserProfile(userProfileRequest, userId);

    assertFalse(result);
  }

  @Test
  public void testDoesUserExist() {
    long userId = 1L;

    when(userRepository.existsById(userId)).thenReturn(true);

    boolean result = userService.doesUserExist(userId);

    assertTrue(result);
  }

  @Test
  public void testDoesUserExistByEmail() {
    String email = "test@test.com";

    when(userRepository.existsByEmail(email)).thenReturn(true);

    boolean result = userService.doesUserExistByEmail(email);

    assertTrue(result);
  }

  @Test
  public void testIsUserBuyerOrSellerOfAd_Seller() {
    long userId = 1L;
    long adId = 2L;

    Ad ad = createAd();
    ad.setSeller(createUser(1L));
    ad.setBuyer(createUser(2L));

    when(adRepository.findById(adId)).thenReturn(Optional.of(ad));

    boolean result = userService.isUserBuyerOrSellerOfAd(userId, adId);

    assertTrue(result);
  }

  @Test
  public void testIsUserBuyerOrSellerOfAd_Buyer() {
    long userId = 2L;
    long adId = 2L;

    Ad ad = createAd();
    ad.setSeller(createUser(1L));
    ad.setBuyer(createUser(2L));

    when(adRepository.findById(adId)).thenReturn(Optional.of(ad));

    boolean result = userService.isUserBuyerOrSellerOfAd(userId, adId);

    assertTrue(result);
  }

  @Test
  public void testIsUserBuyerOrSellerOfAd_NotRelated() {
    long userId = 3L;
    long adId = 2L;

    Ad ad = createAd();
    ad.setSeller(createUser(1L));
    ad.setBuyer(createUser(2L));

    when(adRepository.findById(adId)).thenReturn(Optional.of(ad));

    boolean result = userService.isUserBuyerOrSellerOfAd(userId, adId);

    assertFalse(result);
  }

  private User createUser(long userId) {
    User user = new User();
    user.setId(userId);
    user.setUsername("testUser");
    user.setEmail("test@test.com");
    user.setAddress("Test Address");
    user.setPassword("testPassword");
    return user;
  }

  private UserProfileRequest createUserProfileRequest() {
    UserProfileRequest userProfileRequest = new UserProfileRequest();
    userProfileRequest.setEmail("new@test.com");
    userProfileRequest.setAddress("New Address");
    userProfileRequest.setPassword("newPassword");
    return userProfileRequest;
  }

  private Ad createAd() {
    Ad ad = new Ad();
    ad.setId(2L);
    return ad;
  }
}

