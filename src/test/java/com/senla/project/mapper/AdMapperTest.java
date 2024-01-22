package com.senla.project.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.senla.project.dto.AdRequest;
import com.senla.project.entities.Ad;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AdMapperTest {

  private final AdMapper mapper = Mappers.getMapper(AdMapper.class);


  @Test
  public void testMapToAd() {
    AdRequest adRequest = new AdRequest();
    adRequest.setTitle("testTitle");
    adRequest.setContent("testContent");
    adRequest.setPrice(100);

    Ad expectedEntity = new Ad();
    expectedEntity.setTitle("testTitle");
    expectedEntity.setContent("testContent");
    expectedEntity.setPrice(100);

    Ad actualEntity = mapper.mapToAd(adRequest);

    assertEquals(expectedEntity.getTitle(), actualEntity.getTitle());
    assertEquals(expectedEntity.getContent(), actualEntity.getContent());
    assertEquals(expectedEntity.getPrice(), actualEntity.getPrice());
  }
}
