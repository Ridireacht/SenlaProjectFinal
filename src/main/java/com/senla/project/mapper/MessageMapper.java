package com.senla.project.mapper;

import com.senla.project.dto.MessageRequest;
import com.senla.project.dto.MessageResponse;
import com.senla.project.entities.Message;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MessageMapper {

  MessageMapper MAPPER = Mappers.getMapper(MessageMapper.class);


  MessageResponse mapToMessageResponse(Message message);

  Message mapToMessage(MessageRequest messageRequest);
}
