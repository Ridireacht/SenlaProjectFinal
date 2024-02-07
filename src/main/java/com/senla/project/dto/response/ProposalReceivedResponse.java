package com.senla.project.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProposalReceivedResponse {
  private Long id;
  private Long adId;
  private Long senderId;
  private int price;
}
