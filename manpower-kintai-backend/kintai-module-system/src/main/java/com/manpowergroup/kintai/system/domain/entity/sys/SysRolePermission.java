package com.manpowergroup.kintai.system.domain.entity.sys;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

// ロール権限関連
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@Accessors(chain = true)
@TableName("sys_role_permission")
/** ロールと権限の割当関係を表す。 */
public class SysRolePermission {

    // ロールID
    private Long roleId;

    // 権限ID
    private Long permissionId;

    // 作成者ID
    private Long createdBy;

    // 作成日時
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /** ロールと権限を関連付ける。 */
    public static SysRolePermission link(Long roleId, Long permissionId) {
        return new SysRolePermission()
                .setRoleId(roleId)
                .setPermissionId(permissionId);
    }
}

