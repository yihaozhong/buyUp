package com.skillup.application.order;

import com.skillup.domain.order.OrderDomain;
import com.skillup.domain.order.OrderService;
import com.skillup.domain.order.util.OrderStatus;
import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.promotion.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
/**
 * 增加application层，让domain层更加纯粹，因为domain已经包装好了所有的order status，所以增加这一层可以方便往上传，而且这些业务逻辑可
 * 以封装起来。（这个application层是domain的下层）
 *
 */
@Service
public class OrderApplication {

    @Autowired
    OrderService orderService;

    @Autowired
    PromotionService promotionService;

    // 1.在加了@transactional注解的地方会开启一个事务，当层层调用的函数闭合，返回最上层的函数时，会加上一个commit。在这个过程中，如果有错
    // 误，会自动调，否则commit。
    // 2.当开启一个事务后，进入这个方法时会先写一个begin，每当碰到数据库的操作，就加到begin后面。比如进入下面的方法后，先遇到getById的操作，
    // 但是因为这个注解覆盖的范围只限于跟在它后面的这个方法createBuyNowOrder()，所以进入到getById()后，需要事务的传播机制来处理，直至抵达
    // 数据库那一层。
    @Transactional
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


        // 4. save to data base         // 保存到本地数据库这一步要往service放
        orderService.createOrder(domain);

        return domain;
    }
}
