package com.senla.project.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.senla.project.dto.request.AdRequest;
import com.senla.project.dto.response.AdClosedResponse;
import com.senla.project.dto.response.AdCurrentResponse;
import com.senla.project.dto.response.AdFullOpenResponse;
import com.senla.project.dto.response.AdOpenResponse;
import com.senla.project.dto.response.AdPurchasedResponse;
import com.senla.project.entity.Ad;
import com.senla.project.entity.Rating;
import com.senla.project.entity.Score;
import com.senla.project.entity.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = { AdMapper.class, AdMapperImpl.class })
public class AdMapperTest {

  @Autowired
  private AdMapper adMapper;


  @Test
  public void testMapToAdOpenResponse() {
    Ad ad = createAd();
    AdOpenResponse adOpenResponse = adMapper.mapToAdOpenResponse(ad);

    assertEquals(ad.getId(), adOpenResponse.getId());
    assertEquals(ad.getSeller().getId(), adOpenResponse.getSellerId());
    assertEquals(ad.getSeller().getAddress(), adOpenResponse.getSellerCity());
    assertEquals(ad.getPostedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")), adOpenResponse.getPostedAt());
    assertEquals(ad.getTitle(), adOpenResponse.getTitle());
    assertEquals(ad.getContent(), adOpenResponse.getContent());
    assertEquals(ad.getPrice(), adOpenResponse.getPrice());
  }

  @Test
  public void testMapToAdFullOpenResponse() {
    Ad ad = createAd();
    AdFullOpenResponse adFullOpenResponse = adMapper.mapToAdFullOpenResponse(ad);

    assertEquals(ad.getId(), adFullOpenResponse.getId());
    assertEquals(ad.getSeller().getId(), adFullOpenResponse.getSellerId());
    assertEquals(ad.getSeller().getAddress(), adFullOpenResponse.getSellerCity());
    assertEquals(ad.getPostedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")), adFullOpenResponse.getPostedAt());
    assertEquals(ad.getTitle(), adFullOpenResponse.getTitle());
    assertEquals(ad.getContent(), adFullOpenResponse.getContent());
    assertEquals(ad.getPrice(), adFullOpenResponse.getPrice());
    assertEquals(ad.getSeller().getRating().getAverageScore(), adFullOpenResponse.getSellerRating());
  }

  @Test
  public void testMapToAdCurrentResponse() {
    Ad ad = createAd();
    AdCurrentResponse adCurrentResponse = adMapper.mapToAdCurrentResponse(ad);

    assertEquals(ad.getId(), adCurrentResponse.getId());
    assertEquals(ad.getPostedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")), adCurrentResponse.getPostedAt());
    assertEquals(ad.getTitle(), adCurrentResponse.getTitle());
    assertEquals(ad.getContent(), adCurrentResponse.getContent());
    assertEquals(ad.getPrice(), adCurrentResponse.getPrice());
    assertEquals(ad.isPremium(), adCurrentResponse.isPremium());
  }

  @Test
  public void testMapToAdClosedResponse() {
    Ad ad = createAd();
    AdClosedResponse adClosedResponse = adMapper.mapToAdClosedResponse(ad);

    assertEquals(ad.getId(), adClosedResponse.getId());
    assertEquals(ad.getPostedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")), adClosedResponse.getPostedAt());
    assertEquals(ad.getTitle(), adClosedResponse.getTitle());
    assertEquals(ad.getContent(), adClosedResponse.getContent());
    assertEquals(ad.getPrice(), adClosedResponse.getPrice());
    assertEquals(ad.getBuyer().getId(), adClosedResponse.getBuyerId());
    assertEquals(ad.getScore().getValue(), adClosedResponse.getScore());
  }

  @Test
  public void testMapToAdPurchasedResponse() {
    Ad ad = createAd();
    AdPurchasedResponse adPurchasedResponse = adMapper.mapToAdPurchasedResponse(ad);

    assertEquals(ad.getId(), adPurchasedResponse.getId());
    assertEquals(ad.getPostedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")), adPurchasedResponse.getPostedAt());
    assertEquals(ad.getTitle(), adPurchasedResponse.getTitle());
    assertEquals(ad.getContent(), adPurchasedResponse.getContent());
    assertEquals(ad.getPrice(), adPurchasedResponse.getPrice());
    assertEquals(ad.getScore().getValue(), adPurchasedResponse.getScore());
  }

  @Test
  public void testMapToAd() {
    AdRequest adRequest = createAdRequest();
    Ad ad = adMapper.mapToAd(adRequest);

    assertEquals(adRequest.getTitle(), ad.getTitle());
    assertEquals(adRequest.getContent(), ad.getContent());
    assertEquals(adRequest.getPrice(), ad.getPrice());
  }

  private Ad createAd() {
    Ad ad = new Ad();
    ad.setId(1L);
    ad.setPostedAt(LocalDateTime.now());
    ad.setTitle("Test Title");
    ad.setContent("Test Content");
    ad.setPrice(100);
    ad.setPremium(true);

    User seller = new User();
    seller.setId(2L);
    seller.setAddress("Test City");

    ad.setSeller(seller);

    Rating rating = new Rating();
    rating.setAverageScore(4.5);
    seller.setRating(rating);

    User buyer = new User();
    buyer.setId(3L);

    ad.setBuyer(buyer);

    Score score = new Score();
    score.setValue(4);
    ad.setScore(score);

    return ad;
  }

  private AdRequest createAdRequest() {
    AdRequest adRequest = new AdRequest();
    adRequest.setTitle("Test Title");
    adRequest.setContent("Test Content");
    adRequest.setPrice(100);
    return adRequest;
  }
}
