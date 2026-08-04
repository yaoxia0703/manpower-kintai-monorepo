package com.manpowergroup.kintai.employee.infrastructure.repository.emp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manpowergroup.kintai.common.dto.JoinPageResult;
import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.employee.application.dto.directory.EmployeeDirectoryFilter;
import com.manpowergroup.kintai.employee.application.dto.directory.EmployeeDirectoryResponse;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpEmployee;
import com.manpowergroup.kintai.employee.domain.repository.emp.EmpEmployeeRepository;
import com.manpowergroup.kintai.employee.infrastructure.mapper.emp.EmpEmployeeMapper;
import com.manpowergroup.kintai.employee.infrastructure.mapper.emp.EmployeeDirectoryQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class EmpEmployeeRepositoryImpl implements EmpEmployeeRepository {

    private final EmpEmployeeMapper empEmployeeMapper;
    private final EmployeeDirectoryQueryMapper employeeDirectoryQueryMapper;


    @Override
    public EmpEmployee getById(Long id) {
        return empEmployeeMapper.selectById(id);
    }

    @Override
    public EmpEmployee findByEmail(String email) {
        return empEmployeeMapper.selectOne(new LambdaQueryWrapper<EmpEmployee>()
            .eq(EmpEmployee::getEmail, email)
        );
    }

    @Override
    public PageResult<EmpEmployee> findPageByCompany(long companyId, PageRequest request) {
        Page<EmpEmployee> p = new Page<>(request.page(), request.size());
        empEmployeeMapper.selectPage(p, Wrappers.<EmpEmployee>lambdaQuery()
            .eq(EmpEmployee::getCompanyId, companyId)
            .orderByAsc(EmpEmployee::getEmployeeCode)
        );
        return PageResult.of(p);
    }

    @Override
    public PageResult<EmpEmployee> findPageByCompanyAndKeyword(long companyId, String keyword, PageRequest request) {
        Page<EmpEmployee> p = new Page<>(request.page(), request.size());
        LambdaQueryWrapper<EmpEmployee> wrapper = Wrappers.<EmpEmployee>lambdaQuery()
            .eq(EmpEmployee::getCompanyId, companyId);
        if (StringUtils.hasText(keyword)) {
            wrapper.and(q -> q
                .like(EmpEmployee::getEmployeeCode, keyword)
                .or()
                .like(EmpEmployee::getLastName, keyword)
                .or()
                .like(EmpEmployee::getFirstName, keyword)
                .or()
                .like(EmpEmployee::getLastNameKana, keyword)
                .or()
                .like(EmpEmployee::getFirstNameKana, keyword));
        }
        wrapper.orderByAsc(EmpEmployee::getEmployeeCode);
        empEmployeeMapper.selectPage(p, wrapper);
        return PageResult.of(p);
    }

    @Override
    public void save(EmpEmployee empEmployee) {
        empEmployeeMapper.insert(empEmployee);
    }

    @Override
    public void updateById(EmpEmployee empEmployee) {
        empEmployeeMapper.updateById(empEmployee);
    }

    @Override
    public void deleteById(Long id) {
        empEmployeeMapper.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email, Long excludeId) {
        LambdaQueryWrapper<EmpEmployee> wrapper = new LambdaQueryWrapper<EmpEmployee>()
            .eq(EmpEmployee::getEmail, email);
        if (excludeId != null) {
            wrapper.ne(EmpEmployee::getId, excludeId);
        }
        return empEmployeeMapper.selectCount(wrapper) > 0;
    }

    @Override
    public JoinPageResult<EmployeeDirectoryResponse> pageDirectory(EmployeeDirectoryFilter filter, int page, int size) {
        //総件数
        long total = employeeDirectoryQueryMapper.countDirectory(filter);
        if (total == 0) {
            return JoinPageResult.empty(page, size);
        }
        int offset = (page - 1) * size;
        List<EmployeeDirectoryResponse> records = employeeDirectoryQueryMapper.pageDirectory(filter, offset, size);
        return JoinPageResult.of(records, total, page, size);
    }
}
