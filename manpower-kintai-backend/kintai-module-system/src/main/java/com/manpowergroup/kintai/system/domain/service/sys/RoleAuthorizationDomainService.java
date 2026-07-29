package com.manpowergroup.kintai.system.domain.service.sys;

import com.manpowergroup.kintai.system.domain.model.sys.RoleAuthorization;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
/** ロールのメニュー・権限割当を一体として置換するドメインサービス。 */
public class RoleAuthorizationDomainService {

    /**
     * 指定ロールのメニュー・権限設定を置き換える。
     *
     * @param roleId 対象のロールID
     * @param menuIds 対象のメニューID一覧
     * @param permissionIds 対象の権限ID一覧
     * @return ロール認可設定
     */
    public RoleAuthorization replaceAuthorization(Long roleId, List<Long> menuIds, List<Long> permissionIds) {
        return RoleAuthorization.replace(roleId, menuIds, permissionIds);
    }
}
