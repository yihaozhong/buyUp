package com.skillup.domain.user;

// connect to database

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
}
