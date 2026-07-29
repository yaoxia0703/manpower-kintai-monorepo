package com.manpowergroup.kintai.employee.domain.repository.org;

import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgNode;

import java.util.List;
import java.util.Optional;

public interface OrgNodeRepository {

    /**
     * IDに対応する組織ノードを取得する。
     *
     * @param id 対象の組織ノードID
     * @return 対象が存在する場合は組織ノードを含むOptional、存在しない場合はOptional.empty()
     */
    Optional<OrgNode> findById(Long id);

    /**
     * 会社およびコードに一致する組織ノードが存在するか判定する。
     *
     * @param companyId 対象の会社ID
     * @param code 対象コード
     * @return 条件を満たす場合はtrue、それ以外はfalse
     */
    boolean existsByCompanyAndCode(Long companyId, String code);

    /**
     * 指定IDを除き、同一条件の組織ノードが存在するか判定する。
     *
     * @param companyId 対象の会社ID
     * @param code 対象コード
     * @param excludeId 重複確認から除外するID
     * @return 条件を満たす場合はtrue、それ以外はfalse
     */
    boolean existsByCompanyAndCodeExcludingId(Long companyId, String code, Long excludeId);

    /**
     * 指定会社の組織ノードをページング取得する。
     *
     * @param companyId 対象の会社ID
     * @param page ページ番号
     * @param size 1ページあたりの取得件数
     * @return ページングされた組織ノード一覧
     */
    PageResult<OrgNode> findPageByCompany(Long companyId, int page, int size);

    /**
     * 指定された条件に一致する有効な組織ノードを一覧取得する。
     *
     * @param companyId 対象の会社ID
     * @return 組織ノード一覧。該当しない場合は空リスト
     */
    List<OrgNode> listEnabledByCompany(Long companyId);

    /**
     * 組織ノードを保存する。
     *
     * @param node 処理対象の組織ノード
     * @return 保存した組織ノード
     */
    OrgNode save(OrgNode node);

    /**
     * 組織ノードを更新する。
     *
     * @param node 処理対象の組織ノード
     * @return 更新した組織ノード
     */
    OrgNode update(OrgNode node);

    /**
     * 指定IDの組織ノードを削除する。
     *
     * @param id 対象の組織ノードID
     */
    void deleteById(Long id);

    /**
     * 指定会社の組織ノードを一覧取得する。
     *
     * @param companyId 対象の会社ID
     * @return 組織ノード一覧。該当しない場合は空リスト
     */
    List<OrgNode> findListByCompany(Long companyId);

    /**
     * 指定会社・組織ノード条件に一致する組織ノードの件数を取得する。
     *
     * @param nodeId 対象の組織ノードID
     * @param companyId 対象の会社ID
     * @return 該当件数
     */
    Long selectCountByNodeAndComoany(Long nodeId,Long companyId);
}
