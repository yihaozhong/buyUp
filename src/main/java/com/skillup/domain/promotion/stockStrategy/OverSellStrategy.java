package com.skillup.domain.promotion.stockStrategy;

import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.promotion.PromotionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// 这个方法的策略是：容忍超售
// 不好的方法，因为读写操作没有放到一起。所有的人都能同时读和写会造成很严重的问题，比如10个人同时读到100的数据，想减一变成99，这
// 就有可能出现10个100变成99，和实际数据不一致。

@Service(value = "oversell")
@Slf4j
public class OverSellStrategy implements StockOperation{
    @Autowired
    PromotionRepository promotionRepository;
    @Override
    public boolean lockStock(String id){
            log.info("----- OverSell Strategy -----");
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

    @Override
    public boolean revertStock(String id) {
        return false;
    }

    @Override
    public boolean deductStock(String id) {
        return false;
    }
}
