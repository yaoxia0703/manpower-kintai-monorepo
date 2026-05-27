package com.manpowergroup.kintai.system.domain.entity.emp;

import com.baomidou.mybatisplus.annotation.*;
import com.manpowergroup.kintai.common.enums.Status;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

// 社員マスタ
@Data
@Accessors(chain = true)
@TableName("emp_employee")
public class EmpEmployee {

    @TableId(type = IdType.AUTO)
    private Long id;

    // 所属会社ID
    private Long companyId;

    // 社員番号
    private String employeeCode;

    // 姓
    private String lastName;

    // 名
    private String firstName;

    // 姓（カナ）
    private String lastNameKana;

    // 名（カナ）
    private String firstNameKana;

    // メールアドレス
    private String email;

    // 電話番号
    private String phone;

    // 性別（1=男 2=女 0=その他）
    private Integer gender;

    // 生年月日
    private LocalDate birthDate;

    // 入社日
    private LocalDate hireDate;

    // 退職日
    private LocalDate leaveDate;

    // ステータス（1=在職 0=退職）
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

