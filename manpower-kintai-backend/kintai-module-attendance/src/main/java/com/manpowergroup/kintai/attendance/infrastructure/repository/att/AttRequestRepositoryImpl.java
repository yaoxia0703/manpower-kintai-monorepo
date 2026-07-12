package com.manpowergroup.kintai.attendance.infrastructure.repository.att;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manpowergroup.kintai.attendance.domain.entity.att.AttRequest;
import com.manpowergroup.kintai.attendance.domain.repository.att.AttRequestRepository;
import com.manpowergroup.kintai.attendance.infrastructure.mapper.att.AttRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AttRequestRepositoryImpl implements AttRequestRepository {

    private final AttRequestMapper mapper;

    @Override
    public Optional<AttRequest> findById(Long requestId) {
        return Optional.ofNullable(mapper.selectById(requestId));
    }

    @Override
    public Optional<AttRequest> findByIdAndEmployee(Long requestId, Long employeeId) {
        return Optional.ofNullable(mapper.selectOne(Wrappers.<AttRequest>lambdaQuery()
                .eq(AttRequest::getId, requestId)
                .eq(AttRequest::getEmployeeId, employeeId)));
    }

    @Override
    public List<AttRequest> listByEmployee(Long employeeId) {
        return mapper.selectList(Wrappers.<AttRequest>lambdaQuery()
                .eq(AttRequest::getEmployeeId, employeeId)
                .orderByDesc(AttRequest::getCreatedAt));
    }

    @Override
    public AttRequest save(AttRequest request) {
        mapper.insert(request);
        return request;
    }

    @Override
    public AttRequest update(AttRequest request) {
        mapper.updateById(request);
        return request;
    }
}
