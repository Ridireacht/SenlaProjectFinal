package com.senla.project.mapper;

import com.senla.project.dto.response.ConversationResponse;
import com.senla.project.dto.response.ConversationInfoResponse;
import com.senla.project.entity.Conversation;
import java.time.format.DateTimeFormatter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = DateTimeFormatter.class)
public interface ConversationMapper {

  @Mapping(source = "ad.id", target = "adId")
  @Mapping(target = "updatedAt", expression = "java(conversation.getUpdatedAt().format(DateTimeFormatter.ofPattern(\"dd-MM-yyyy HH:mm\")))")
  ConversationInfoResponse mapToConversationInfoResponse(Conversation conversation);

  @Mapping(source = "ad.id", target = "adId")
  ConversationResponse mapToConversationResponse(Conversation conversation);
}
