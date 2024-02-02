package com.senla.project.mapper;

import com.senla.project.dto.response.UserBriefProfileResponse;
import com.senla.project.dto.response.UserFullProfileResponse;
import com.senla.project.entity.User;
import com.senla.project.repository.RatingRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mapping(source = "rating.averageScore", target = "rating")
  UserBriefProfileResponse mapToUserBriefProfileResponse(User user);

  @Mapping(source = "rating.averageScore", target = "rating")
  UserFullProfileResponse mapToUserFullProfileResponse(User user);
}
