package com.senla.project.dto.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConversationInfoResponse {
  private Long id;
  private Long adId;
  private String updatedAt;
}
