package com.manpowergroup.kintai.attendance.application.service.impl.att;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.manpowergroup.kintai.attendance.application.dto.timesheet.response.TimesheetDayResponse;
import com.manpowergroup.kintai.attendance.application.dto.timesheet.response.TimesheetMonthResponse;
import com.manpowergroup.kintai.attendance.application.query.timesheet.TimesheetMonthQuery;
import com.manpowergroup.kintai.attendance.application.service.att.AttTimesheetQueryService;
import com.manpowergroup.kintai.attendance.domain.entity.att.AttRecord;
import com.manpowergroup.kintai.attendance.infrastructure.mapper.att.AttRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttTimesheetQueryServiceImpl implements AttTimesheetQueryService {

    private static final String[] JP_WEEKDAYS = {
            "\u65e5", "\u6708", "\u706b", "\u6c34", "\u6728", "\u91d1", "\u571f"
    };

    private final AttRecordMapper recordMapper;

    @Override
    public TimesheetMonthResponse getMonthlyTimesheet(TimesheetMonthQuery query) {
        LocalDate start = LocalDate.of(query.year(), query.month(), 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        Map<LocalDate, AttRecord> recordMap = recordMapper.selectList(
                new LambdaQueryWrapper<AttRecord>()
                        .eq(AttRecord::getEmployeeId, query.employeeId())
                        .eq(AttRecord::getCompanyId, query.companyId())
                        .between(AttRecord::getWorkDate, start, end)
        ).stream().collect(Collectors.toMap(AttRecord::getWorkDate, record -> record));

        List<TimesheetDayResponse> days = new ArrayList<>();
        LocalDate cursor = start;
        int workDays = 0;
        int totalWork = 0;
        int totalOvertime = 0;

        while (!cursor.isAfter(end)) {
            TimesheetDayResponse day = buildDayResponse(cursor, recordMap.get(cursor));
            days.add(day);

            if (day.getWorkMinutes() != null && day.getWorkMinutes() > 0) {
                workDays++;
                totalWork += day.getWorkMinutes();
                totalOvertime += day.getOvertimeMinutes() == null ? 0 : day.getOvertimeMinutes();
            }

            cursor = cursor.plusDays(1);
        }

        return new TimesheetMonthResponse()
                .setYear(query.year())
                .setMonth(query.month())
                .setDays(days)
                .setWorkDays(workDays)
                .setTotalWorkMinutes(totalWork)
                .setTotalOvertimeMinutes(totalOvertime);
    }

    private TimesheetDayResponse buildDayResponse(LocalDate date, AttRecord record) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        boolean weekend = dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;

        TimesheetDayResponse dto = new TimesheetDayResponse()
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
}
