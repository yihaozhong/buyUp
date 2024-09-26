package com.skillup.domain.promotionCache;

/*
这个domain层不关心是谁做的cache，与基础设施层做了隔离
 */
public interface PromotionCacheRepo {
    // 根据promotion id在redis里或者缓存里拿到cacheDomain
    PromotionCacheDomain getPromotionById(String id);
    // 或者把cacheDomain保存到中间件里
    void setPromotion(PromotionCacheDomain cacheDomain);
}
