package com.skillup.domain.commodity;

// this is where we connect to database, take commodityDomain in, and

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service // create a Spring object to containers
public class CommodityService {
    @Autowired
    CommodityRepository commodityRepository;
    public CommodityDomain createCommodity(CommodityDomain commodityDomain) throws Exception {
        // insert to database

        commodityRepository.createCommodity(commodityDomain);
        // return the result
        return commodityDomain;
    }

    public CommodityDomain readCommodityById(String id){
        // return commodityDomain or null
        return commodityRepository.getCommodityById(id);
    }

}
