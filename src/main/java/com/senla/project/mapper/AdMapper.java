package com.senla.project.mapper;

import com.senla.project.dto.AdClosedResponse;
import com.senla.project.dto.AdCurrentResponse;
import com.senla.project.dto.AdPurchasedResponse;
import com.senla.project.dto.AdRequest;
import com.senla.project.dto.AdResponse;
import com.senla.project.entities.Ad;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdMapper {

  @Mapping(source = "buyer.id", target = "buyerId")
  @Mapping(source = "score.score", target = "score")
  AdClosedResponse mapToAdClosedResponse(Ad ad);

  AdCurrentResponse mapToAdCurrentResponse(Ad ad);

  @Mapping(source = "score.score", target = "score")
  AdPurchasedResponse mapToAdPurchasedResponse(Ad ad);

  @Mapping(source = "seller.id", target = "sellerId")
  AdResponse mapToAdResponse(Ad ad);

  Ad mapToAd(AdRequest adRequest);
}
