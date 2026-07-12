package com.manpowergroup.kintai.system.domain.service.sys;

import com.manpowergroup.kintai.system.domain.model.sys.RoleAuthorization;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
/** ロールのメニュー・権限割当を一体として置換するドメインサービス。 */
public class RoleAuthorizationDomainService {

    /** 指定ロールの割当内容を正規化して置換モデルを生成する。 */
    public RoleAuthorization replaceAuthorization(Long roleId, List<Long> menuIds, List<Long> permissionIds) {
        return RoleAuthorization.replace(roleId, menuIds, permissionIds);
    }
}
