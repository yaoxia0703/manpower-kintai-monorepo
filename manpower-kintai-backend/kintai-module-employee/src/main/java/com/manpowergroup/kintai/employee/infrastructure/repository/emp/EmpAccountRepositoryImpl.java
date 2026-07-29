package com.manpowergroup.kintai.employee.infrastructure.repository.emp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpAccount;
import com.manpowergroup.kintai.employee.domain.repository.emp.EmpAccountRepository;
import com.manpowergroup.kintai.employee.infrastructure.mapper.emp.EmpAccountMapper;
import org.springframework.stereotype.Repository;

@Repository
public class EmpAccountRepositoryImpl implements EmpAccountRepository {

    private final EmpAccountMapper empAccountMapper;

    public EmpAccountRepositoryImpl(EmpAccountMapper empAccountMapper) {
        this.empAccountMapper = empAccountMapper;
    }


    @Override
    public EmpAccount getById(Long id) {
        return empAccountMapper.selectById(id);
    }

    @Override
    public EmpAccount getByEmployeeId(Long employeeId) {
        return empAccountMapper.selectOne(new LambdaQueryWrapper<EmpAccount>()
            .eq(EmpAccount::getEmployeeId, employeeId)
        );
    }

    @Override
    public long countByUsername(String username) {
        return empAccountMapper.selectCount(
            new LambdaQueryWrapper<EmpAccount>()
                .eq(EmpAccount::getUsername, username));
    }

    @Override
    public boolean existsByUsernameExcludingId(String username, Long excludeId) {
        return empAccountMapper.selectCount(new LambdaQueryWrapper<EmpAccount>()
            .eq(EmpAccount::getUsername, username)
            .ne(EmpAccount::getId, excludeId)) > 0;
    }

    @Override
    public void save(EmpAccount empAccount) {
        empAccountMapper.insert(empAccount);
    }

    @Override
    public void updateById(EmpAccount empAccount) {
        empAccountMapper.updateById(empAccount);
    }

    @Override
    public void deleteById(Long id) {
        empAccountMapper.deleteById(id);
    }
}
