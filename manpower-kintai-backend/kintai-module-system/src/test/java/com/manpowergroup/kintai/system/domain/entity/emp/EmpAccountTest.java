package com.manpowergroup.kintai.system.domain.entity.emp;

import com.manpowergroup.kintai.common.exception.BizException;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EmpAccountTest {

    @Test
    void authenticateAcceptsEnabledAccountWithMatchingPassword() {
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        when(encoder.encode("raw")).thenReturn("encoded");
        EmpAccount account = EmpAccount.register(1L, "user", "raw", encoder);
        when(encoder.matches("raw", "encoded")).thenReturn(true);

        account.authenticate("raw", encoder);

        verify(encoder).matches("raw", "encoded");
    }

    @Test
    void authenticateRejectsDisabledAccount() {
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        when(encoder.encode("raw")).thenReturn("encoded");
        EmpAccount account = EmpAccount.register(1L, "user", "raw", encoder);
        account.disable();

        assertThrows(BizException.class, () -> account.authenticate("raw", encoder));
    }

    @Test
    void authenticateRejectsWrongPassword() {
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        when(encoder.encode("raw")).thenReturn("encoded");
        EmpAccount account = EmpAccount.register(1L, "user", "raw", encoder);
        when(encoder.matches("raw", "encoded")).thenReturn(false);

        assertThrows(BizException.class, () -> account.authenticate("raw", encoder));
    }

    @Test
    void registerStoresEncodedPasswordAndEnablesAccount() {
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        when(encoder.encode("raw")).thenReturn("encoded");

        EmpAccount account = EmpAccount.register(1L, "user", "raw", encoder);

        assertEquals(1L, account.getEmployeeId());
        assertEquals("user", account.getUsername());
        assertEquals("encoded", account.getPassword());
        assertEquals(com.manpowergroup.kintai.common.enums.Status.ENABLED, account.getStatus());
    }

    @Test
    void changePasswordUpdatesPasswordWhenOldPasswordMatches() {
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        when(encoder.encode("old")).thenReturn("encoded-old");
        when(encoder.encode("new")).thenReturn("encoded-new");
        when(encoder.matches("old", "encoded-old")).thenReturn(true);
        EmpAccount account = EmpAccount.register(1L, "user", "old", encoder);

        account.changePassword("old", "new", encoder);

        assertEquals("encoded-new", account.getPassword());
    }

    @Test
    void changePasswordRejectsWrongOldPassword() {
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        when(encoder.encode("old")).thenReturn("encoded-old");
        when(encoder.matches("wrong", "encoded-old")).thenReturn(false);
        EmpAccount account = EmpAccount.register(1L, "user", "old", encoder);

        assertThrows(BizException.class, () -> account.changePassword("wrong", "new", encoder));
    }

    @Test
    void recordLoginUpdatesLastLogin() {
        EmpAccount account = new EmpAccount();
        LocalDateTime loggedInAt = LocalDateTime.of(2026, 6, 4, 9, 30);

        account.recordLogin(loggedInAt);

        assertEquals(loggedInAt, account.getLastLogin());
    }
}
