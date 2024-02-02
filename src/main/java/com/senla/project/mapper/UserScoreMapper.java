package com.senla.project.mapper;

import com.senla.project.dto.request.UserScoreRequest;
import com.senla.project.entity.UserScore;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface UserScoreMapper {

  UserScore mapToUserScore(UserScoreRequest userScoreRequest);
}
