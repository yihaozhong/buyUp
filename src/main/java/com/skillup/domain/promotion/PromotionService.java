package com.skillup.domain.promotion;

// this is where we connect to database, take userDomain in, and


import lombok.extern.slf4j.Slf4j;
import org.jooq.True;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // create a Spring object to containers
@Slf4j
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

    public List<PromotionDomain> getPromotionByStatus(Integer status) {
        return promotionRepository.getPromotionByStatus(status);
    }

    public boolean lockStock(String id){
        synchronized (this){
            // 1 get available stock, check >= 1
            PromotionDomain currentPromotionDomain = promotionRepository.getPromotionById(id);
            if (currentPromotionDomain.getAvailableStock() <= 0){
                return false;
            }
            log.info("----- Current available stock is {} -----", currentPromotionDomain.getAvailableStock());
            // 2 available stock - 1, set lock stock + 1
            currentPromotionDomain.setAvailableStock(currentPromotionDomain.getAvailableStock() - 1);
            currentPromotionDomain.setLockStock(currentPromotionDomain.getLockStock() + 1);
            log.info("----- Updated current available stock is {}, lock stock is {} -----"
                    , currentPromotionDomain.getAvailableStock(), currentPromotionDomain.getLockStock());
            // 3 save domain
            promotionRepository.updatePromotion(currentPromotionDomain);
            return true;

        }


    }
}
