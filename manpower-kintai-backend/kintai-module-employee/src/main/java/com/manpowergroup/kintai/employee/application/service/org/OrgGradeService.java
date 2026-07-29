package com.manpowergroup.kintai.employee.application.service.org;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.employee.application.command.org.GradeCreateCommand;
import com.manpowergroup.kintai.employee.application.command.org.GradeUpdateCommand;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgGrade;

import java.util.List;

// 職級マスタサービス（アプリケーション層）
public interface OrgGradeService  {

    /**
     * IDに対応する職級を取得する。
     *
     * @param id 対象の職級ID
     * @return 取得した職級
     */
    OrgGrade getById(Long id);

    /**
     * 指定された検索条件で職級をページング取得する。
     *
     * @param companyId 対象の会社ID
     * @param request 処理対象のページング条件
     * @return ページングされた職級一覧
     */
    PageResult<OrgGrade> pageByCompany(Long companyId, PageRequest request);

    /**
     * 会社に一致する職級を一覧取得する。
     *
     * @param companyId 対象の会社ID
     * @return 職級一覧。該当しない場合は空リスト
     */
    List<OrgGrade> listByCompany(Long companyId);

    /**
     * 職級レベルに一致する職級を一覧取得する。
     *
     * @param gradeLevel 職級レベル
     * @return 職級一覧。該当しない場合は空リスト
     */
    List<OrgGrade> listByGradeLevel(String gradeLevel);

    /**
     * 職級を新規作成する。
     *
     * @param command 処理対象の職級作成コマンド
     * @return 保存した職級
     */
    OrgGrade create(GradeCreateCommand command);

    /**
     * 職級を更新する。
     *
     * @param id 対象の職級ID
     * @param command 処理対象の職級更新コマンド
     * @return 更新した職級
     */
    OrgGrade update(Long id, GradeUpdateCommand command);

    /**
     * 職級を有効化する。
     *
     * @param id 対象の職級ID
     */
    void enable(Long id);

    /**
     * 職級を無効化する。
     *
     * @param id 対象の職級ID
     */
    void disable(Long id);

    /**
     * 指定IDの職級を削除する。
     *
     * @param id 対象の職級ID
     */
    void remove(Long id);
}

