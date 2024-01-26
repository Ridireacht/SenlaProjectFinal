package com.senla.project.service.impl;

import com.senla.project.dto.response.AdClosedResponse;
import com.senla.project.dto.response.AdCurrentResponse;
import com.senla.project.dto.response.AdPurchasedResponse;
import com.senla.project.dto.request.AdRequest;
import com.senla.project.dto.response.AdResponse;
import com.senla.project.entity.Ad;
import com.senla.project.entity.User;
import com.senla.project.mapper.AdMapper;
import com.senla.project.repository.AdRepository;
import com.senla.project.repository.UserRepository;
import com.senla.project.service.AdService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdServiceImpl implements AdService {

  private final AdRepository adRepository;
  private final UserRepository userRepository;

  private final AdMapper adMapper;


  @Override
  public List<AdResponse> getAllAds() {
    List<Ad> ads = adRepository.findAll();
    return ads.stream()
        .map(adMapper::mapToAdResponse)
        .collect(Collectors.toList());
  }

  @Override
  public List<AdCurrentResponse> getCurrentAdsByUserId(Long userId) {
    List<Ad> currentAds = adRepository.findBySellerIdAndIsClosedFalse(userId);
    return currentAds.stream()
        .map(adMapper::mapToAdCurrentResponse)
        .collect(Collectors.toList());
  }

  @Override
  public List<AdClosedResponse> getClosedAdsByUserId(Long userId) {
    List<Ad> closedAds = adRepository.findBySellerIdAndIsClosedTrue(userId);
    return closedAds.stream()
        .map(adMapper::mapToAdClosedResponse)
        .collect(Collectors.toList());
  }

  @Override
  public List<AdPurchasedResponse> getPurchasedAdsByUserId(Long userId) {
    List<Ad> purchasedAds = adRepository.findByBuyerId(userId);
    return purchasedAds.stream()
        .map(adMapper::mapToAdPurchasedResponse)
        .collect(Collectors.toList());
  }

  @Override
  public AdResponse getAdById(Long adId) {
    Ad ad = adRepository.findById(adId).get();
    return adMapper.mapToAdResponse(ad);
  }

  @Override
  public AdResponse createAd(Long userId, AdRequest adRequest) {
    User seller = userRepository.findById(userId).get();

    Ad ad = adMapper.mapToAd(adRequest);
    ad.setSeller(seller);
    ad.setPostedAt(LocalDateTime.now());

    Ad savedAd = adRepository.save(ad);
    return adMapper.mapToAdResponse(savedAd);
  }

  @Override
  public AdResponse updateAd(Long adId, AdRequest adRequest) {
    Ad existingAd = adRepository.findById(adId).orElse(null);
    if (existingAd != null) {
      existingAd.setTitle(adRequest.getTitle());
      existingAd.setContent(adRequest.getContent());
      existingAd.setPrice(adRequest.getPrice());
      Ad updatedAd = adRepository.save(existingAd);
      return adMapper.mapToAdResponse(updatedAd);
    }
    return null;
  }

  @Override
  public AdResponse makeAdPremium(Long adId) {
    Ad ad = adRepository.findById(adId).get();

    ad.setPremium(true);
    adRepository.save(ad);

    return adMapper.mapToAdResponse(ad);
  }

  @Override
  public boolean deleteAd(Long adId) {
    if (adRepository.existsById(adId)) {
      adRepository.deleteById(adId);
      return true;
    }

    return false;
  }

  @Override
  public boolean doesAdExist(Long adId) {
    return adRepository.existsById(adId);
  }

  @Override
  public boolean isAdAvailableForUser(Long adId, Long currentUserId) {
    Ad ad = adRepository.findById(adId).get();
    return ad.getSeller().getId().equals(currentUserId) || ad.getBuyer().getId().equals(currentUserId);
  }

  @Override
  public boolean doesAdBelongToUser(Long adId, Long currentUserId) {
    Ad ad = adRepository.findById(adId).get();
    return ad.getSeller().getId().equals(currentUserId);
  }

  @Override
  public boolean didUserBoughtAd(Long adId, Long currentUserId) {
    Ad ad = adRepository.findById(adId).get();
    return ad.getBuyer().getId().equals(currentUserId);
  }

}
