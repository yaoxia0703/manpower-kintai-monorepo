package com.manpowergroup.kintai.system.domain.entity.sys;

import com.baomidou.mybatisplus.annotation.*;
import com.manpowergroup.kintai.common.enums.Status;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

// 列挙値定義（DIVISION / DEPT / L1〜L5 等）
@Getter
@Setter(AccessLevel.PRIVATE)
@Accessors(chain = true)
@TableName("sys_enum_value")
/** 列挙型に属する値と表示順を管理する。 */
public class SysEnumValue {

    @TableId(type = IdType.AUTO)
    @Setter
    // 列挙値ID
    private Long id;

    // 列挙型コード（sys_enum_type.codeを参照）
    private String enumTypeCode;

    // 列挙値コード（例：DIVISION）
    private String code;

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

    /** 列挙値を作成し、未指定の状態を有効に初期化する。 */
    public static SysEnumValue create(String enumTypeCode, String code, Integer sort, Status status) {
        return new SysEnumValue()
                .setEnumTypeCode(enumTypeCode)
                .setCode(code)
                .setSort(sort)
                .setStatus(status == null ? Status.ENABLED : status);
    }

    /** 所属する列挙型を含む編集可能な属性を更新する。 */
    public void updateEditableFields(String enumTypeCode, String code, Integer sort) {
        this.enumTypeCode = enumTypeCode;
        this.code = code;
        this.sort = sort;
    }

    public void enable() {
        this.status = Status.ENABLED;
    }

    public void disable() {
        this.status = Status.DISABLED;
    }
}

