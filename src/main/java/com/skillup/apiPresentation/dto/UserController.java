package com.skillup.apiPresentation.dto;

import com.skillup.apiPresentation.dto.in.UserInDto;
import com.skillup.apiPresentation.dto.in.UserPin;
import com.skillup.apiPresentation.dto.out.UserOutDto;
import com.skillup.apiPresentation.util.SkillUpCommon;
import com.skillup.apiPresentation.util.SkillUpResponse;
import com.skillup.domain.user.UserDomain;
import com.skillup.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;


// 这一层为API层，是防腐层（对上下层分别处理使其隔离开），只是做数据的收发和转化。如果需要处理，调用一下真正处理的函数就好

// userController在这里要实现多线程，每个用户进来都会开辟一个内存空间，UserDomain userDomain这样的引用就会放到stack上，生成
// 的对象则放到heap上去。所以当线程做切换时，切的只是stack
@RestController
@RequestMapping("/account")
public class UserController {
    /*
     在这里直接new的操作不好，需要避免，因为每当controller接收postMapping里的请求都要调用createUser的方法连接数据库，
     那么每次都要new一个对象，而这个对象我们可能之后需要复用，需要它一直存在。但是因为每次都new，所以一旦时间久了用不到，就
     会被JVM回收了，或者说回收了之后我们需要重新创建，这样就增加了开销。解决方法就是在UserService里加一个@Service，这样
     spring就会自动帮我们生成一个唯一的对象（单例模式），调用的时候就加一个@Autowired，这样程序就会自动去@Service保存的
     管理器找，能找到就说明生成过了，否则就是没有，这样避免了每次都new。
     eg. UserService userService = new UserService();
*/

    @Autowired
    UserService userService;
    @PostMapping("")
    public ResponseEntity<SkillUpResponse> createUser(@RequestBody UserInDto userInDto){
        // insert data into user table
        // domain没有数据库操作，具体的数据库操作是通过调用repository来完成
        // 把inDto变成一个domain
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

    @GetMapping("/id/{id}")
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

    @GetMapping("/name/{name}")
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
    // 决定逻辑放在哪里需要先考虑和返回值有没有关系，如果有关且不复杂，就直接放在这，不然如果包装到domain层，最终还是要传回来。
    @PostMapping("/login")
    public ResponseEntity<SkillUpResponse> login(@RequestBody UserInDto userInDto){
        // 1. get user by name, if fail , return 400
        UserDomain userDomain = userService.readAccountByName(userInDto.getUserName());
        if (Objects.isNull(userDomain)){
            return ResponseEntity.status(SkillUpCommon.BAD_REQUEST).body(
                    SkillUpResponse.builder()
                            .msg(String.format(SkillUpCommon.USER_NAME_WRONG, userInDto.getUserName()))
                            .build()
            );
        }
        // 2. check credentials and 3. if check fail, return 400
        if (!userInDto.getPassword().equals(userDomain.getPassword())){
            return ResponseEntity.status(SkillUpCommon.BAD_REQUEST).body(
                    SkillUpResponse.builder()
                            .msg(SkillUpCommon.PASSWORD_NOT_MATCH)
                            .build());
        }

        // 4. if check success, return 200
        return ResponseEntity.status(SkillUpCommon.SUCCESS).body(
                SkillUpResponse.builder()
                        .result(toOutDto(userDomain))
                        .build());
    }

    @PutMapping("/password")
    public ResponseEntity<SkillUpResponse> updatePassword(@RequestBody UserPin userPin){
        // 1. get user, 1.1 user name is wrong, return 400
        UserDomain userDomain = userService.readAccountByName(userPin.getUserName());
        if (Objects.isNull(userDomain)){
            return ResponseEntity.status(SkillUpCommon.BAD_REQUEST).body(
                    SkillUpResponse.builder()
                            .msg(String.format(SkillUpCommon.USER_NAME_WRONG, userPin.getUserName()))
                            .build()
            );
        }
        // 2. check password
        if (!userPin.getOldPassword().equals(userDomain.getPassword())){
            return ResponseEntity.status(SkillUpCommon.BAD_REQUEST).body(
                    SkillUpResponse.builder()
                            .msg(SkillUpCommon.PASSWORD_NOT_MATCH)
                            .build());
        }
        // 3. if match, update the password and save, return 200
        userDomain.setPassword(userPin.getNewPassword());
        userService.updateUser(userDomain);

        return ResponseEntity.status(SkillUpCommon.SUCCESS).body(
                SkillUpResponse.builder()
                        .result(toOutDto(userDomain))
                        .build());
    }
    // UserInDto和UserOutDto里的数据都是残缺的，只有domain中的才完整，所以这里要先转化为一个userDomain
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
