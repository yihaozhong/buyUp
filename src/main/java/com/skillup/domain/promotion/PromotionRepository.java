package com.skillup.domain.promotion;

import java.util.List;

// connecting to the database, call by JooqUserRepo,
public interface PromotionRepository {
    void createPromotion(PromotionDomain promotionDomain);

    PromotionDomain getPromotionById(String id);

    List<PromotionDomain> getPromotionByStatus(Integer status);

    void updatePromotion(PromotionDomain promotionDomain);
}
