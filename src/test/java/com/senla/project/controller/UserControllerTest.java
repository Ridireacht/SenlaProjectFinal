package com.senla.project.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @MockBean
  private AuthService authService;


  @Test
  public void testGetUserBriefProfile() throws Exception {
    when(userService.doesUserExist(anyLong())).thenReturn(true);
    when(userService.getUserBriefProfile(anyLong())).thenReturn(new UserBriefProfileResponse());

    mockMvc.perform(MockMvcRequestBuilders.get("/users/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void testGetCurrentUserFullProfile() throws Exception {
    when(authService.getCurrentUserId()).thenReturn(1L);
    when(userService.getUserFullProfile(anyLong())).thenReturn(new UserFullProfileResponse());

    mockMvc.perform(MockMvcRequestBuilders.get("/users/current")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void testUpdateCurrentUserProfile() throws Exception {
    when(authService.getCurrentUserId()).thenReturn(1L);
    when(userService.updateUserProfile(any(UserProfileRequest.class), anyLong())).thenReturn(true);

    String requestBody = "{\"email\": \"test@example.com\", \"address\": \"123 Street\", \"password\": \"password123\"}";

    mockMvc.perform(MockMvcRequestBuilders.put("/users/current")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isOk());
  }
}