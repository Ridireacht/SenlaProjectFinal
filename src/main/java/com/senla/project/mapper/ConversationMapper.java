package com.senla.project.mapper;

import com.senla.project.dto.ConversationResponse;
import com.senla.project.entities.Conversation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConversationMapper {

  @Mapping(source = "ad.id", target = "adId")
  ConversationResponse mapToConversationResponse(Conversation conversation);
}
