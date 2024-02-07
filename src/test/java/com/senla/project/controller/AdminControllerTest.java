package com.senla.project.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.project.config.SecurityConfig;
import com.senla.project.dto.response.UserFullProfileResponse;
import com.senla.project.exception.GlobalExceptionHandler;
import com.senla.project.service.AdminService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = AdminController.class,
    excludeAutoConfiguration = {
        SecurityConfig.class,
        SecurityAutoConfiguration.class
    }
)
@ContextConfiguration(classes = { AdminController.class, GlobalExceptionHandler.class })
public class AdminControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AdminService adminService;


  @Test
  public void testGetUserFullProfiles_Success() throws Exception {
    UserFullProfileResponse user1 = new UserFullProfileResponse();
    user1.setId(1L);
    user1.setUsername("user1");
    user1.setEmail("user1@example.com");
    user1.setAddress("Address1");
    user1.setRating(4.5);

    UserFullProfileResponse user2 = new UserFullProfileResponse();
    user2.setId(2L);
    user2.setUsername("user2");
    user2.setEmail("user2@example.com");
    user2.setAddress("Address2");
    user2.setRating(3.8);

    List<UserFullProfileResponse> mockUsers = Arrays.asList(user1, user2);

    when(adminService.getUserFullProfiles()).thenReturn(mockUsers);

    mockMvc.perform(get("/admin/users")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].id").value(1L))
        .andExpect(jsonPath("$[0].username").value("user1"))
        .andExpect(jsonPath("$[0].email").value("user1@example.com"))
        .andExpect(jsonPath("$[0].address").value("Address1"))
        .andExpect(jsonPath("$[0].rating").value(4.5))
        .andExpect(jsonPath("$[1].id").value(2L))
        .andExpect(jsonPath("$[1].username").value("user2"))
        .andExpect(jsonPath("$[1].email").value("user2@example.com"))
        .andExpect(jsonPath("$[1].address").value("Address2"))
        .andExpect(jsonPath("$[1].rating").value(3.8));
  }

  @Test
  public void testGetUserFullProfile_Success() throws Exception {
    Long userId = 1L;

    UserFullProfileResponse userProfile = new UserFullProfileResponse();
    userProfile.setId(userId);
    userProfile.setUsername("user1");
    userProfile.setEmail("user1@example.com");
    userProfile.setAddress("Address1");
    userProfile.setRating(4.5);

    when(adminService.doesUserExist(userId)).thenReturn(true);
    when(adminService.getUserFullProfile(userId)).thenReturn(userProfile);

    mockMvc.perform(get("/admin/users/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(userId))
        .andExpect(jsonPath("$.username").value("user1"))
        .andExpect(jsonPath("$.email").value("user1@example.com"))
        .andExpect(jsonPath("$.address").value("Address1"))
        .andExpect(jsonPath("$.rating").value(4.5));
  }

  @Test
  public void testGetUserFullProfile_UserNotFound() throws Exception {
    Long userId = 1L;

    when(adminService.doesUserExist(userId)).thenReturn(false);

    mockMvc.perform(get("/admin/users/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testDeleteUser_Success() throws Exception {
    Long userId = 1L;

    when(adminService.doesUserExist(userId)).thenReturn(true);
    when(adminService.deleteUser(userId)).thenReturn(true);

    mockMvc.perform(delete("/admin/users/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().string("true"));
  }

  @Test
  public void testDeleteUser_UserNotFound() throws Exception {
    Long userId = 1L;

    when(adminService.doesUserExist(userId)).thenReturn(false);

    mockMvc.perform(delete("/admin/users/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testDeleteComment_Success() throws Exception {
    Long commentId = 1L;

    when(adminService.doesCommentExist(commentId)).thenReturn(true);
    when(adminService.getAdId(anyLong())).thenReturn(2L);
    when(adminService.isAdClosed(2L)).thenReturn(false);
    when(adminService.deleteComment(commentId)).thenReturn(true);

    mockMvc.perform(delete("/admin/comments/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));
  }

  @Test
  public void testDeleteComment_CommentNotFound() throws Exception {
    Long commentId = 1L;

    when(adminService.doesCommentExist(commentId)).thenReturn(false);

    mockMvc.perform(delete("/admin/comments/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testDeleteComment_Forbidden() throws Exception {
    Long commentId = 1L;

    when(adminService.doesCommentExist(commentId)).thenReturn(true);
    when(adminService.getAdId(anyLong())).thenReturn(2L);
    when(adminService.isAdClosed(2L)).thenReturn(true);

    mockMvc.perform(delete("/admin/comments/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }
}
