package com.manpowergroup.kintai.system.domain.entity.org;

import com.baomidou.mybatisplus.annotation.*;
import com.manpowergroup.kintai.common.enums.Status;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

// 組織ノード（無限階層・Closure Table方式）
@Data
@Accessors(chain = true)
@TableName("org_node")
public class OrgNode {

    @TableId(type = IdType.AUTO)
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

    private Long createdBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    private Long updatedBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer isDeleted;
}

