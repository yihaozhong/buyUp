package com.skillup.apiPresentation.dto;

import com.skillup.apiPresentation.dto.in.UserInDto;
import com.skillup.apiPresentation.dto.out.UserOutDto;
import com.skillup.domain.user.UserDomain;
import com.skillup.domain.user.UserService;
import com.skillup.infrastructure.jooq.tables.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping("/account")
    public UserOutDto createUser(@RequestBody UserInDto userInDto){
        // insert data into data table

        UserDomain userDomain = userService.createUser(toDomain(userInDto));

        return toOutDto(userDomain);
    }

    private UserDomain toDomain(UserInDto userInDto){
        return UserDomain.builder()
                .userId(UUID.randomUUID().toString())
                .userName(userInDto.getUserName())
                .password(userInDto.getPassword())
                .build();
    }

    private UserOutDto toOutDto(UserDomain userDomain){
        return UserOutDto.builder()
                .userName(userDomain.getUserName())
                .userId(userDomain.getUserId())
                .build();
    }
}
