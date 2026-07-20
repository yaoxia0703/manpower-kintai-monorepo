package com.manpowergroup.kintai.employee.domain.entity.emp;

import com.baomidou.mybatisplus.annotation.*;
import com.manpowergroup.kintai.common.enums.Status;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

// 社員マスタ
@Getter
@Setter(AccessLevel.PRIVATE)
@Accessors(chain = true)
@TableName("emp_employee")
/** 社員の所属、雇用情報および個人情報を管理する。 */
public class EmpEmployee {

    @TableId(type = IdType.AUTO)
    @Setter
    // 社員ID
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

    /** 社員番号と雇用情報を持つ社員を作成する。 */
    public static EmpEmployee create(Long companyId, String employeeCode,
                                     String lastName, String firstName,
                                     String lastNameKana, String firstNameKana,
                                     String email, String phone, Integer gender,
                                     LocalDate birthDate, LocalDate hireDate,
                                     LocalDate leaveDate, Status status) {
        return new EmpEmployee()
                .setCompanyId(companyId)
                .setEmployeeCode(employeeCode)
                .setLastName(lastName)
                .setFirstName(firstName)
                .setLastNameKana(lastNameKana)
                .setFirstNameKana(firstNameKana)
                .setEmail(email)
                .setPhone(phone)
                .setGender(gender)
                .setBirthDate(birthDate)
                .setHireDate(hireDate)
                .setLeaveDate(leaveDate)
                .setStatus(status == null ? Status.ENABLED : status);
    }

    /** 所属や雇用上の識別情報を維持したまま個人情報を更新する。 */
    public void updatePersonalInfo(String lastName, String firstName,
                                   String lastNameKana, String firstNameKana,
                                   String email, String phone, Integer gender,
                                   LocalDate birthDate) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.lastNameKana = lastNameKana;
        this.firstNameKana = firstNameKana;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    public void enable() {
        this.status = Status.ENABLED;
    }

    public void disable() {
        this.status = Status.DISABLED;
    }
}

