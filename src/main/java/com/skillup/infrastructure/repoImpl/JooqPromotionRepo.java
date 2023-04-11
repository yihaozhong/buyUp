package com.skillup.infrastructure.repoImpl;

import com.skillup.domain.promotion.PromotionDomain;
import com.skillup.domain.promotion.PromotionRepository;
import com.skillup.domain.promotion.stockStrategy.StockOperation;
import com.skillup.infrastructure.jooq.tables.Promotion;
import com.skillup.infrastructure.jooq.tables.records.PromotionRecord;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
// @Repository的作用和@Service一样，都是生成一个object归spring管理，但是字面上能明确分工。
// 比如repository就是连接数据库的，service和功能有关
@Repository(value = "optimistic")
@Slf4j
public class JooqPromotionRepo implements PromotionRepository, StockOperation {
    // JooqPromotionRepo这里实现了两个接口，绑定到同一个对象上，而不是两个对象。
    @Autowired
    DSLContext dslContext;
    // 这个常量的意思是Once the class loads, this constant variable is generated. 它属于singleton，无论连接什么table，
    // 都要通过这个对象，它也不会反复生成，是最简单的一种singleton。
    public static final Promotion P_T = new Promotion();
    @Override
    public void createPromotion(PromotionDomain domain){
        dslContext.executeInsert(toRecord(domain));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED) // 事务的传播到这个getById()终止，因为方法中封装了JDBC的操作
    public PromotionDomain getPromotionById(String id) {
        return dslContext.selectFrom(P_T).where(P_T.PROMOTION_ID.eq(id)).fetchOptional(this:: toDomain).orElse(null);
    }

    @Override
    public List<PromotionDomain> getPromotionByStatus(Integer status) {
        // 这里因为找的是一个list，就不需要再用orElse()了，如果找不到，那就返回空。
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

    // 1 这里的method是把update和查找融合到一起
    // 2 读就是当前读，即使别的ID退回去，也不影响我这个ID读的结果，没有因为select死掉。然后因为set带了写锁，所以第一个ID没有执行完，
    // 别的ID走到了这条语句也没法执行。
    // 3 set数据是在数据库的层面操作，就只执行加一减一的那条语句，甚至连数到底是多少也不用关心，因为没有取出来。如果要取数据，程序上就有
    // 并发。
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean lockStock(String id) {

        /**
         * update promotion
         * set available_stock = available_stock - 1, lock_stock = lock_stock + 1
         * where id = promotion_id and available_stock > 0
         */

        log.info("----- Optimistic Strategy Lock Stock-----");
        int isLocked = dslContext.update(P_T)
                .set(P_T.AVAILABLE_STOCK, P_T.AVAILABLE_STOCK.subtract(1))
                .set(P_T.LOCK_STOCK, P_T.LOCK_STOCK.add(1))
                .where(P_T.PROMOTION_ID.eq(id).and(P_T.AVAILABLE_STOCK.greaterThan(0L)))
                .execute();
        return isLocked == 1;

    }

    @Override
    public boolean revertStock(String id) {
        /**
         * update promotion
         * set available_stock = available_stock + 1, lock_stock = lock_stock - 1
         * where id = promotion_id and lock_stock > 0
         */

        log.info("----- Optimistic Strategy Revert-----");
        int isReversed = dslContext.update(P_T)
                .set(P_T.AVAILABLE_STOCK, P_T.AVAILABLE_STOCK.add(1))
                .set(P_T.LOCK_STOCK, P_T.LOCK_STOCK.subtract(1))
                .where(P_T.PROMOTION_ID.eq(id).and(P_T.LOCK_STOCK.greaterThan(0L)))
                .execute();
        return isReversed == 1;
    }

    @Override
    public boolean deductStock(String id) {
        /**
         * update promotion
         * set lock_stock = lock_stock - 1
         * where id = promotion_id and lock_stock > 0
         */

        log.info("----- Optimistic Strategy Deduct Stock-----");
        int isDeducted = dslContext.update(P_T)
                .set(P_T.LOCK_STOCK, P_T.LOCK_STOCK.subtract(1))
                .where(P_T.PROMOTION_ID.eq(id).and(P_T.AVAILABLE_STOCK.greaterThan(0L)))
                .execute();
        return isDeducted == 1;
    }
}
