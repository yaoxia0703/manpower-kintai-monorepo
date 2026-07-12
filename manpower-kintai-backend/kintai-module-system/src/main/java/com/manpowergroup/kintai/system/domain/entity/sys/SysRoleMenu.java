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

// ロールメニュー関連
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@Accessors(chain = true)
@TableName("sys_role_menu")
/** ロールとメニューの割当関係を表す。 */
public class SysRoleMenu {

    // ロールID
    private Long roleId;

    // メニューID
    private Long menuId;

    // 作成者ID
    private Long createdBy;

    // 作成日時
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /** ロールとメニューを関連付ける。 */
    public static SysRoleMenu link(Long roleId, Long menuId) {
        return new SysRoleMenu()
                .setRoleId(roleId)
                .setMenuId(menuId);
    }
}

