package com.manpowergroup.kintai.system.domain.entity.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 職級ロール関連（職級に自動的に付与されるロール）
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_grade_role")
public class SysGradeRole {

    // 職級ID
    private Long gradeId;

    // ロールID
    private Long roleId;

    // 作成者ID
    private Long createdBy;

    // 作成日時
    @TableField(fill = com.baomidou.mybatisplus.annotation.FieldFill.INSERT)
    private LocalDateTime createdAt;
}

