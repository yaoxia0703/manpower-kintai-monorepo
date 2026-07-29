package com.manpowergroup.kintai.employee.domain.repository.org;

import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgGrade;

import java.util.List;

public interface OrgGradeRepository {

    /**
     * 指定会社の職級を一覧取得する。
     *
     * @param companyId 対象の会社ID
     * @return 職級一覧。該当しない場合は空リスト
     */
    List<OrgGrade> findListByCompany(Long companyId);

    /**
     * 指定会社・職級条件に一致する職級の件数を取得する。
     *
     * @param companyId 対象の会社ID
     * @param gradeId 対象の職級ID
     * @return 該当件数
     */
    Long selectCountByCompanyAndGrade(Long companyId, Long gradeId);

    /**
     * IDに対応する職級を取得する。
     *
     * @param id 対象の職級ID
     * @return 取得した職級
     */
    OrgGrade getById(Long id);

    /**
     * 指定会社の職級をページング取得する。
     *
     * @param companyId 対象の会社ID
     * @param page ページ番号
     * @param size 1ページあたりの取得件数
     * @return ページングされた職級一覧
     */
    PageResult<OrgGrade> findPageByCompany(Long companyId, int page, int size);

    /**
     * 会社に対応する職級を取得する。
     *
     * @param companyId 対象の会社ID
     * @return 職級一覧。該当しない場合は空リスト
     */
    List<OrgGrade> findByCompany(Long companyId);

    /**
     * 職級レベルに対応する職級を取得する。
     *
     * @param gradeLevel 職級レベル
     * @return 職級一覧。該当しない場合は空リスト
     */
    List<OrgGrade> findByGradeLevel(String gradeLevel);

    /**
     * 指定会社・職級コードに一致する職級の件数を取得する。
     *
     * @param code 対象コード
     * @param companyId 対象の会社ID
     * @return 条件を満たす場合はtrue、それ以外はfalse
     */
    boolean selectCountByCodeAndCompany(String code, Long companyId);

    /**
     * 職級を保存する。
     *
     * @param grade 処理対象の職級
     */
    void save(OrgGrade grade);

    /**
     * 職級を更新する。
     *
     * @param grade 処理対象の職級
     */
    void updateById(OrgGrade grade);

    /**
     * 指定IDの職級を削除する。
     *
     * @param id 対象の職級ID
     */
    void deleteById(Long id);

    /**
     * 指定IDを除き、同一条件の職級が存在するか判定する。
     *
     * @param id 対象の職級ID
     * @param companyId 対象の会社ID
     * @param code 対象コード
     * @return 条件を満たす場合はtrue、それ以外はfalse
     */
    boolean existsByCompanyAndCodeExcludingId(Long id, Long companyId, String code);

}
