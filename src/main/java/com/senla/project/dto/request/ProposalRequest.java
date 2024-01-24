package com.senla.project.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProposalRequest {
  @NotNull(message = "Ad ID must not be null")
  private Long adId;

  @NotNull(message = "Price must not be null")
  @Min(value = 1, message = "Price must be greater than 0")
  private int price;
}
