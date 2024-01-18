package com.senla.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProposalReceivedResponse {
  private Long adId;
  private Long senderId;
  private int price;
}
