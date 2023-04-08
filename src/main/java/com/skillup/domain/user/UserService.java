package com.skillup.domain.user;

// this is where we connect to database, take userDomain in, and

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service // create a Spring object to containers
public class UserService {
    @Autowired
    UserRepository userRepository;
    public UserDomain createUser(UserDomain userDomain) throws Exception {
        // insert to database

        userRepository.createUser(userDomain);
        // return the result
        return userDomain;
    }

    public UserDomain readAccountById(String accountId){
        // return userDomain or null
        return userRepository.readUserById(accountId);
    }

    public UserDomain readAccountByName(String userName){
        // return userDomain or null
        return userRepository.readUserByName(userName);
    }
}
