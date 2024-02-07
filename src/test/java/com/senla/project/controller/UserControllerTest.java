package com.senla.project.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.project.config.SecurityConfig;
import com.senla.project.dto.request.UserProfileRequest;
import com.senla.project.dto.response.UserBriefProfileResponse;
import com.senla.project.dto.response.UserFullProfileResponse;
import com.senla.project.exception.GlobalExceptionHandler;
import com.senla.project.service.AuthService;
import com.senla.project.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(controllers =
    UserController.class,
    excludeAutoConfiguration = {
        SecurityConfig.class,
        SecurityAutoConfiguration.class
    }
)
@ContextConfiguration(classes = { UserController.class, GlobalExceptionHandler.class })
public class UserControllerTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @MockBean
  private AuthService authService;


  @Test
  public void testGetUserBriefProfile_Success() throws Exception {
    Long userId = 1L;
    UserBriefProfileResponse expectedResponse = new UserBriefProfileResponse();
    expectedResponse.setId(userId);
    expectedResponse.setUsername("testUser");
    expectedResponse.setAddress("Test Address");
    expectedResponse.setRating(4.5);

    when(userService.doesUserExist(userId)).thenReturn(true);
    when(userService.getUserBriefProfile(userId)).thenReturn(expectedResponse);

    mockMvc.perform(get("/users/{id}", userId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(userId.intValue())))
        .andExpect(jsonPath("$.username", is("testUser")))
        .andExpect(jsonPath("$.address", is("Test Address")))
        .andExpect(jsonPath("$.rating", is(4.5)));
  }

  @Test
  public void testGetUserBriefProfile_UserNotFound() throws Exception {
    Long userId = 1L;

    when(userService.doesUserExist(userId)).thenReturn(false);

    mockMvc.perform(get("/users/{id}", userId))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testGetCurrentUserFullProfile_Success() throws Exception {
    Long userId = 1L;
    UserFullProfileResponse expectedResponse = new UserFullProfileResponse();
    expectedResponse.setId(userId);
    expectedResponse.setUsername("testUser");
    expectedResponse.setEmail("test@example.com");
    expectedResponse.setAddress("Test Address");
    expectedResponse.setRating(4.5);

    when(authService.getCurrentUserId()).thenReturn(userId);
    when(userService.getUserFullProfile(userId)).thenReturn(expectedResponse);

    mockMvc.perform(get("/users/current"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(userId.intValue())))
        .andExpect(jsonPath("$.username", is("testUser")))
        .andExpect(jsonPath("$.email", is("test@example.com")))
        .andExpect(jsonPath("$.address", is("Test Address")))
        .andExpect(jsonPath("$.rating", is(4.5)));
  }

  @Test
  public void testUpdateCurrentUserProfile_Success() throws Exception {
    UserProfileRequest userProfileRequest = new UserProfileRequest();
    userProfileRequest.setEmail("newemail@example.com");
    userProfileRequest.setAddress("New Address");

    when(authService.getCurrentUserId()).thenReturn(1L);
    when(userService.doesUserExistByEmail(userProfileRequest.getEmail())).thenReturn(false);
    when(userService.updateUserProfile(any(UserProfileRequest.class), eq(1L))).thenReturn(true);

    mockMvc.perform(put("/users/current")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userProfileRequest)))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));
  }

  @Test
  public void testUpdateCurrentUserProfile_ConflictEmail() throws Exception {
    UserProfileRequest userProfileRequest = new UserProfileRequest();
    userProfileRequest.setEmail("existingemail@example.com");

    when(authService.getCurrentUserId()).thenReturn(1L);
    when(userService.doesUserExistByEmail(userProfileRequest.getEmail())).thenReturn(true);

    mockMvc.perform(put("/users/current")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userProfileRequest)))
        .andExpect(status().isConflict());
  }

  @Test
  public void testUpdateCurrentUserProfile_EmptyRequest() throws Exception {
    UserProfileRequest userProfileRequest = new UserProfileRequest();

    mockMvc.perform(put("/users/current")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userProfileRequest)))
        .andExpect(status().isBadRequest());
  }
}