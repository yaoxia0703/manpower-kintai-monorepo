package com.manpowergroup.kintai.employee.infrastructure.repository.emp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.manpowergroup.kintai.employee.domain.entity.emp.EmpEmployeePosition;
import com.manpowergroup.kintai.employee.domain.repository.emp.EmpEmployeePositionRepository;
import com.manpowergroup.kintai.employee.infrastructure.mapper.emp.EmpEmployeePositionMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class EmpEmployeePositionRepositoryImpl implements EmpEmployeePositionRepository {
    private final EmpEmployeePositionMapper empEmployeePositionMapper;

    public EmpEmployeePositionRepositoryImpl(EmpEmployeePositionMapper empEmployeePositionMapper) {
        this.empEmployeePositionMapper = empEmployeePositionMapper;
    }


    @Override
    public EmpEmployeePosition getById(long id) {
        return empEmployeePositionMapper.selectById(id);
    }

    @Override
    public List<EmpEmployeePosition> getByEmployeeIdAndStartDateAndEndDate(long employeeId) {
        LocalDate today = LocalDate.now();
        return empEmployeePositionMapper.selectList(new LambdaQueryWrapper<EmpEmployeePosition>()
            .eq(EmpEmployeePosition::getEmployeeId, employeeId)
            .le(EmpEmployeePosition::getStartDate, today)
            .and(w -> w.isNull(EmpEmployeePosition::getEndDate).or().ge(EmpEmployeePosition::getEndDate, today))
        );
    }

    @Override
    public List<EmpEmployeePosition> getByEmployeeId(long employeeId) {
        return empEmployeePositionMapper.selectList(new LambdaQueryWrapper<EmpEmployeePosition>()
            .eq(EmpEmployeePosition::getEmployeeId, employeeId)
            .orderByDesc(EmpEmployeePosition::getStartDate)
        );
    }

    @Override
    public EmpEmployeePosition getPrimaryByEmployee(long employeeId) {
        return empEmployeePositionMapper.selectOne(new LambdaQueryWrapper<EmpEmployeePosition>()
            .eq(EmpEmployeePosition::getEmployeeId, employeeId)
            .eq(EmpEmployeePosition::getIsPrimary, 1)
            .le(EmpEmployeePosition::getStartDate, LocalDate.now())
            .and(w -> w.isNull(EmpEmployeePosition::getEndDate)
                .or().ge(EmpEmployeePosition::getEndDate, LocalDate.now()))
        );
    }

    @Override
    public void save(EmpEmployeePosition empEmployeePosition) {
        empEmployeePositionMapper.insert(empEmployeePosition);
    }

    @Override
    public void updateById(EmpEmployeePosition empEmployeePosition) {
        empEmployeePositionMapper.updateById(empEmployeePosition);
    }

    @Override
    public void deleteById(long id) {
        empEmployeePositionMapper.deleteById(id);
    }
}
