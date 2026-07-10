package com.manpowergroup.kintai.system.domain.entity.sys;

import com.baomidou.mybatisplus.annotation.*;
import com.manpowergroup.kintai.common.enums.Status;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

// ロールマスタ
@Data
@Accessors(chain = true)
@TableName("sys_role")
public class SysRole {

    @TableId(type = IdType.AUTO)
    // ロールID
    private Long id;

    // 所属会社ID（NULLは全社共通）
    private Long companyId;

    // ロールコード
    private String code;

    // ロール名
    private String name;

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

