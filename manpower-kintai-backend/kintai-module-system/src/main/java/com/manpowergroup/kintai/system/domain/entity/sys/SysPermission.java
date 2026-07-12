package com.manpowergroup.kintai.system.domain.entity.sys;

import com.baomidou.mybatisplus.annotation.*;
import com.manpowergroup.kintai.common.enums.Status;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

// 権限マスタ（method + path + code 三位一体）
@Getter
@Setter(AccessLevel.PRIVATE)
@Accessors(chain = true)
@TableName("sys_permission")
/** 権限コードと HTTP メソッド・パスの対応を管理する。 */
public class SysPermission {

    @TableId(type = IdType.AUTO)
    @Setter
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

    /** 有効状態の権限を作成する。 */
    public static SysPermission create(Long menuId, String code, String name, String method,
                                       String path, String remark, Integer sort) {
        return new SysPermission()
                .setMenuId(menuId)
                .setCode(code)
                .setName(name)
                .setMethod(method)
                .setPath(path)
                .setRemark(remark)
                .setSort(sort)
                .setStatus(Status.ENABLED);
    }

    /** 権限の編集可能な属性を更新する。 */
    public void updateEditableFields(Long menuId, String code, String name, String method,
                                     String path, String remark, Integer sort) {
        this.menuId = menuId;
        this.code = code;
        this.name = name;
        this.method = method;
        this.path = path;
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

