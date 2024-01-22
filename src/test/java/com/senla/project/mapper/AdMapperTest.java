package com.senla.project.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.senla.project.dto.AdRequest;
import com.senla.project.dto.AdResponse;
import com.senla.project.entities.Ad;
import com.senla.project.entities.User;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AdMapperTest {

  private final AdMapper mapper = Mappers.getMapper(AdMapper.class);

  @Test
  public void testMapToAdResponse() {
    User seller = new User();
    seller.setId(2L);

    Ad ad = new Ad();
    ad.setId(1L);
    ad.setSeller(seller);
    ad.setPostedAt(LocalDateTime.of(2022, 1, 17, 15, 30));
    ad.setTitle("testTitle");
    ad.setContent("testContent");
    ad.setPrice(100);

    AdResponse expectedResponse = new AdResponse();
    expectedResponse.setId(1L);
    expectedResponse.setSellerId(2L);
    expectedResponse.setPostedAt(LocalDateTime.of(2022, 1, 17, 15, 30));
    expectedResponse.setTitle("testTitle");
    expectedResponse.setContent("testContent");
    expectedResponse.setPrice(100);

    AdResponse actualResponse = mapper.mapToAdResponse(ad);

    assertEquals(expectedResponse.getId(), actualResponse.getId());
    assertEquals(expectedResponse.getSellerId(), actualResponse.getSellerId());
    assertEquals(expectedResponse.getPostedAt(), actualResponse.getPostedAt());
    assertEquals(expectedResponse.getTitle(), actualResponse.getTitle());
    assertEquals(expectedResponse.getContent(), actualResponse.getContent());
    assertEquals(expectedResponse.getPrice(), actualResponse.getPrice());
  }

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
