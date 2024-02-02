package com.senla.project.mapper;

import com.senla.project.dto.request.AdRequest;
import com.senla.project.dto.response.AdClosedResponse;
import com.senla.project.dto.response.AdCurrentResponse;
import com.senla.project.dto.response.AdFullOpenResponse;
import com.senla.project.dto.response.AdPurchasedResponse;
import com.senla.project.dto.response.AdOpenResponse;
import com.senla.project.entity.Ad;
import com.senla.project.repository.RatingRepository;
import java.time.format.DateTimeFormatter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", imports = DateTimeFormatter.class)
public abstract class AdMapper {

  @Autowired
  protected RatingRepository ratingRepository;


  @Mapping(source = "seller.id", target = "sellerId")
  @Mapping(source = "seller.address", target = "sellerCity")
  @Mapping(target = "postedAt", expression = "java(ad.getPostedAt().format(DateTimeFormatter.ofPattern(\"dd-MM-yyyy HH:mm\")))")
  public abstract AdOpenResponse mapToAdOpenResponse(Ad ad);

  @Mapping(source = "seller.id", target = "sellerId")
  @Mapping(source = "seller.address", target = "sellerCity")
  @Mapping(target = "sellerRating", expression = "java(ratingRepository.findByUserId(ad.getSeller().getId()).get().getAverageScore())")
  @Mapping(target = "postedAt", expression = "java(ad.getPostedAt().format(DateTimeFormatter.ofPattern(\"dd-MM-yyyy HH:mm\")))")
  public abstract AdFullOpenResponse mapToAdFullOpenResponse(Ad ad);

  @Mapping(target = "postedAt", expression = "java(ad.getPostedAt().format(DateTimeFormatter.ofPattern(\"dd-MM-yyyy HH:mm\")))")
  public abstract AdCurrentResponse mapToAdCurrentResponse(Ad ad);

  @Mapping(source = "buyer.id", target = "buyerId")
  @Mapping(source = "score.score", target = "score")
  @Mapping(target = "postedAt", expression = "java(ad.getPostedAt().format(DateTimeFormatter.ofPattern(\"dd-MM-yyyy HH:mm\")))")
  public abstract AdClosedResponse mapToAdClosedResponse(Ad ad);

  @Mapping(source = "score.score", target = "score")
  @Mapping(target = "postedAt", expression = "java(ad.getPostedAt().format(DateTimeFormatter.ofPattern(\"dd-MM-yyyy HH:mm\")))")
  public abstract AdPurchasedResponse mapToAdPurchasedResponse(Ad ad);

  public abstract Ad mapToAd(AdRequest adRequest);
}
