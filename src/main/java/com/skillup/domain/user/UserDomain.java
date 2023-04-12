package com.skillup.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
// domain层只关注业务逻辑，处理业务
// 这里由Lombok的data帮我们做getter和setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDomain {
    private String userId;
    private String userName;
    private String password;
}
