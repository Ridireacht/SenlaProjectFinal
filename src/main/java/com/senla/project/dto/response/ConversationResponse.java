package com.senla.project.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConversationResponse {
  private Long id;
  private Long adId;
  private List<MessageResponse> messages = new ArrayList<>();
}
