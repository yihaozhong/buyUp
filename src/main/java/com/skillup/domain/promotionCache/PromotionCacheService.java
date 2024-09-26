package com.skillup.domain.promotionCache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromotionCacheService {
    @Autowired
    PromotionCacheRepo promotionCacheRepo;

    public PromotionCacheDomain getPromotionById(String id) {
        return promotionCacheRepo.getPromotionById(id);
    }
    public PromotionCacheDomain setPromotion(PromotionCacheDomain cacheDomain) {
        promotionCacheRepo.setPromotion(cacheDomain);
        return cacheDomain;
    }
}
