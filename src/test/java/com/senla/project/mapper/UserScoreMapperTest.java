package com.senla.project.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.senla.project.dto.UserResponse;
import com.senla.project.dto.UserScoreRequest;
import com.senla.project.entities.User;
import com.senla.project.entities.UserScore;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserScoreMapperTest {

  private final UserScoreMapper mapper = Mappers.getMapper(UserScoreMapper.class);

  @Test
  public void testMapToUserScore() {
    UserScoreRequest userScoreRequest = new UserScoreRequest();
    userScoreRequest.setScore(5);

    UserScore expectedEntity = new UserScore();
    expectedEntity.setScore(5);

    UserScore actualEntity = mapper.mapToUserScore(userScoreRequest);

    assertEquals(expectedEntity.getScore(), actualEntity.getScore());
  }
}
