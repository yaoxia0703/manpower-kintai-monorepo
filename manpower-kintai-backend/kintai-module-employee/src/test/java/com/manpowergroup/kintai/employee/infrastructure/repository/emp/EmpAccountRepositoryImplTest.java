package com.manpowergroup.kintai.employee.infrastructure.repository.emp;

import com.manpowergroup.kintai.employee.domain.entity.emp.EmpAccount;
import com.manpowergroup.kintai.employee.infrastructure.mapper.emp.EmpAccountMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class EmpAccountRepositoryImplTest {

    private EmpAccountMapper empAccountMapper;
    private EmpAccountRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        empAccountMapper = mock(EmpAccountMapper.class);
        repository = new EmpAccountRepositoryImpl(empAccountMapper);
    }

    @Test
    void delegatesWriteOperationsToMapper() {
        EmpAccount account = new EmpAccount();

        repository.save(account);
        repository.updateById(account);
        repository.deleteById(9L);

        verify(empAccountMapper).insert(account);
        verify(empAccountMapper).updateById(account);
        verify(empAccountMapper).deleteById(9L);
    }
}
