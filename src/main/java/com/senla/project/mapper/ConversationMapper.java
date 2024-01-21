package com.senla.project.mapper;

import com.senla.project.dto.ConversationResponse;
import com.senla.project.entities.Conversation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ConversationMapper {

  ConversationMapper MAPPER = Mappers.getMapper(ConversationMapper.class);

  @Mapping(source = "ad.id", target = "adId")
  ConversationResponse mapToConversationResponse(Conversation conversation);
}