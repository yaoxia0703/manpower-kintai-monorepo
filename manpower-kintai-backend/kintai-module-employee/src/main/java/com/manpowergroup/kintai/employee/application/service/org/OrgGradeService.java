package com.manpowergroup.kintai.employee.application.service.org;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.employee.application.command.org.GradeCreateCommand;
import com.manpowergroup.kintai.employee.application.command.org.GradeUpdateCommand;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgGrade;

import java.util.List;

// 職級マスタサービス（アプリケーション層）
public interface OrgGradeService extends IService<OrgGrade> {

    // IDで職級を取得（存在しない場合は例外）
    OrgGrade getById(Long id);

    // 会社IDで職級一覧をページング取得
    PageResult<OrgGrade> pageByCompany(Long companyId, PageRequest request);

    // 会社IDで職級一覧を全取得
    List<OrgGrade> listByCompany(Long companyId);

    // 職級レベルで職級一覧を取得
    List<OrgGrade> listByGradeLevel(String gradeLevel);

    // 職級を新規作成
    OrgGrade create(GradeCreateCommand command);

    // 職級を更新
    OrgGrade update(Long id, GradeUpdateCommand command);

    // 職級を有効化
    void enable(Long id);

    // 職級を無効化
    void disable(Long id);

    // 職級を削除（論理削除）
    void remove(Long id);
}

