package com.cjlabs.web.threadlocal;

import com.cjlabs.core.types.longs.FmkUserId;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class FmkUserInfo {

    @ToString.Include
    private FmkUserId userId;
    
    @ToString.Include
    private String userName;
    
    /**
     * 创建一个只包含用户ID的实例
     */
    public static FmkUserInfo ofUserId(FmkUserId userId) {
        return new FmkUserInfo(userId, null);
    }
    
    /**
     * 检查用户ID是否存在
     */
    public boolean hasUserId() {
        return userId != null;
    }
}