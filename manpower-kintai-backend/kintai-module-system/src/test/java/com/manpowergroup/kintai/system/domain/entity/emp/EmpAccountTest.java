package com.manpowergroup.kintai.system.domain.entity.emp;

import com.manpowergroup.kintai.common.enums.Status;
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
        EmpAccount account = new EmpAccount()
                .setStatus(Status.ENABLED)
                .setPassword("encoded");
        when(encoder.matches("raw", "encoded")).thenReturn(true);

        account.authenticate("raw", encoder);

        verify(encoder).matches("raw", "encoded");
    }

    @Test
    void authenticateRejectsDisabledAccount() {
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        EmpAccount account = new EmpAccount()
                .setStatus(Status.DISABLED)
                .setPassword("encoded");

        assertThrows(BizException.class, () -> account.authenticate("raw", encoder));
    }

    @Test
    void authenticateRejectsWrongPassword() {
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        EmpAccount account = new EmpAccount()
                .setStatus(Status.ENABLED)
                .setPassword("encoded");
        when(encoder.matches("raw", "encoded")).thenReturn(false);

        assertThrows(BizException.class, () -> account.authenticate("raw", encoder));
    }

    @Test
    void recordLoginUpdatesLastLogin() {
        EmpAccount account = new EmpAccount();
        LocalDateTime loggedInAt = LocalDateTime.of(2026, 6, 4, 9, 30);

        account.recordLogin(loggedInAt);

        assertEquals(loggedInAt, account.getLastLogin());
    }
}
