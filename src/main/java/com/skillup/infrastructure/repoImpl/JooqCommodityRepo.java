package com.skillup.infrastructure.repoImpl;

import com.skillup.domain.commodity.CommodityDomain;
import com.skillup.domain.commodity.CommodityRepository;

import com.skillup.infrastructure.jooq.tables.Commodity;
import com.skillup.infrastructure.jooq.tables.records.CommodityRecord;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository// create a Spring object to containers
public class JooqCommodityRepo implements CommodityRepository {

    @Autowired
    DSLContext dslContext;

    public static final Commodity COMMODITY_T = new Commodity();
    // public static final Commodity USER_T = new Commodity();

    @Override
    public void createCommodity(CommodityDomain commodityDomain) {
        try {
            dslContext.executeInsert(toRecord(commodityDomain));
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @Override
    public CommodityDomain getCommodityById(String id) {
        Optional<CommodityDomain> commodityRecordOptional = dslContext.selectFrom(COMMODITY_T).where(COMMODITY_T.COMMODITY_ID.eq(id))
                .fetchOptional(this:: toDomain);
        return commodityRecordOptional.orElse(null); //if present, .get(), else null
    }

    private CommodityRecord toRecord(CommodityDomain commodityDomain){
        CommodityRecord commodityRecord = new CommodityRecord();

        commodityRecord.setCommodityId(commodityDomain.getCommodityId());
        commodityRecord.setCommodityName(commodityDomain.getCommodityName());
        commodityRecord.setDescription(commodityDomain.getDescription());
        commodityRecord.setPrice(commodityDomain.getPrice());
        commodityRecord.setImageUrl(commodityDomain.getImageUrl());

        return commodityRecord;
    }

    public CommodityDomain toDomain(CommodityRecord commodityRecord){
        return CommodityDomain.builder().commodityId(commodityRecord.getCommodityId())
                .commodityName(commodityRecord.getCommodityName())
                .description(commodityRecord.getDescription())
                .price(commodityRecord.getPrice())
                .imageUrl(commodityRecord.getImageUrl()).build();
    }

}
