package com.manpowergroup.kintai.system.domain.entity.sys;

import com.baomidou.mybatisplus.annotation.*;
import com.manpowergroup.kintai.common.enums.Status;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

// 権限マスタ（method + path + code 三位一体）
@Data
@Accessors(chain = true)
@TableName("sys_permission")
public class SysPermission {

    @TableId(type = IdType.AUTO)
    // 権限ID
    private Long id;

    // メニューID
    private Long menuId;

    // 権限コード（例：employee:list）
    private String code;

    // 権限名称
    private String name;

    // HTTPメソッド（GET/POST/PUT/DELETE）
    private String method;

    // APIパス
    private String path;

    // 備考
    private String remark;

    // 表示順
    private Integer sort;

    // ステータス
    private Status status;

    // 作成者ID
    private Long createdBy;

    // 作成日時
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    // 更新者ID
    private Long updatedBy;

    // 更新日時
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    // 論理削除（0=有効 1=削除）
    @TableLogic
    private Integer isDeleted;

    public void enable() {
        this.status = Status.ENABLED;
    }

    public void disable() {
        this.status = Status.DISABLED;
    }
}

