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

    /**
     * 指定期間の勤怠編集を制限する申請を日付ごとに取得する。
     *
     * @param employeeId 対象の社員ID
     * @param companyId 対象の会社ID
     * @param startDate 対象期間の開始日
     * @param endDate 対象期間の終了日
     * @return 勤務日をキーとする勤怠編集制限申請のマップ。該当しない場合は空のマップ
     */
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

    /**
     * 指定勤務日の勤怠記録が編集可能か検証する。
     *
     * @param employeeId 対象の社員ID
     * @param companyId 対象の会社ID
     * @param workDate 対象の勤務日
     */
    public void ensureEditable(Long employeeId, Long companyId, LocalDate workDate) {
        if (findLocks(employeeId, companyId, workDate, workDate).containsKey(workDate)) {
            throw BizException.withDetail(
                    ErrorCode.CONFLICT,
                    "timesheet date is locked by an active leave request");
        }
    }
}
