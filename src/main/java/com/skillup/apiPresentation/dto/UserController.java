package com.skillup.apiPresentation.dto;

import com.skillup.apiPresentation.dto.in.UserInDto;
import com.skillup.apiPresentation.dto.out.UserOutDto;
import com.skillup.apiPresentation.util.SkillUpCommon;
import com.skillup.apiPresentation.util.SkillUpResponse;
import com.skillup.domain.user.UserDomain;
import com.skillup.domain.user.UserService;
import com.skillup.infrastructure.jooq.tables.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping("/account")
    public ResponseEntity<SkillUpResponse> createUser(@RequestBody UserInDto userInDto){

        UserDomain userDomain;
        // insert data into data table
        try{
            userDomain = userService.createUser(toDomain(userInDto));
            return ResponseEntity.status(SkillUpCommon.SUCCESS).body(SkillUpResponse.builder()
                    .msg(null)
                    .result(toOutDto(userDomain)).build());
        } catch (Exception e){
            return ResponseEntity.status(SkillUpCommon.BAD_REQUEST).body(SkillUpResponse.builder()
                    .msg(String.format(SkillUpCommon.USER_EXISTS, userInDto.getUserName()))
                    .result(null).build());
        }
    }

    @GetMapping("/account/id/{id}")
    public ResponseEntity<SkillUpResponse> readAccountById(@PathVariable("id") String accountId){
        UserDomain userDomain = userService.readAccountById(accountId);
        // TODO: handle userDomain is null
        if (Objects.isNull(userDomain)){
            return ResponseEntity.status(SkillUpCommon.BAD_REQUEST).body(
                    SkillUpResponse.builder()
                            .msg(String.format(SkillUpCommon.USER_ID_WRONG, accountId))
                            .result(null).build()
            );
        }
        return ResponseEntity.status(SkillUpCommon.SUCCESS).body(
                SkillUpResponse.builder()
                        .result(toOutDto(userDomain)).build());
    }

    @GetMapping("/account/name/{name}")
    public ResponseEntity<SkillUpResponse> readAccountByName(@PathVariable("name") String accountName){
        UserDomain userDomain = userService.readAccountByName(accountName);
        // TODO: handle userDomain is null
        if (Objects.isNull(userDomain)){
            return ResponseEntity.status(SkillUpCommon.BAD_REQUEST).body(
                    SkillUpResponse.builder()
                            .msg(String.format(SkillUpCommon.USER_NAME_WRONG, accountName))
                            .result(null).build()
            );
        }
        return ResponseEntity.status(SkillUpCommon.SUCCESS).body(
                SkillUpResponse.builder()
                        .result(toOutDto(userDomain)).build());
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
