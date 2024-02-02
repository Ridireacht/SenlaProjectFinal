package com.senla.project.mapper;

import com.senla.project.dto.request.ScoreRequest;
import com.senla.project.entity.Score;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface ScoreMapper {

  Score mapToScore(ScoreRequest scoreRequest);
}
