package com.manpowergroup.kintai.system.infrastructure.repository.emp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.system.domain.repository.emp.EmpEmployeeRepository;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpEmployee;
import com.manpowergroup.kintai.system.infrastructure.mapper.emp.EmpEmployeeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Optional;

// 社員マスタリポジトリ実装（infrastructure層）
@Repository
@RequiredArgsConstructor
public class EmpEmployeeRepositoryImpl implements EmpEmployeeRepository {

    private final EmpEmployeeMapper mapper;

    @Override
    public Optional<EmpEmployee> findById(Long id) {
        return Optional.ofNullable(mapper.selectById(id));
    }

    @Override
    public Optional<EmpEmployee> findByEmail(String email) {
        return Optional.ofNullable(mapper.selectOne(
                Wrappers.<EmpEmployee>lambdaQuery().eq(EmpEmployee::getEmail, email)));
    }

    @Override
    public Optional<EmpEmployee> findByEmployeeCode(Long companyId, String employeeCode) {
        return Optional.ofNullable(mapper.selectOne(Wrappers.<EmpEmployee>lambdaQuery()
                .eq(EmpEmployee::getCompanyId, companyId)
                .eq(EmpEmployee::getEmployeeCode, employeeCode)));
    }

    @Override
    public PageResult<EmpEmployee> findPageByCompanyId(Long companyId, int page, int size) {
        Page<EmpEmployee> p = new Page<>(page, size);
        mapper.selectPage(p, Wrappers.<EmpEmployee>lambdaQuery()
                .eq(EmpEmployee::getCompanyId, companyId)
                .orderByAsc(EmpEmployee::getEmployeeCode));
        return PageResult.of(p);
    }

    @Override
    public PageResult<EmpEmployee> searchByName(Long companyId, String keyword, int page, int size) {
        Page<EmpEmployee> p = new Page<>(page, size);
        LambdaQueryWrapper<EmpEmployee> wrapper = Wrappers.<EmpEmployee>lambdaQuery()
                .eq(EmpEmployee::getCompanyId, companyId);
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w
                    .like(EmpEmployee::getLastName, keyword)
                    .or().like(EmpEmployee::getFirstName, keyword)
                    .or().like(EmpEmployee::getLastNameKana, keyword)
                    .or().like(EmpEmployee::getFirstNameKana, keyword));
        }
        wrapper.orderByAsc(EmpEmployee::getEmployeeCode);
        mapper.selectPage(p, wrapper);
        return PageResult.of(p);
    }

    @Override
    public EmpEmployee save(EmpEmployee employee) {
        mapper.insert(employee);
        return employee;
    }

    @Override
    public EmpEmployee update(EmpEmployee employee) {
        mapper.updateById(employee);
        return employee;
    }

    @Override
    public void removeById(Long id) {
        mapper.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email, Long excludeId) {
        LambdaQueryWrapper<EmpEmployee> wrapper = Wrappers.<EmpEmployee>lambdaQuery()
                .eq(EmpEmployee::getEmail, email);
        if (excludeId != null) {
            wrapper.ne(EmpEmployee::getId, excludeId);
        }
        return mapper.selectCount(wrapper) > 0;
    }
}


