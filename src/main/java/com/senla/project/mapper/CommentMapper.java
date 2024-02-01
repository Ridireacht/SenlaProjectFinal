package com.senla.project.mapper;

import com.senla.project.dto.request.CommentRequest;
import com.senla.project.dto.response.CommentResponse;
import com.senla.project.entity.Comment;
import java.time.format.DateTimeFormatter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = DateTimeFormatter.class)
public interface CommentMapper {

  @Mapping(source = "sender.id", target = "senderId")
  @Mapping(target = "postedAt", expression = "java(comment.getPostedAt().format(DateTimeFormatter.ofPattern(\"dd-MM-yyyy HH:mm\")))")
  CommentResponse mapToCommentResponse(Comment comment);

  Comment mapToComment(CommentRequest commentRequest);
}
