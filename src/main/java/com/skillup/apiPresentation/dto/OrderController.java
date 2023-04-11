package com.skillup.apiPresentation.dto;

import com.skillup.apiPresentation.dto.in.OrderInDto;
import com.skillup.apiPresentation.dto.out.OrderOutDto;
import com.skillup.apiPresentation.util.SkillUpCommon;
import com.skillup.apiPresentation.util.SnowFlake;
import com.skillup.application.order.OrderApplication;
import com.skillup.domain.order.OrderDomain;
import com.skillup.domain.order.OrderService;
import com.skillup.domain.order.util.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/order")
public class OrderController {


    @Autowired
    SnowFlake snowFlake;

    @Autowired
    OrderApplication orderApplication;

    @Autowired
    OrderService orderService;

    // ResponseEntity<>用来做错误控制
    // createBuyNowOrder是通过抢购直接下单，不走cart，直接把银行卡地址填好，后续再修改就行了。而createOrder则需要通过cart。
    // 没必要放下一层的才放到这里，因为最终要return到API层
    // validate input这一步可以提前做校验，然后拦截，在前端表示出来
    @PostMapping
    public ResponseEntity<OrderOutDto> createByNowOrder(@Valid @RequestBody OrderInDto orderInDto) {
        OrderDomain orderDomain = orderApplication.createByNowOrder(toDomain(orderInDto));
        return ResponseEntity.status(SkillUpCommon.SUCCESS).body(toOrderOutDto(orderDomain));
    }
    // 这里直接找domain层，不需要经过application层
    @GetMapping("/id/{id}")
    public ResponseEntity<OrderOutDto> getOrderById(@PathVariable Long id) {
        OrderDomain orderDomain = orderService.getOrderById(id);
        if (Objects.isNull(orderDomain)) {
            return ResponseEntity.status(SkillUpCommon.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(SkillUpCommon.SUCCESS).body(toOrderOutDto(orderDomain));
    }

    private OrderDomain toDomain(OrderInDto orderInDto) {
        return OrderDomain.builder()
                .orderNumber(snowFlake.nextId())
                .userId(orderInDto.getUserId())
                .promotionId(orderInDto.getPromotionId())
                .promotionName(orderInDto.getPromotionName())
                .orderAmount(orderInDto.getOrderAmount())
                .orderStatus(OrderStatus.READY)
                .build();
    }

    private OrderOutDto toOrderOutDto(OrderDomain orderDomain) {
        return OrderOutDto.builder()
                .orderNumber(orderDomain.getOrderNumber().toString())
                .userId(orderDomain.getUserId())
                .promotionId(orderDomain.getPromotionId())
                .promotionName(orderDomain.getPromotionName())
                .orderAmount(orderDomain.getOrderAmount())
                .orderStatus(orderDomain.getOrderStatus().code)
                .createTime(orderDomain.getCreateTime())
                .payTime(orderDomain.getPayTime())
                .build();
    }
}
