package com.skillup.domain.order.util;

public enum OrderStatus {
    ITEM_ERROR(-2),
    OUT_OF_STOCK(-1),
    READY(0),
    CREATED(1),
    PAYED(2),
    OVERTIME(3)
    ;

    public Integer code;

    OrderStatus(Integer code){
        this.code = code;
    }
}
