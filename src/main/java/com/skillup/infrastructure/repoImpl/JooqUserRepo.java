package com.skillup.infrastructure.repoImpl;

import com.skillup.domain.user.UserDomain;
import com.skillup.domain.user.UserRepository;
import com.skillup.infrastructure.jooq.tables.User;
import com.skillup.infrastructure.jooq.tables.records.UserRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
// @Repository的作用和@Service一样，都是生成一个object归spring管理，但是字面上能明确分工。
// 比如repository就是连接数据库的，service和功能有关

@Repository// create a Spring object to containers
public class JooqUserRepo implements UserRepository {

    @Autowired
    DSLContext dslContext;

    public static final User USER_T = new User();
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

    @Override
    public UserDomain readUserById(String id) {
        // fetchOptional是为了包装类别，减少判空逻辑
        // 这里用lambda函数来调用前面生成好的函数的结果，然后套到另一个方法里，再直接调用这个方法
        Optional<UserDomain> userRecordOptional = dslContext.selectFrom(USER_T).where(USER_T.USER_ID.eq(id))
                .fetchOptional(this:: toDomain);
        return userRecordOptional.orElse(null); //if present, .get(), else null
        // orElse()在这是对if else的一个简化处理，如果有值，默认执行get()然后返回，否则返回null
    }

    @Override
    public UserDomain readUserByName(String name) {
        Optional<UserDomain> userRecordOptional = dslContext.selectFrom(USER_T).where(USER_T.USER_NAME.eq(name))
                .fetchOptional(this:: toDomain);
        return userRecordOptional.orElse(null); //if present, .get(), else null
    }

    @Override
    public void updateUser(UserDomain userDomain) {
        try {
            dslContext.executeUpdate(toRecord(userDomain));
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw e;
        }
    }

    private UserRecord toRecord(UserDomain userDomain){
        // 用jooq生成的代码不支持builder，所以这里就只能手动set每一个属性
        UserRecord userRecord = new UserRecord();

        userRecord.setUserId(userDomain.getUserId());
        userRecord.setUserName(userDomain.getUserName());
        userRecord.setPassword(userDomain.getPassword());

        return userRecord;
    }

    public UserDomain toDomain(UserRecord userRecord){
        return UserDomain.builder().userId(userRecord.getUserId())
                .userName(userRecord.getUserName())
                .password(userRecord.getPassword()).build();
    }
}
