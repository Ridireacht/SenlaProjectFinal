package com.senla.project.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.senla.project.config.SecurityConfig;
import com.senla.project.dto.response.ProposalSentResponse;
import com.senla.project.exception.GlobalExceptionHandler;
import com.senla.project.service.AdService;
import com.senla.project.service.AuthService;
import com.senla.project.service.ProposalService;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(
    controllers = ProposalController.class,
    excludeAutoConfiguration = {
        SecurityConfig.class,
        SecurityAutoConfiguration.class
    })
@ContextConfiguration(classes = { ProposalController.class, GlobalExceptionHandler.class })
public class ProposalControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ProposalService proposalService;

  @MockBean
  private AdService adService;

  @MockBean
  private AuthService authService;


  @Test
  public void testGetSentProposalsOfCurrentUser() throws Exception {
    when(proposalService.getSentProposalsOfUser(anyLong())).thenReturn(Collections.emptyList());

    mockMvc.perform(MockMvcRequestBuilders.get("/proposals/sent"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void testGetReceivedProposalsOfCurrentUser() throws Exception {
    when(proposalService.getReceivedProposalsOfUser(anyLong())).thenReturn(Collections.emptyList());

    mockMvc.perform(MockMvcRequestBuilders.get("/proposals/received"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void testSendProposal() throws Exception {
    when(adService.doesAdExist(anyLong())).thenReturn(true);
    when(adService.doesAdBelongToUser(anyLong(), anyLong())).thenReturn(false);
    when(adService.isAdClosed(anyLong())).thenReturn(false);
    when(proposalService.createProposal(anyLong(), any())).thenReturn(new ProposalSentResponse());

    String requestBody = "{\"adId\": 1, \"price\": 100}";

    mockMvc.perform(MockMvcRequestBuilders.post("/proposals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void testSendProposalWithInvalidAd() throws Exception {
    when(adService.doesAdExist(anyLong())).thenReturn(false);

    String requestBody = "{\"adId\": 1, \"price\": 100}";

    mockMvc.perform(MockMvcRequestBuilders.post("/proposals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testSendProposalWithClosedAd() throws Exception {
    when(adService.doesAdExist(anyLong())).thenReturn(true);
    when(adService.doesAdBelongToUser(anyLong(), anyLong())).thenReturn(false);
    when(adService.isAdClosed(anyLong())).thenReturn(true);

    String requestBody = "{\"adId\": 1, \"price\": 100}";

    mockMvc.perform(MockMvcRequestBuilders.post("/proposals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isForbidden());
  }

  @Test
  public void testAcceptProposalById() throws Exception {
    when(proposalService.doesProposalExist(anyLong())).thenReturn(true);
    when(proposalService.isProposalSentToUser(anyLong(), anyLong())).thenReturn(true);
    when(proposalService.acceptProposalById(anyLong())).thenReturn(true);

    mockMvc.perform(MockMvcRequestBuilders.post("/proposals/received/1"))
        .andExpect(status().isOk());
  }

  @Test
  public void testDeclineProposalById() throws Exception {
    when(proposalService.doesProposalExist(anyLong())).thenReturn(true);
    when(proposalService.isProposalSentToUser(anyLong(), anyLong())).thenReturn(true);
    when(proposalService.declineProposalById(anyLong())).thenReturn(true);

    mockMvc.perform(MockMvcRequestBuilders.delete("/proposals/received/1"))
        .andExpect(status().isOk());
  }
}
