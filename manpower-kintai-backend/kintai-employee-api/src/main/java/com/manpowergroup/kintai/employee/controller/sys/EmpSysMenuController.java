package com.manpowergroup.kintai.employee.controller.sys;

import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.framework.security.jwt.LoginPrincipal;
import com.manpowergroup.kintai.system.domain.entity.sys.SysMenu;
import com.manpowergroup.kintai.system.application.service.sys.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// メニューController（社員向け・自分のロールに紐づくメニューのみ）
@RestController
@RequestMapping("/employee/sys/menus")
@RequiredArgsConstructor
public class EmpSysMenuController {

    private final SysMenuService service;

    // ログイン中の社員がアクセス可能なメニュー一覧を取得
    @GetMapping
    public Result<List<SysMenu>> getMyMenus(@AuthenticationPrincipal LoginPrincipal principal) {
        return Result.ok(service.listByEmployeeId(principal.employeeId()));
    }
}


