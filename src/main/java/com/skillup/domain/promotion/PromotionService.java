package com.skillup.domain.promotion;

// this is where we connect to database, take userDomain in, and


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service // create a Spring object to containers
public class PromotionService {
    @Autowired
    PromotionRepository promotionRepository;

    public PromotionDomain createPromotion(PromotionDomain promotionDomain){
        promotionRepository.createPromotion(promotionDomain);
        return promotionDomain;
    }

    public PromotionDomain getPromotionById(String id) {
        return promotionRepository.getPromotionById(id);
    }
}
