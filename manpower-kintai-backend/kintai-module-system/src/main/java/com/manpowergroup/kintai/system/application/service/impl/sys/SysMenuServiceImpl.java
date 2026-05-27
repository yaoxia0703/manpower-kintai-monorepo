package com.manpowergroup.kintai.system.application.service.impl.sys;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEmployeeRole;
import com.manpowergroup.kintai.system.domain.entity.sys.SysMenu;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRoleMenu;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysEmployeeRoleMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysMenuMapper;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysRoleMenuMapper;
import com.manpowergroup.kintai.system.application.service.sys.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// メニューマスタサービス実装（アプリケーション層）
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu>
        implements SysMenuService {

    private final SysEmployeeRoleMapper employeeRoleMapper;
    private final SysRoleMenuMapper roleMenuMapper;

    @Override
    public SysMenu getById(Long id) {
        SysMenu menu = super.getById(id);
        if (menu == null) throw new BizException(SystemErrorCode.MENU_NOT_FOUND);
        return menu;
    }

    @Override
    public List<SysMenu> listAll() {
        return lambdaQuery().orderByAsc(SysMenu::getSort).list();
    }

    @Override
    public List<SysMenu> listByEmployeeId(Long employeeId) {
        // 有効期間内のロールIDを取得
        List<Long> roleIds = employeeRoleMapper.selectList(Wrappers.<SysEmployeeRole>lambdaQuery()
                        .eq(SysEmployeeRole::getEmployeeId, employeeId)
                        .le(SysEmployeeRole::getStartDate, LocalDate.now())
                        .and(w -> w.isNull(SysEmployeeRole::getEndDate)
                                .or().ge(SysEmployeeRole::getEndDate, LocalDate.now())))
                .stream().map(SysEmployeeRole::getRoleId).collect(Collectors.toList());
        if (roleIds.isEmpty()) return Collections.emptyList();
        // ロールに紐づくメニューIDを取得
        List<Long> menuIds = roleMenuMapper.selectList(Wrappers.<SysRoleMenu>lambdaQuery()
                        .in(SysRoleMenu::getRoleId, roleIds))
                .stream().map(SysRoleMenu::getMenuId).distinct().collect(Collectors.toList());
        if (menuIds.isEmpty()) return Collections.emptyList();
        return lambdaQuery()
                .in(SysMenu::getId, menuIds)
                .orderByAsc(SysMenu::getSort)
                .list();
    }

    @Override
    @Transactional
    public SysMenu create(SysMenu menu) {
        boolean exists = lambdaQuery().eq(SysMenu::getCode, menu.getCode()).count() > 0;
        if (exists) throw new BizException(SystemErrorCode.MENU_CODE_DUPLICATE);
        save(menu);
        return menu;
    }

    @Override
    @Transactional
    public SysMenu update(Long id, SysMenu menu) {
        SysMenu existing = getById(id);
        boolean exists = lambdaQuery()
                .eq(SysMenu::getCode, menu.getCode())
                .ne(SysMenu::getId, id)
                .count() > 0;
        if (exists) throw new BizException(SystemErrorCode.MENU_CODE_DUPLICATE);
        existing.setName(menu.getName())
                .setCode(menu.getCode())
                .setPath(menu.getPath())
                .setComponent(menu.getComponent())
                .setIcon(menu.getIcon())
                .setType(menu.getType())
                .setSort(menu.getSort())
                .setParentId(menu.getParentId());
        updateById(existing);
        return existing;
    }

    @Override
    @Transactional
    public void show(Long id) {
        SysMenu menu = getById(id);
        menu.setVisible(1);
        updateById(menu);
    }

    @Override
    @Transactional
    public void hide(Long id) {
        SysMenu menu = getById(id);
        menu.setVisible(0);
        updateById(menu);
    }

    @Override
    @Transactional
    public void enable(Long id) {
        SysMenu menu = getById(id);
        menu.setStatus(Status.ENABLED);
        updateById(menu);
    }

    @Override
    @Transactional
    public void disable(Long id) {
        SysMenu menu = getById(id);
        menu.setStatus(Status.DISABLED);
        updateById(menu);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        getById(id);
        removeById(id);
    }

    enum SystemErrorCode implements BaseErrorCode {
        MENU_NOT_FOUND(404, "error.menu.not_found"),
        MENU_CODE_DUPLICATE(409, "error.menu.code_duplicate");

        private final int code;
        private final String messageKey;

        SystemErrorCode(int code, String messageKey) {
            this.code = code;
            this.messageKey = messageKey;
        }

        @Override public int code() { return code; }
        @Override public String messageKey() { return messageKey; }
    }
}

