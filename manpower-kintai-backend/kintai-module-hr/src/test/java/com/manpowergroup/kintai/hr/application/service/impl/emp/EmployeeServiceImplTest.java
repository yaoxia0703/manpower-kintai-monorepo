package com.manpowergroup.kintai.hr.application.service.impl.emp;

import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.hr.application.port.emp.EmployeeDirectoryProvider;
import com.manpowergroup.kintai.hr.application.port.emp.EmployeeRegisterEntry;
import com.manpowergroup.kintai.hr.application.port.emp.EmployeeRegisterProvider;
import com.manpowergroup.kintai.hr.application.port.emp.EmployeeRegisterResult;
import com.manpowergroup.kintai.hr.application.port.emp.EmployeeRegisterValidationProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EmployeeServiceImplTest {

    private static final long OPERATOR_ID = 1L;
    private static final long COMPANY_ID = 10L;
    private static final long NODE_ID = 20L;
    private static final long GRADE_ID = 30L;

    private EmployeeRegisterProvider registerProvider;
    private EmployeeRegisterValidationProvider validationProvider;
    private EmployeeServiceImpl service;

    @BeforeEach
    void setUp() {
        registerProvider = mock(EmployeeRegisterProvider.class);
        validationProvider = mock(EmployeeRegisterValidationProvider.class);
        service = new EmployeeServiceImpl(
            mock(EmployeeDirectoryProvider.class), registerProvider, validationProvider);
        when(validationProvider.getOperator(OPERATOR_ID)).thenReturn(Optional.of(
            new EmployeeRegisterValidationProvider.OperatorInfo(OPERATOR_ID, COMPANY_ID, Status.ENABLED)));
        when(validationProvider.companyExists(COMPANY_ID)).thenReturn(true);
        when(validationProvider.nodeBelongsToCompany(NODE_ID, COMPANY_ID)).thenReturn(true);
        when(validationProvider.gradeBelongsToCompany(GRADE_ID, COMPANY_ID)).thenReturn(true);
    }

    @Test
    void registerEmployeeAcceptsReferencesBelongingToTargetCompany() {
        EmployeeRegisterEntry entry = entry();
        EmployeeRegisterResult expected = new EmployeeRegisterResult(
            100L, 200L, 300L, "EMP-001", "Test User", "test@example.com");
        when(registerProvider.registerEmployee(entry)).thenReturn(expected);

        EmployeeRegisterResult actual = service.registerEmployee(entry, OPERATOR_ID);

        assertEquals(expected, actual);
        verify(validationProvider).companyExists(COMPANY_ID);
        verify(registerProvider).registerEmployee(entry);
    }

    @Test
    void registerEmployeeRejectsMissingTargetCompany() {
        when(validationProvider.companyExists(COMPANY_ID)).thenReturn(false);

        assertThrows(BizException.class, () -> service.registerEmployee(entry(), OPERATOR_ID));

        verify(validationProvider).companyExists(COMPANY_ID);
        verify(registerProvider, never()).registerEmployee(entry());
    }

    @Test
    void registerEmployeeRejectsNodeOutsideTargetCompany() {
        when(validationProvider.nodeBelongsToCompany(NODE_ID, COMPANY_ID)).thenReturn(false);

        assertThrows(BizException.class, () -> service.registerEmployee(entry(), OPERATOR_ID));

        verify(registerProvider, never()).registerEmployee(entry());
    }

    @Test
    void registerEmployeeRejectsGradeOutsideTargetCompany() {
        when(validationProvider.gradeBelongsToCompany(GRADE_ID, COMPANY_ID)).thenReturn(false);

        assertThrows(BizException.class, () -> service.registerEmployee(entry(), OPERATOR_ID));

        verify(registerProvider, never()).registerEmployee(entry());
    }

    private EmployeeRegisterEntry entry() {
        return new EmployeeRegisterEntry(
            COMPANY_ID,
            "EMP-001",
            "Test",
            "User",
            null,
            null,
            "test@example.com",
            null,
            null,
            null,
            NODE_ID,
            GRADE_ID,
            null,
            "test-user",
            "password");
    }
}
