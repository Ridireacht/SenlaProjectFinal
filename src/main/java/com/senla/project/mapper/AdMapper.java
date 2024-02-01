package com.senla.project.mapper;

import com.senla.project.dto.request.AdRequest;
import com.senla.project.dto.response.AdClosedResponse;
import com.senla.project.dto.response.AdCurrentResponse;
import com.senla.project.dto.response.AdFullOpenResponse;
import com.senla.project.dto.response.AdPurchasedResponse;
import com.senla.project.dto.response.AdOpenResponse;
import com.senla.project.entity.Ad;
import com.senla.project.repository.RatingRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class AdMapper {

  @Autowired
  protected RatingRepository ratingRepository;


  @Mapping(source = "seller.id", target = "sellerId")
  @Mapping(source = "seller.address", target = "sellerCity")
  public abstract AdOpenResponse mapToAdOpenResponse(Ad ad);

  @Mapping(source = "seller.id", target = "sellerId")
  @Mapping(source = "seller.address", target = "sellerCity")
  @Mapping(target = "sellerRating", expression = "java(ratingRepository.findByUserId(seller.getId()).get().getAverageScore())")
  public abstract AdFullOpenResponse mapToAdFullOpenResponse(Ad ad);

  public abstract AdCurrentResponse mapToAdCurrentResponse(Ad ad);

  @Mapping(source = "buyer.id", target = "buyerId")
  @Mapping(source = "score.score", target = "score")
  public abstract AdClosedResponse mapToAdClosedResponse(Ad ad);

  @Mapping(source = "score.score", target = "score")
  public abstract AdPurchasedResponse mapToAdPurchasedResponse(Ad ad);

  public abstract Ad mapToAd(AdRequest adRequest);
}
