package com.senla.project.mapper;

import com.senla.project.dto.response.ConversationResponse;
import com.senla.project.dto.response.ConversationInfoResponse;
import com.senla.project.entity.Conversation;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", imports = {DateTimeFormatter.class, Collectors.class})
public abstract class ConversationMapper {

  @Autowired
  protected MessageMapper messageMapper;

  @Mapping(source = "ad.id", target = "adId")
  @Mapping(target = "updatedAt", expression = "java(conversation.getUpdatedAt().format(DateTimeFormatter.ofPattern(\"dd-MM-yyyy HH:mm\")))")
  public abstract ConversationInfoResponse mapToConversationInfoResponse(Conversation conversation);

  @Mapping(source = "ad.id", target = "adId")
  @Mapping(target = "messages", expression = "java(conversation.getMessages().stream().map(messageMapper::mapToMessageResponse).collect(Collectors.toList()))")
  public abstract ConversationResponse mapToConversationResponse(Conversation conversation);
}
