package com.manpowergroup.kintai.system.application.service.emp;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manpowergroup.kintai.system.application.command.emp.EmployeePositionCreateCommand;
import com.manpowergroup.kintai.system.application.command.emp.EmployeePositionUpdateCommand;
import com.manpowergroup.kintai.system.domain.entity.emp.EmpEmployeePosition;

import java.util.List;

// 社員職位サービス（アプリケーション層）
public interface EmpEmployeePositionService extends IService<EmpEmployeePosition> {

    // IDで社員職位を取得（存在しない場合は例外）
    EmpEmployeePosition getById(Long id);

    // 社員IDで有効な職位一覧を取得
    List<EmpEmployeePosition> listActiveByEmployee(Long employeeId);

    // 社員IDで全職位を取得（履歴含む）
    List<EmpEmployeePosition> listAllByEmployee(Long employeeId);

    // 社員の主務職位を取得
    EmpEmployeePosition getPrimaryByEmployee(Long employeeId);

    // 社員職位を新規追加（主務・兼務）
    EmpEmployeePosition create(EmployeePositionCreateCommand command);

    // 社員職位を更新
    EmpEmployeePosition update(Long id, EmployeePositionUpdateCommand command);

    // 社員職位を終了（離任日を設定）
    void terminate(Long id);

    // 社員職位を削除（論理削除）
    void remove(Long id);
}

