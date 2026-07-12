package com.manpowergroup.kintai.attendance.domain.repository.att;

import com.manpowergroup.kintai.attendance.domain.entity.att.AttRequest;

import java.util.Optional;
import java.util.List;

public interface AttRequestRepository {

    Optional<AttRequest> findById(Long requestId);

    Optional<AttRequest> findByIdAndEmployee(Long requestId, Long employeeId);

    List<AttRequest> listByEmployee(Long employeeId);

    AttRequest save(AttRequest request);

    AttRequest update(AttRequest request);
}
