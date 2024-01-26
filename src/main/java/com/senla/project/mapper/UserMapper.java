package com.senla.project.mapper;

import com.senla.project.dto.response.UserResponse;
import com.senla.project.entity.User;
import com.senla.project.repository.RatingRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

  @Autowired
  protected RatingRepository ratingRepository;


  @Mapping(target = "rating", expression = "java(ratingRepository.findByUserId(user.getId()).get().getAverageScore())")
  public abstract UserResponse mapToUserResponse(User user);
}
