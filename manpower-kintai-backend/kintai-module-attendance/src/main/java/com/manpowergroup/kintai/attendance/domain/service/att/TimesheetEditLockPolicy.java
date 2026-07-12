package com.manpowergroup.kintai.attendance.domain.service.att;

import com.manpowergroup.kintai.attendance.domain.entity.att.AttRequest;
import com.manpowergroup.kintai.attendance.domain.repository.att.AttRequestRepository;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class TimesheetEditLockPolicy {

    private final AttRequestRepository requestRepository;

    public Map<LocalDate, AttRequest> findLocks(
            Long employeeId, Long companyId, LocalDate startDate, LocalDate endDate) {
        Map<LocalDate, AttRequest> locks = new LinkedHashMap<>();
        var requests = requestRepository.listByEmployee(employeeId).stream()
                .filter(request -> Objects.equals(request.getCompanyId(), companyId))
                .toList();

        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            LocalDate current = date;
            requests.stream()
                    .filter(request -> request.locksTimesheetOn(current))
                    .findFirst()
                    .ifPresent(request -> locks.put(current, request));
            date = date.plusDays(1);
        }
        return locks;
    }

    public void ensureEditable(Long employeeId, Long companyId, LocalDate workDate) {
        if (findLocks(employeeId, companyId, workDate, workDate).containsKey(workDate)) {
            throw BizException.withDetail(
                    ErrorCode.CONFLICT,
                    "timesheet date is locked by an active leave request");
        }
    }
}
