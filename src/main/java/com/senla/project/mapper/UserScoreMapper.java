package com.senla.project.mapper;

import com.senla.project.dto.UserScoreRequest;
import com.senla.project.entities.UserScore;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserScoreMapper {

  UserScoreMapper MAPPER = Mappers.getMapper(UserScoreMapper.class);


  UserScore mapToUserScore(UserScoreRequest userScoreRequest);
}
