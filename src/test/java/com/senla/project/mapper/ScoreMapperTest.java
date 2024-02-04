package com.senla.project.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.senla.project.dto.request.ScoreRequest;
import com.senla.project.entity.Score;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.AUTO_CONFIGURED)
public class ScoreMapperTest {

  @Autowired
  private ScoreMapper scoreMapper;


  @Test
  public void testMapToScore() {
    ScoreRequest scoreRequest = createScoreRequest();
    Score score = scoreMapper.mapToScore(scoreRequest);

    assertEquals(scoreRequest.getValue(), score.getValue());
  }

  private ScoreRequest createScoreRequest() {
    ScoreRequest scoreRequest = new ScoreRequest();
    scoreRequest.setValue(4);
    return scoreRequest;
  }
}
