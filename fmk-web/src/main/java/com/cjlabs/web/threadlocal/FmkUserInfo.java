package com.cjlabs.web.threadlocal;

import com.cjlabs.core.types.longs.FmkUserId;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FmkUserInfo {

    private FmkUserId userId;

    private String userName;

}
