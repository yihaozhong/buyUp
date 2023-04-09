package com.skillup.infrastructure.repoImpl;

import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.promotion.PromotionRepository;
import com.skillup.infrastructure.jooq.tables.Promotion;
import com.skillup.infrastructure.jooq.tables.records.PromotionRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JooqPromotionRepo implements PromotionRepository {

    @Autowired
    DSLContext dslContext;

    public static final Promotion P_T = new Promotion();
    @Override
    public void createPromotion(PromotionDomain domain){
        dslContext.executeInsert(toRecord(domain));
    }

    @Override
    public PromotionDomain getPromotionById(String id) {
        return dslContext.selectFrom(P_T).where(P_T.PROMOTION_ID.eq(id)).fetchOptional(this:: toDomain).orElse(null);
    }

    @Override
    public List<PromotionDomain> getPromotionByStatus(Integer status) {
        return dslContext.selectFrom(P_T).where(P_T.STATUS.eq(status)).fetch(this:: toDomain);
    }

//    @Override
//    public boolean lockStock(String id){
//        // database level: lockStock = get value, set value - 1
//        return false;
//    }

    @Override
    public void updatePromotion(PromotionDomain promotionDomain) {
        dslContext.executeUpdate(toRecord(promotionDomain));
    }


    private PromotionRecord toRecord(PromotionDomain domain){
        PromotionRecord promotionRecord = new PromotionRecord();
        promotionRecord.setPromotionId(domain.getPromotionId());
        promotionRecord.setPromotionName(domain.getPromotionName());
        promotionRecord.setCommodityId(domain.getCommodityId());
        promotionRecord.setOriginalPrice(domain.getOriginalPrice());
        promotionRecord.setPromotionPrice(domain.getPromotionalPrice());
        promotionRecord.setStartTime(domain.getStartTime());
        promotionRecord.setEndTime(domain.getEndTime());
        promotionRecord.setStatus(domain.getStatus());
        promotionRecord.setTotalStock(domain.getTotalStock());
        promotionRecord.setAvailableStock(domain.getAvailableStock());
        promotionRecord.setLockStock(domain.getLockStock());
        promotionRecord.setImageUrl(domain.getImageUrl());
        return promotionRecord;
    }

    private PromotionDomain toDomain(PromotionRecord record) {
        return PromotionDomain.builder()
                .promotionId(record.getPromotionId())
                .promotionName(record.getPromotionName())
                .commodityId(record.getCommodityId())
                .originalPrice(record.getOriginalPrice())
                .promotionalPrice(record.getPromotionPrice())
                .status(record.getStatus())
                .startTime(record.getStartTime())
                .endTime(record.getEndTime())
                .totalStock(record.getTotalStock())
                .availableStock(record.getAvailableStock())
                .lockStock(record.getLockStock())
                .imageUrl(record.getImageUrl())
                .build();
    }

}
