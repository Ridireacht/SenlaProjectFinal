package com.senla.project.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.senla.project.dto.UserScoreRequest;
import com.senla.project.entity.UserScore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserScoreMapperTest {

  @Autowired
  UserScoreMapper mapper;


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
