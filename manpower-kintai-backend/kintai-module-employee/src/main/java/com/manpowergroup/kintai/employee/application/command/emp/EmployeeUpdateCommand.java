package com.manpowergroup.kintai.employee.application.command.emp;

import com.manpowergroup.kintai.common.enums.Status;

import java.time.LocalDate;

public record EmployeeUpdateCommand(
        // 会社ID
        Long companyId,

        // 社員コード
        String employeeCode,

        // 姓
        String lastName,

        // 名
        String firstName,

        // 姓カナ
        String lastNameKana,

        // 名カナ
        String firstNameKana,

        // メールアドレス
        String email,

        // 電話番号
        String phone,

        // 性別
        Integer gender,

        // 生年月日
        LocalDate birthDate,

        // 入社日
        LocalDate hireDate,

        // 退職日
        LocalDate leaveDate,

        // ステータス
        Status status
) {
}
