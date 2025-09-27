package com.cjlabs.web.requestinterceptor;

import com.xodo.business.admin.sysrole.service.SysRoleService;
import com.xodo.business.admin.sysuser.resp.SysAdminUserResponse;
import com.xodo.business.admin.sysuser.service.SysAdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class FmkAdminUserCheckService {
    @Autowired
    private SysAdminUserService sysAdminUserService;
    @Autowired
    private SysRoleService sysRoleService;

    public boolean checkRole() {
        SysAdminUserResponse sysUserDetail = sysAdminUserService.getSysUserDetail();
        return Objects.nonNull(sysUserDetail);
    }
}
