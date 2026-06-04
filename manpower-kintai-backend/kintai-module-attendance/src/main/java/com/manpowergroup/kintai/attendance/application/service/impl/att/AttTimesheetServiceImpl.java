package com.manpowergroup.kintai.attendance.application.service.impl.att;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.manpowergroup.kintai.attendance.application.dto.TimesheetDayDTO;
import com.manpowergroup.kintai.attendance.application.dto.TimesheetMonthResponse;
import com.manpowergroup.kintai.attendance.application.dto.TimesheetSaveRequest;
import com.manpowergroup.kintai.attendance.application.service.att.AttTimesheetService;
import com.manpowergroup.kintai.attendance.domain.entity.att.AttRecord;
import com.manpowergroup.kintai.attendance.domain.enums.AttRecordStatus;
import com.manpowergroup.kintai.attendance.infrastructure.mapper.att.AttRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttTimesheetServiceImpl implements AttTimesheetService {

    private static final String[] JP_WEEKDAYS = {"日", "月", "火", "水", "木", "金", "土"};

    private final AttRecordMapper recordMapper;

    @Override
    public TimesheetMonthResponse getMonthlyTimesheet(Long employeeId, Long companyId, int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        Map<LocalDate, AttRecord> recordMap = recordMapper.selectList(
                new LambdaQueryWrapper<AttRecord>()
                        .eq(AttRecord::getEmployeeId, employeeId)
                        .eq(AttRecord::getCompanyId, companyId)
                        .between(AttRecord::getWorkDate, start, end)
        ).stream().collect(Collectors.toMap(AttRecord::getWorkDate, record -> record));

        List<TimesheetDayDTO> days = new ArrayList<>();
        LocalDate cursor = start;
        int workDays = 0;
        int totalWork = 0;
        int totalOvertime = 0;

        while (!cursor.isAfter(end)) {
            TimesheetDayDTO day = buildDayDTO(cursor, recordMap.get(cursor));
            days.add(day);

            if (day.getWorkMinutes() != null && day.getWorkMinutes() > 0) {
                workDays++;
                totalWork += day.getWorkMinutes();
                totalOvertime += day.getOvertimeMinutes() == null ? 0 : day.getOvertimeMinutes();
            }

            cursor = cursor.plusDays(1);
        }

        return new TimesheetMonthResponse()
                .setYear(year)
                .setMonth(month)
                .setDays(days)
                .setWorkDays(workDays)
                .setTotalWorkMinutes(totalWork)
                .setTotalOvertimeMinutes(totalOvertime);
    }

    private TimesheetDayDTO buildDayDTO(LocalDate date, AttRecord record) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        boolean weekend = dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;

        TimesheetDayDTO dto = new TimesheetDayDTO()
                .setWorkDate(date)
                .setDayOfWeek(JP_WEEKDAYS[dayOfWeek.getValue() % 7])
                .setWeekend(weekend)
                .setHoliday(false);

        if (record == null) {
            return dto;
        }

        dto.setRecordId(record.getId())
                .setAttendanceType(record.getAttendanceType())
                .setClockIn(record.getClockIn())
                .setClockOut(record.getClockOut())
                .setWorkMinutes(record.getWorkMinutes())
                .setOvertimeMinutes(record.getOvertimeMinutes())
                .setRemark(record.getRemark())
                .setStatus(record.getStatus())
                .setBreakMinutes(record.calculateBreakMinutes());

        return dto;
    }

    @Override
    @Transactional
    public void saveRecord(Long employeeId, Long companyId, TimesheetSaveRequest request) {
        AttRecord existing = recordMapper.selectOne(
                new LambdaQueryWrapper<AttRecord>()
                        .eq(AttRecord::getEmployeeId, employeeId)
                        .eq(AttRecord::getCompanyId, companyId)
                        .eq(AttRecord::getWorkDate, request.getWorkDate())
        );

        if (existing != null) {
            existing.setAttendanceType(request.getAttendanceType())
                    .setClockIn(request.getClockIn())
                    .setClockOut(request.getClockOut())
                    .setRemark(request.getRemark())
                    .setUpdatedBy(employeeId);
            existing.recalculate(request.getBreakMinutes());
            recordMapper.updateById(existing);
            return;
        }

        AttRecord record = new AttRecord()
                .setEmployeeId(employeeId)
                .setCompanyId(companyId)
                .setWorkDate(request.getWorkDate())
                .setAttendanceType(request.getAttendanceType())
                .setClockIn(request.getClockIn())
                .setClockOut(request.getClockOut())
                .setRemark(request.getRemark())
                .setStatus(AttRecordStatus.DRAFT)
                .setCreatedBy(employeeId)
                .setUpdatedBy(employeeId);
        record.recalculate(request.getBreakMinutes());
        recordMapper.insert(record);
    }

    @Override
    @Transactional
    public void deleteRecord(Long employeeId, Long recordId) {
        AttRecord record = recordMapper.selectOne(
                new LambdaQueryWrapper<AttRecord>()
                        .eq(AttRecord::getId, recordId)
                        .eq(AttRecord::getEmployeeId, employeeId)
        );
        if (record != null) {
            recordMapper.deleteById(recordId);
        }
    }
}
