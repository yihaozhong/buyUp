package com.skillup.application.order;

import com.skillup.domain.order.OrderDomain;
import com.skillup.domain.order.OrderService;
import com.skillup.domain.order.util.OrderStatus;
import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.promotion.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class OrderApplication {

    @Autowired
    OrderService orderService;

    @Autowired
    PromotionService promotionService;
    public OrderDomain createByNowOrder(OrderDomain domain) {
        // 1. check promotion
        PromotionDomain promotionDomain = promotionService.getPromotionById(domain.getPromotionId());
        if (Objects.isNull(promotionDomain)){
            domain.setOrderStatus(OrderStatus.ITEM_ERROR);
            return domain;
        }
        // 2. lock promotion
        boolean isLocked = promotionService.lockStock(domain.getPromotionId());
        if (!isLocked){
            domain.setOrderStatus(OrderStatus.OUT_OF_STOCK);
            return domain;
        }
        // 3. order detail (creatTime...)
        domain.setCreateTime(LocalDateTime.now());
        domain.setOrderStatus(OrderStatus.READY);


        // 4. save to data base
        orderService.createOrder(domain);

        return domain;
    }
}
