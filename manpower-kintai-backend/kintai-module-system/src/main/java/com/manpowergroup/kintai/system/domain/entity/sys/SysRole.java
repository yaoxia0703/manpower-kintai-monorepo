package com.manpowergroup.kintai.system.domain.entity.sys;

import com.baomidou.mybatisplus.annotation.*;
import com.manpowergroup.kintai.common.enums.Status;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

// ロールマスタ
@Getter
@Setter(AccessLevel.PRIVATE)
@Accessors(chain = true)
@TableName("sys_role")
/** 会社単位または全社共通のロール定義を管理する。 */
public class SysRole {

    @TableId(type = IdType.AUTO)
    @Setter
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

    /** 有効状態のロールを作成する。 */
    public static SysRole create(Long companyId, String code, String name, String remark, Integer sort) {
        return new SysRole()
                .setCompanyId(companyId)
                .setCode(code)
                .setName(name)
                .setRemark(remark)
                .setSort(sort)
                .setStatus(Status.ENABLED);
    }

    /** ロールの編集可能な属性を更新する。 */
    public void updateEditableFields(Long companyId, String code, String name, String remark, Integer sort) {
        this.companyId = companyId;
        this.code = code;
        this.name = name;
        this.remark = remark;
        this.sort = sort;
    }

    public void enable() {
        this.status = Status.ENABLED;
    }

    public void disable() {
        this.status = Status.DISABLED;
    }
}

