package com.senla.project.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.project.config.SecurityConfig;
import com.senla.project.dto.request.ProposalRequest;
import com.senla.project.dto.response.ProposalReceivedResponse;
import com.senla.project.dto.response.ProposalSentResponse;
import com.senla.project.exception.GlobalExceptionHandler;
import com.senla.project.service.AdService;
import com.senla.project.service.AuthService;
import com.senla.project.service.ProposalService;
import java.util.ArrayList;
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

  @Autowired
  private ObjectMapper objectMapper;


  @Test
  public void testGetSentProposalsOfCurrentUser_Success() throws Exception {
    Long userId = 1L;
    List<ProposalSentResponse> expectedResponse = new ArrayList<>();

    ProposalSentResponse proposal1 = new ProposalSentResponse();
    proposal1.setAdId(1L);
    proposal1.setPrice(100);

    ProposalSentResponse proposal2 = new ProposalSentResponse();
    proposal2.setAdId(2L);
    proposal2.setPrice(200);

    expectedResponse.add(proposal1);
    expectedResponse.add(proposal2);

    when(authService.getCurrentUserId()).thenReturn(userId);
    when(proposalService.getSentProposalsOfUser(userId)).thenReturn(expectedResponse);

    mockMvc.perform(get("/proposals/sent"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].adId", is(1)))
        .andExpect(jsonPath("$[0].price", is(100)))
        .andExpect(jsonPath("$[1].adId", is(2)))
        .andExpect(jsonPath("$[1].price", is(200)));
  }

  @Test
  public void testGetReceivedProposalsOfCurrentUser_Success() throws Exception {
    Long userId = 1L;
    List<ProposalReceivedResponse> expectedResponse = new ArrayList<>();

    ProposalReceivedResponse proposal1 = new ProposalReceivedResponse();
    proposal1.setAdId(1L);
    proposal1.setSenderId(2L);
    proposal1.setPrice(100);

    ProposalReceivedResponse proposal2 = new ProposalReceivedResponse();
    proposal2.setAdId(2L);
    proposal2.setSenderId(3L);
    proposal2.setPrice(200);

    expectedResponse.add(proposal1);
    expectedResponse.add(proposal2);

    when(authService.getCurrentUserId()).thenReturn(userId);
    when(proposalService.getReceivedProposalsOfUser(userId)).thenReturn(expectedResponse);

    mockMvc.perform(get("/proposals/received"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].adId", is(1)))
        .andExpect(jsonPath("$[0].senderId", is(2)))
        .andExpect(jsonPath("$[0].price", is(100)))
        .andExpect(jsonPath("$[1].adId", is(2)))
        .andExpect(jsonPath("$[1].senderId", is(3)))
        .andExpect(jsonPath("$[1].price", is(200)));
  }

  @Test
  public void testSendProposal_Success() throws Exception {
    Long adId = 1L;
    Long userId = 1L;

    ProposalRequest proposalRequest = new ProposalRequest();
    proposalRequest.setAdId(adId);
    proposalRequest.setPrice(100);

    ProposalSentResponse expectedResponse = new ProposalSentResponse();
    expectedResponse.setAdId(adId);
    expectedResponse.setPrice(100);

    when(authService.getCurrentUserId()).thenReturn(userId);
    when(adService.doesAdExist(adId)).thenReturn(true);
    when(adService.doesAdBelongToUser(adId, userId)).thenReturn(false);
    when(adService.isAdClosed(adId)).thenReturn(false);
    when(proposalService.createProposal(eq(userId), any(ProposalRequest.class))).thenReturn(expectedResponse);

    mockMvc.perform(post("/proposals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(proposalRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.adId", is(1)))
        .andExpect(jsonPath("$.price", is(100)));
  }

  @Test
  public void testSendProposal_AdNotFound() throws Exception {
    ProposalRequest proposalRequest = new ProposalRequest();
    proposalRequest.setAdId(1L);
    proposalRequest.setPrice(100);

    when(adService.doesAdExist(1L)).thenReturn(false);

    mockMvc.perform(post("/proposals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(proposalRequest)))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testSendProposal_CannotSendToOwnAd() throws Exception {
    Long userId = 1L;
    ProposalRequest proposalRequest = new ProposalRequest();
    proposalRequest.setAdId(1L);
    proposalRequest.setPrice(100);

    when(authService.getCurrentUserId()).thenReturn(userId);
    when(adService.doesAdExist(1L)).thenReturn(true);
    when(adService.doesAdBelongToUser(1L, userId)).thenReturn(true);

    mockMvc.perform(post("/proposals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(proposalRequest)))
        .andExpect(status().isForbidden());
  }

  @Test
  public void testSendProposal_AdClosed() throws Exception {
    Long userId = 1L;
    ProposalRequest proposalRequest = new ProposalRequest();
    proposalRequest.setAdId(1L);
    proposalRequest.setPrice(100);

    when(adService.doesAdExist(1L)).thenReturn(true);
    when(adService.doesAdBelongToUser(1L, userId)).thenReturn(false);
    when(adService.isAdClosed(1L)).thenReturn(true);

    mockMvc.perform(post("/proposals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(proposalRequest)))
        .andExpect(status().isForbidden());
  }

  @Test
  public void testAcceptProposalById_Success() throws Exception {
    Long proposalId = 1L;

    when(authService.getCurrentUserId()).thenReturn(2L);
    when(proposalService.doesProposalExist(proposalId)).thenReturn(true);
    when(proposalService.isProposalSentToUser(proposalId, 2L)).thenReturn(true);
    when(proposalService.acceptProposalById(proposalId)).thenReturn(true);

    mockMvc.perform(post("/proposals/received/{id}", proposalId))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));
  }

  @Test
  public void testAcceptProposalById_ProposalNotFound() throws Exception {
    Long proposalId = 1L;

    when(proposalService.doesProposalExist(proposalId)).thenReturn(false);

    mockMvc.perform(post("/proposals/received/{id}", proposalId))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testAcceptProposalById_NotSentToCurrentUser() throws Exception {
    Long proposalId = 1L;

    when(authService.getCurrentUserId()).thenReturn(2L);
    when(proposalService.doesProposalExist(proposalId)).thenReturn(true);
    when(proposalService.isProposalSentToUser(proposalId, 2L)).thenReturn(false);

    mockMvc.perform(post("/proposals/received/{id}", proposalId))
        .andExpect(status().isForbidden());
  }

  @Test
  public void testDeclineProposalById_Success() throws Exception {
    Long proposalId = 1L;
    Long userId = 1L;

    when(authService.getCurrentUserId()).thenReturn(userId);
    when(proposalService.isProposalSentToUser(proposalId, userId)).thenReturn(true);
    when(proposalService.doesProposalExist(proposalId)).thenReturn(true);
    when(proposalService.declineProposalById(proposalId)).thenReturn(true);

    mockMvc.perform(delete("/proposals/received/{id}", proposalId))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));
  }

  @Test
  public void testDeclineProposalById_ProposalNotFound() throws Exception {
    Long proposalId = 1L;

    when(proposalService.doesProposalExist(proposalId)).thenReturn(false);

    mockMvc.perform(delete("/proposals/received/{id}", proposalId))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testDeclineProposalById_NotSentToCurrentUser() throws Exception {
    Long proposalId = 1L;

    when(authService.getCurrentUserId()).thenReturn(2L);
    when(proposalService.doesProposalExist(proposalId)).thenReturn(true);
    when(proposalService.isProposalSentToUser(proposalId, 2L)).thenReturn(false);

    mockMvc.perform(delete("/proposals/received/{id}", proposalId))
        .andExpect(status().isForbidden());
  }
}
