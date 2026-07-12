package com.manpowergroup.kintai.attendance.application.service.att;

import com.manpowergroup.kintai.attendance.application.command.att.AttRequestCreateCommand;
import com.manpowergroup.kintai.attendance.application.command.att.AttRequestUpdateCommand;
import com.manpowergroup.kintai.attendance.domain.entity.att.AttRequest;

import java.util.List;

public interface AttRequestService {

    AttRequest create(AttRequestCreateCommand command);

    AttRequest update(AttRequestUpdateCommand command);

    void cancel(Long employeeId, Long requestId);

    List<AttRequest> listByEmployee(Long employeeId);
}
