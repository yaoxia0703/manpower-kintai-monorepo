package com.manpowergroup.kintai.system.domain.repository.sys;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.system.domain.entity.sys.SysRole;

import java.util.Collection;
import java.util.List;

public interface SysRoleRepository {
    /**
     * 指定された条件に一致する有効なロールを一覧取得する。
     *
     * @param companyId 対象の会社ID
     * @return ロール一覧。該当しない場合は空リスト
     */
    List<SysRole> listEnabledByCompanyId(Long companyId);

    /**
     * 指定会社・ロールID一覧に一致するロールの件数を取得する。
     *
     * @param companyId 対象の会社ID
     * @param roleIds 対象のロールID一覧
     * @return 該当件数
     */
    Long selectCountBYCompanyIdAndRoleIds(Long companyId, Collection<Long> roleIds);

    /**
     * ID一覧およびコードに一致するロールが存在するか判定する。
     *
     * @param roleIds 対象のロールID一覧
     * @param roleCode ロールコード
     * @return 条件を満たす場合はtrue、それ以外はfalse
     */
    boolean existsEnabledByIdsAndCode(Collection<Long> roleIds, String roleCode);

    /**
     * IDに対応するロールを取得する。
     *
     * @param id 対象のロールID
     * @return 取得したロール
     */
    SysRole findById(Long id);

    /**
     * 指定された検索条件でロールをページング取得する。
     *
     * @param companyId 対象の会社ID
     * @param page ページ番号
     * @param size 1ページあたりの取得件数
     * @return ページングされたロール一覧
     */
    PageResult<SysRole> pageByCompany(Long companyId, int  page, int size);

    /**
     * 会社に一致するロールを一覧取得する。
     *
     * @param companyId 対象の会社ID
     * @return ロール一覧。該当しない場合は空リスト
     */
    List<SysRole> listByCompany(Long companyId);

    /**
     * 会社およびコードに一致するロールが存在するか判定する。
     *
     * @param companyId 対象の会社ID
     * @param roleCode ロールコード
     * @return 条件を満たす場合はtrue、それ以外はfalse
     */
    boolean existsByCompanyAndCode(Long companyId, String roleCode);

    /**
     * ロールを保存する。
     *
     * @param sysRole 保存対象のロール
     */
    void save (SysRole sysRole);

    /**
     * ロールを更新する。
     *
     * @param sysRole 保存対象のロール
     */
    void updateById (SysRole sysRole);

    /**
     * 指定IDのロールを削除する。
     *
     * @param id 対象のロールID
     */
    void deleteById (Long id);

    /**
     * 指定IDを除き、同一条件のロールが存在するか判定する。
     *
     * @param companyId 対象の会社ID
     * @param id 対象のロールID
     * @param code 対象コード
     * @return 条件を満たす場合はtrue、それ以外はfalse
     */
    boolean existsByCompanyAndCodeExcludingId(Long companyId, Long id,String code);

    /**
     * 指定IDの有効なロールを一覧取得する。
     *
     * @param ids 対象ID一覧
     * @return ロール一覧。該当しない場合は空リスト
     */
    List<SysRole> listEnadbledByIds(List<Long> ids);
}


