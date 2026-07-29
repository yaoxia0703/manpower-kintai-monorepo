package com.manpowergroup.kintai.employee.domain.repository.org;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgCompany;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgNode;

import java.util.List;

public interface OrgCompanyRepository {

    /**
     * 会社を一覧取得する。
     *
     * @return 会社一覧。該当しない場合は空リスト
     */
    List<OrgCompany> findList();

    /**
     * 指定された条件に一致する会社の件数を取得する。
     *
     * @param companyId 対象の会社ID
     * @return 該当件数
     */
    Long selectCount(Long companyId);

    /**
     * IDに対応する会社を取得する。
     *
     * @param id 対象の会社ID
     * @return 取得した会社
     */
    OrgCompany findById(Long id);

    /**
     * 指定された検索条件で会社をページング取得する。
     *
     * @param page ページ番号
     * @param size 1ページあたりの取得件数
     * @return ページングされた会社一覧
     */
    PageResult<OrgCompany> findPage(int page, int size);

    /**
     * 指定された条件に一致する有効な会社を一覧取得する。
     *
     * @return 会社一覧。該当しない場合は空リスト
     */
    List<OrgCompany> listEnabled();

    /**
     * 指定会社コードに一致する会社の件数を取得する。
     *
     * @param companyCode 会社コード
     * @return 該当件数
     */
    Long selecCountByCompanyCode(String companyCode);

    /**
     * 指定IDを除き、同一条件の会社が存在するか判定する。
     *
     * @param companyCode 会社コード
     * @param id 対象の会社ID
     * @return 条件を満たす場合はtrue、それ以外はfalse
     */
    boolean existsByCompanyAndCodeExcludingId(String companyCode, Long id);

    /**
     * 会社を保存する。
     *
     * @param orgCompany 保存対象の会社
     */
    void save(OrgCompany orgCompany);

    /**
     * 会社を更新する。
     *
     * @param orgCompany 保存対象の会社
     */
    void updateById(OrgCompany orgCompany);

    /**
     * 指定IDの会社を削除する。
     *
     * @param id 対象の会社ID
     */
    void deleteById(Long id);

}
