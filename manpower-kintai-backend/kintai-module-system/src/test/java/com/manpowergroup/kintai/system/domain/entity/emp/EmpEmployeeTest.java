package com.manpowergroup.kintai.system.domain.entity.emp;

import com.manpowergroup.kintai.common.enums.Status;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmpEmployeeTest {

    @Test
    void createDefaultsStatusToEnabledWhenStatusIsNotSpecified() {
        LocalDate birthDate = LocalDate.of(1990, 1, 2);
        LocalDate hireDate = LocalDate.of(2020, 4, 1);

        EmpEmployee employee = EmpEmployee.create(
                1L,
                "E001",
                "Yamada",
                "Taro",
                "ヤマダ",
                "タロウ",
                "taro@example.com",
                "090-0000-0000",
                1,
                birthDate,
                hireDate,
                null,
                null);

        assertEquals(1L, employee.getCompanyId());
        assertEquals("E001", employee.getEmployeeCode());
        assertEquals("Yamada", employee.getLastName());
        assertEquals("Taro", employee.getFirstName());
        assertEquals("ヤマダ", employee.getLastNameKana());
        assertEquals("タロウ", employee.getFirstNameKana());
        assertEquals("taro@example.com", employee.getEmail());
        assertEquals("090-0000-0000", employee.getPhone());
        assertEquals(1, employee.getGender());
        assertEquals(birthDate, employee.getBirthDate());
        assertEquals(hireDate, employee.getHireDate());
        assertEquals(Status.ENABLED, employee.getStatus());
    }

    @Test
    void updatePersonalInfoKeepsEmploymentIdentityAndStatus() {
        LocalDate hireDate = LocalDate.of(2020, 4, 1);
        EmpEmployee employee = EmpEmployee.create(
                1L,
                "E001",
                "Old",
                "Name",
                "オールド",
                "ネーム",
                "old@example.com",
                "090-0000-0000",
                1,
                LocalDate.of(1990, 1, 2),
                hireDate,
                null,
                Status.DISABLED)
                .setId(99L);

        LocalDate newBirthDate = LocalDate.of(1991, 2, 3);
        employee.updatePersonalInfo(
                "New",
                "Person",
                "ニュー",
                "パーソン",
                "new@example.com",
                "080-1111-2222",
                2,
                newBirthDate);

        assertEquals(99L, employee.getId());
        assertEquals(1L, employee.getCompanyId());
        assertEquals("E001", employee.getEmployeeCode());
        assertEquals(hireDate, employee.getHireDate());
        assertEquals(Status.DISABLED, employee.getStatus());
        assertEquals("New", employee.getLastName());
        assertEquals("Person", employee.getFirstName());
        assertEquals("ニュー", employee.getLastNameKana());
        assertEquals("パーソン", employee.getFirstNameKana());
        assertEquals("new@example.com", employee.getEmail());
        assertEquals("080-1111-2222", employee.getPhone());
        assertEquals(2, employee.getGender());
        assertEquals(newBirthDate, employee.getBirthDate());
    }
}
