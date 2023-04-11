package com.skillup.domain.promotion;

// this is where we connect to database, take userDomain in, and


import com.skillup.domain.promotion.stockStrategy.StockOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service // create a Spring object to containers
@Slf4j
public class PromotionService {
    @Autowired
    PromotionRepository promotionRepository;

    @Resource(name = "${promotion.stock-strategy}")
    StockOperation stockOperation;

    public PromotionDomain createPromotion(PromotionDomain promotionDomain){
        promotionRepository.createPromotion(promotionDomain);
        return promotionDomain;
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public PromotionDomain getPromotionById(String id) {
        return promotionRepository.getPromotionById(id);
    }

    public List<PromotionDomain> getPromotionByStatus(Integer status) {
        return promotionRepository.getPromotionByStatus(status);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean lockStock(String id){
        return stockOperation.lockStock(id);
    }

    public boolean revertStock(String id){
        return stockOperation.revertStock(id);
    }

    public boolean deductStock(String id){
        return stockOperation.deductStock(id);
    }
}
