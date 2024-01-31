package com.senla.project.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.senla.project.dto.request.AdRequest;
import com.senla.project.dto.response.AdClosedResponse;
import com.senla.project.dto.response.AdCurrentResponse;
import com.senla.project.dto.response.AdPurchasedResponse;
import com.senla.project.dto.response.AdResponse;
import com.senla.project.entity.Ad;
import com.senla.project.entity.User;
import com.senla.project.entity.UserScore;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AdMapperTest {

  @Autowired
  AdMapper mapper;


  @Test
  public void testMapToAdClosedResponse() {
    User buyer = new User();
    buyer.setId(2L);

    UserScore userScore = new UserScore();
    userScore.setScore(3);

    Ad ad = new Ad();
    ad.setId(1L);
    ad.setPostedAt(LocalDateTime.of(2022, 1, 17, 15, 30));
    ad.setTitle("testTitle");
    ad.setContent("testContent");
    ad.setPrice(100);
    ad.setBuyer(buyer);
    ad.setScore(userScore);

    AdClosedResponse expectedResponse = new AdClosedResponse();
    expectedResponse.setId(1L);
    expectedResponse.setPostedAt(LocalDateTime.of(2022, 1, 17, 15, 30));
    expectedResponse.setTitle("testTitle");
    expectedResponse.setContent("testContent");
    expectedResponse.setPrice(100);
    expectedResponse.setBuyerId(2L);
    expectedResponse.setScore(3);

    AdClosedResponse actualResponse = mapper.mapToAdClosedResponse(ad);

    assertEquals(expectedResponse.getId(), actualResponse.getId());
    assertEquals(expectedResponse.getPostedAt(), actualResponse.getPostedAt());
    assertEquals(expectedResponse.getTitle(), actualResponse.getTitle());
    assertEquals(expectedResponse.getContent(), actualResponse.getContent());
    assertEquals(expectedResponse.getPrice(), actualResponse.getPrice());
    assertEquals(expectedResponse.getBuyerId(), actualResponse.getBuyerId());
    assertEquals(expectedResponse.getScore(), actualResponse.getScore());
  }

  @Test
  public void testMapToAdCurrentResponse() {
    Ad ad = new Ad();
    ad.setId(1L);
    ad.setPostedAt(LocalDateTime.of(2022, 1, 17, 15, 30));
    ad.setTitle("testTitle");
    ad.setContent("testContent");
    ad.setPrice(100);
    ad.setPremium(false);

    AdCurrentResponse expectedResponse = new AdCurrentResponse();
    expectedResponse.setId(1L);
    expectedResponse.setPostedAt(LocalDateTime.of(2022, 1, 17, 15, 30));
    expectedResponse.setTitle("testTitle");
    expectedResponse.setContent("testContent");
    expectedResponse.setPrice(100);
    expectedResponse.setPremium(false);

    AdCurrentResponse actualResponse = mapper.mapToAdCurrentResponse(ad);

    assertEquals(expectedResponse.getId(), actualResponse.getId());
    assertEquals(expectedResponse.getPostedAt(), actualResponse.getPostedAt());
    assertEquals(expectedResponse.getTitle(), actualResponse.getTitle());
    assertEquals(expectedResponse.getContent(), actualResponse.getContent());
    assertEquals(expectedResponse.getPrice(), actualResponse.getPrice());
    assertEquals(expectedResponse.isPremium(), actualResponse.isPremium());
  }

  @Test
  public void testMapToAdPurchasedResponse() {
    UserScore userScore = new UserScore();
    userScore.setScore(3);

    Ad ad = new Ad();
    ad.setId(1L);
    ad.setPostedAt(LocalDateTime.of(2022, 1, 17, 15, 30));
    ad.setTitle("testTitle");
    ad.setContent("testContent");
    ad.setPrice(100);
    ad.setScore(userScore);

    AdPurchasedResponse expectedResponse = new AdPurchasedResponse();
    expectedResponse.setId(1L);
    expectedResponse.setPostedAt(LocalDateTime.of(2022, 1, 17, 15, 30));
    expectedResponse.setTitle("testTitle");
    expectedResponse.setContent("testContent");
    expectedResponse.setPrice(100);
    expectedResponse.setScore(3);

    AdPurchasedResponse actualResponse = mapper.mapToAdPurchasedResponse(ad);

    assertEquals(expectedResponse.getId(), actualResponse.getId());
    assertEquals(expectedResponse.getPostedAt(), actualResponse.getPostedAt());
    assertEquals(expectedResponse.getTitle(), actualResponse.getTitle());
    assertEquals(expectedResponse.getContent(), actualResponse.getContent());
    assertEquals(expectedResponse.getPrice(), actualResponse.getPrice());
    assertEquals(expectedResponse.getScore(), actualResponse.getScore());
  }

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
