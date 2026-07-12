package com.manpowergroup.kintai.admin.controller.wf;

import com.manpowergroup.kintai.attendance.application.command.wf.ApprovalRuleCreateCommand;
import com.manpowergroup.kintai.attendance.application.command.wf.ApprovalRuleUpdateCommand;
import com.manpowergroup.kintai.attendance.application.dto.wf.request.ApprovalRuleCreateRequest;
import com.manpowergroup.kintai.attendance.application.dto.wf.request.ApprovalRuleUpdateRequest;
import com.manpowergroup.kintai.attendance.application.dto.wf.response.ApprovalRuleResponse;
import com.manpowergroup.kintai.attendance.application.service.wf.WfApprovalRuleService;
import com.manpowergroup.kintai.common.result.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/wf/approval-rules")
@RequiredArgsConstructor
public class AdminApprovalRuleController {

    private final WfApprovalRuleService service;

    @GetMapping
    public Result<List<ApprovalRuleResponse>> list(@RequestParam Long companyId) {
        return Result.ok(service.listByCompany(companyId).stream()
                .map(ApprovalRuleResponse::from)
                .toList());
    }

    @GetMapping("/{ruleId}")
    public Result<ApprovalRuleResponse> getById(@PathVariable Long ruleId) {
        return Result.ok(ApprovalRuleResponse.from(service.getById(ruleId)));
    }

    @PostMapping
    public Result<ApprovalRuleResponse> create(
            @RequestBody @Valid ApprovalRuleCreateRequest request) {
        return Result.ok(ApprovalRuleResponse.from(service.create(
                new ApprovalRuleCreateCommand(
                        request.companyId(), request.requestType(), request.stopCondition(),
                        request.stopGradeLevel(), request.stopDeptFunc(),
                        request.amountThreshold(), request.sort(), request.status()))));
    }

    @PutMapping("/{ruleId}")
    public Result<ApprovalRuleResponse> update(
            @PathVariable Long ruleId,
            @RequestBody @Valid ApprovalRuleUpdateRequest request) {
        return Result.ok(ApprovalRuleResponse.from(service.update(ruleId,
                new ApprovalRuleUpdateCommand(
                        request.requestType(), request.stopCondition(),
                        request.stopGradeLevel(), request.stopDeptFunc(),
                        request.amountThreshold(), request.sort()))));
    }

    @PutMapping("/{ruleId}/enable")
    public Result<Void> enable(@PathVariable Long ruleId) {
        service.enable(ruleId);
        return Result.ok();
    }

    @PutMapping("/{ruleId}/disable")
    public Result<Void> disable(@PathVariable Long ruleId) {
        service.disable(ruleId);
        return Result.ok();
    }

    @DeleteMapping("/{ruleId}")
    public Result<Void> remove(@PathVariable Long ruleId) {
        service.remove(ruleId);
        return Result.ok();
    }
}
