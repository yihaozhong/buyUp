package com.skillup.infrastructure.repoImpl;

import com.skillup.domain.user.UserDomain;
import com.skillup.domain.user.UserRepository;
import com.skillup.infrastructure.jooq.tables.records.UserRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository // create a Spring object to containers
public class JooqUserRepo implements UserRepository {

    @Autowired
    DSLContext dslContext;

    // public static final User USER_T = new User();

    @Override
    public void createUser(UserDomain userDomain) {
        try {
            dslContext.executeInsert(toRecord(userDomain));
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw e;
        }
    }

    private UserRecord toRecord(UserDomain userDomain){
        UserRecord userRecord = new UserRecord();

        userRecord.setUserId(userDomain.getUserId());
        userRecord.setUserName(userDomain.getUserName());
        userRecord.setPassword(userDomain.getPassword());

        return userRecord;
    }
}
