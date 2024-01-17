package com.senla.project.dto;

import com.senla.project.entities.Message;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConversationResponse {
  private Long id;
  private List<Message> messages = new ArrayList<>();
}
