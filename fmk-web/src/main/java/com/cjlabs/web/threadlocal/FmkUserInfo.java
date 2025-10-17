package com.cjlabs.web.threadlocal;

import com.cjlabs.core.types.longs.FmkUserId;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FmkUserInfo {

    private FmkUserId userId;

    private String userName;

}
