package com.manpowergroup.kintai.system.domain.entity.sys;

import com.baomidou.mybatisplus.annotation.*;
import com.manpowergroup.kintai.common.enums.Status;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

// 列挙型マスタ（ORG_NODE_TYPE / GRADE_LEVEL 等）
@Getter
@Setter(AccessLevel.PRIVATE)
@Accessors(chain = true)
@TableName("sys_enum_type")
/** 列挙型の名称、並び順および有効状態を管理する。 */
public class SysEnumType {

    @TableId(type = IdType.AUTO)
    @Setter
    // 列挙型ID
    private Long id;

    // 列挙型コード（例：ORG_NODE_TYPE）
    private String code;

    // 列挙型名称
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

    /** 列挙型を作成し、未指定の状態を有効に初期化する。 */
    public static SysEnumType create(String code, String name, String remark, Integer sort, Status status) {
        return new SysEnumType()
                .setCode(code)
                .setName(name)
                .setRemark(remark)
                .setSort(sort)
                .setStatus(status == null ? Status.ENABLED : status);
    }

    /** 列挙型の編集可能な属性を更新する。 */
    public void updateEditableFields(String code, String name, String remark, Integer sort) {
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

