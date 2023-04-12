package com.skillup.domain.user;

import com.skillup.infrastructure.jooq.tables.User;

// connecting to the database, call by JooqUserRepo,
public interface UserRepository {
    void createUser(UserDomain userDomain);

    UserDomain readUserById(String id);

    UserDomain readUserByName(String name);

    void updateUser(UserDomain userDomain);
}
