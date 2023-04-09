package com.skillup.domain.promotion;

// connecting to the database, call by JooqUserRepo,
public interface PromotionRepository {
    void createPromotion(PromotionDomain promotionDomain);

    PromotionDomain getPromotionById(String id);
}
