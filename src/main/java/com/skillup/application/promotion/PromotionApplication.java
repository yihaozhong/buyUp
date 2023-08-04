package com.skillup.application.promotion;

import com.skillup.application.mapper.PromotionMapper;
import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.promotion.PromotionService;
import com.skillup.domain.promotionCache.PromotionCacheDomain;
import com.skillup.domain.promotionCache.PromotionCacheService;
import com.skillup.domain.stock.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PromotionApplication {
    @Autowired
    StockService stockService;
    @Autowired
    PromotionCacheService promotionCacheService;
    @Autowired
    PromotionService promotionService;

    public PromotionDomain getById(String id){
        // CACHE ASIDE STRATEGY

        //1. try to hit cache
        PromotionCacheDomain cacheDomain = promotionCacheService.getPromotionById(id);
        //2. not hit, read data base
        if(Objects.isNull(cacheDomain)){
            PromotionDomain promotionDomain = promotionService.getPromotionById(id);
            if(Objects.isNull(promotionDomain)){
                return null;
            }
            //3. insert cache
            promotionCacheService.setPromotion(PromotionMapper.INSTANCE.toCacheDomain(promotionDomain));
        }

        // 4. get stock cache
        Long availableStock = stockService.getAvailableStock(id);
        if (Objects.isNull(availableStock)){
            return null;
        }
        // 5. update stock onto available stock
        cacheDomain.setAvailableStock(availableStock);
        return PromotionMapper.INSTANCE.toDomain(cacheDomain);
    }

}


