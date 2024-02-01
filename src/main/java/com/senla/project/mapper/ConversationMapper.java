package com.senla.project.mapper;

import com.senla.project.dto.response.ConversationResponse;
import com.senla.project.dto.response.ConversationInfoResponse;
import com.senla.project.entity.Conversation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConversationMapper {

  @Mapping(source = "ad.id", target = "adId")
  ConversationInfoResponse mapToConversationInfoResponse(Conversation conversation);

  @Mapping(source = "ad.id", target = "adId")
  ConversationResponse mapToConversationResponse(Conversation conversation);
}
