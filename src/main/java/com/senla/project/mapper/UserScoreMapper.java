package com.senla.project.mapper;

import com.senla.project.dto.UserScoreRequest;
import com.senla.project.entities.UserScore;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserScoreMapper {

  UserScore mapToUserScore(UserScoreRequest userScoreRequest);
}
