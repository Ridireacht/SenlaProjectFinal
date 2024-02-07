package com.senla.project.controller;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.senla.project.config.SecurityConfig;
import com.senla.project.dto.response.AdCurrentResponse;
import com.senla.project.dto.response.AdOpenResponse;
import com.senla.project.exception.GlobalExceptionHandler;
import com.senla.project.service.AdService;
import com.senla.project.service.AuthService;
import com.senla.project.service.UserService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = AdController.class,
    excludeAutoConfiguration = {
        SecurityConfig.class,
        SecurityAutoConfiguration.class
    }
)
@ContextConfiguration(classes = { AdController.class, GlobalExceptionHandler.class })
public class AdControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AdService adService;

  @MockBean
  private UserService userService;

  @MockBean
  private AuthService authService;


  @Test
  public void testGetFilteredAds_Success() throws Exception {
    AdOpenResponse response1 = new AdOpenResponse();
    response1.setId(1L);
    response1.setSellerId(1L);
    response1.setSellerCity("Test City");
    response1.setPostedAt("2024-02-07T10:20:30");
    response1.setTitle("Test title");
    response1.setContent("Test content");
    response1.setPrice(100);

    AdOpenResponse response2 = new AdOpenResponse();
    response2.setId(1L);
    response2.setSellerId(1L);
    response2.setSellerCity("Test City");
    response2.setPostedAt("2024-02-07T10:20:30");
    response2.setTitle("Test title");
    response2.setContent("Test content");
    response2.setPrice(100);

    List<AdOpenResponse> mockAds = Arrays.asList(response1, response2);

    doReturn(ResponseEntity.ok(mockAds)).when(adService).getFilteredAdsForUser(anyLong(), anyString(), anyString(), anyInt(), anyInt(), anyBoolean());

    mockMvc.perform(get("/ads")
            .param("searchString", "test")
            .param("category", "open")
            .param("minPrice", "10")
            .param("maxPrice", "100")
            .param("isInMyCity", "true")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].id").exists())
        .andExpect(jsonPath("$[0].sellerId").exists())
        .andExpect(jsonPath("$[0].sellerCity").exists())
        .andExpect(jsonPath("$[0].postedAt").exists())
        .andExpect(jsonPath("$[0].title").exists())
        .andExpect(jsonPath("$[0].content").exists())
        .andExpect(jsonPath("$[0].price").exists())
        .andExpect(jsonPath("$[1].id").exists())
        .andExpect(jsonPath("$[1].sellerId").exists())
        .andExpect(jsonPath("$[1].sellerCity").exists())
        .andExpect(jsonPath("$[1].postedAt").exists())
        .andExpect(jsonPath("$[1].title").exists())
        .andExpect(jsonPath("$[1].content").exists())
        .andExpect(jsonPath("$[1].price").exists());
  }

  @Test
  public void testGetFilteredAds_InvalidCategory() throws Exception {
    mockMvc.perform(get("/ads")
            .param("category", "invalid_category")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testGetFilteredAds_CategoryInMyCityMismatch() throws Exception {
    mockMvc.perform(get("/ads")
            .param("category", "closed")
            .param("isInMyCity", "true")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testGetFilteredAds_MinPriceHigherThanMaxPrice() throws Exception {
    mockMvc.perform(get("/ads")
            .param("category", "open")
            .param("minPrice", "100")
            .param("maxPrice", "10")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testGetFilteredAds_SearchStringTooShort() throws Exception {
    mockMvc.perform(get("/ads")
            .param("category", "open")
            .param("searchString", "t")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testGetAd_Success() throws Exception {
    AdCurrentResponse adResponse = new AdCurrentResponse();
    adResponse.setId(1L);

    when(authService.getCurrentUserId()).thenReturn(1L);
    when(adService.doesAdExist(anyLong())).thenReturn(true);
    when(adService.isAdClosed(anyLong())).thenReturn(false);
    when(userService.isUserBuyerOrSellerOfAd(anyLong(), anyLong())).thenReturn(true);
    doReturn(ResponseEntity.ok(adResponse)).when(adService).getAd(anyLong(), anyLong());

    mockMvc.perform(get("/ads/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L));
  }

  @Test
  public void testGetAd_NotFound() throws Exception {
    when(adService.doesAdExist(anyLong())).thenReturn(false);

    mockMvc.perform(get("/ads/999")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testGetAd_AdClosedAndForbiddenUser() throws Exception {
    when(authService.getCurrentUserId()).thenReturn(1L);
    when(adService.doesAdExist(anyLong())).thenReturn(true);
    when(adService.isAdClosed(anyLong())).thenReturn(true);
    when(userService.isUserBuyerOrSellerOfAd(anyLong(), anyLong())).thenReturn(false);

    mockMvc.perform(get("/ads/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }
}
