package com.manpowergroup.kintai.system.domain.entity.org;

import com.baomidou.mybatisplus.annotation.*;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

// 組織ノード（無限階層・Closure Table方式）
@Getter
@Setter(AccessLevel.PRIVATE)
@Accessors(chain = true)
@TableName("org_node")
/** Closure Table と連携する組織ノードで、親子関係と階層レベルを管理する。 */
public class OrgNode {

    @TableId(type = IdType.AUTO)
    @Setter
    // 組織ノードID
    private Long id;

    // 所属会社ID
    private Long companyId;

    // 親ノードID（NULLがルート）
    private Long parentId;

    // ノード責任者社員ID
    private Long managerId;

    // ノード名
    private String name;

    // ノードタイプコード（ORG_NODE_TYPE参照：DIVISION/DEPT/SECTION/TEAM）
    private String typeCode;

    // 部門機能区分（DEPT_FUNCTION参照：GENERAL/FINANCE/HR/SALES）
    private String deptFunction;

    // ノードコード
    private String code;

    // 階層レベル
    private Integer level;

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

    /** 親ノードとの会社整合性を検証し、階層レベルを導出して作成する。 */
    public static OrgNode create(Long companyId, OrgNode parent, Long managerId,
                                 String name, String typeCode, String deptFunction,
                                 String code, Integer sort, Status status) {
        validateParent(companyId, parent);
        return new OrgNode()
                .setCompanyId(companyId)
                .setParentId(parent == null ? null : parent.getId())
                .setManagerId(managerId)
                .setName(name)
                .setTypeCode(typeCode)
                .setDeptFunction(deptFunction)
                .setCode(code)
                .setLevel(parent == null ? 1 : parent.getLevel() + 1)
                .setSort(sort)
                .setStatus(status == null ? Status.ENABLED : status);
    }

    /** 同一会社内の新しい親へ移動し、階層レベルを更新する。 */
    public void moveTo(OrgNode parent) {
        validateParent(companyId, parent);
        this.parentId = parent == null ? null : parent.getId();
        this.level = parent == null ? 1 : parent.getLevel() + 1;
    }

    /** ツリー上の識別情報を維持したまま表示属性を更新する。 */
    public void updateEditableFields(Long managerId, String name, String typeCode,
                                     String deptFunction, String code, Integer sort) {
        this.managerId = managerId;
        this.name = name;
        this.typeCode = typeCode;
        this.deptFunction = deptFunction;
        this.code = code;
        this.sort = sort;
    }

    /** サブツリー移動時に階層レベルを相対的に補正する。 */
    public void shiftLevel(int delta) {
        this.level += delta;
    }

    private static void validateParent(Long companyId, OrgNode parent) {
        if (parent != null && !companyId.equals(parent.getCompanyId())) {
            throw BizException.withDetail(ErrorCode.VALIDATION_ERROR,
                    "organization node parent must belong to the same company");
        }
    }

    public void enable() {
        this.status = Status.ENABLED;
    }

    public void disable() {
        this.status = Status.DISABLED;
    }
}

