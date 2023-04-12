package com.skillup.domain.user;

// this is where we connect to database, take userDomain in, and

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
      上层-API层（定义接口/规则），中层-domain，下层-infrastructure（实现具体功能，需要随时能换）
      "我使用了你->我依赖于你"，所以infrastructure依赖domain，完成了依赖反转，这与文件的放置位置也有关系。而这里反转的意义在于上层代码不依赖于
      下层的实现，只是提供接口让下层实现。比如当我们在infrastructure层工作的时候，没有办法改动domain的代码，domain就相当于源码，我们只能实现来
      自源码包的接口，无法改动。
     */

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

    public UserDomain updateUser(UserDomain userDomain){
        // return userDomain or null
        userRepository.updateUser(userDomain);
        return userDomain;
    }
}
