package com.manpowergroup.kintai.attendance.domain.repository.att;

import com.manpowergroup.kintai.attendance.domain.entity.att.AttRequest;

import java.util.Optional;
import java.util.List;

public interface AttRequestRepository {

    /**
     * IDに対応する勤怠申請を取得する。
     *
     * @param requestId 対象の申請ID
     * @return 対象が存在する場合は勤怠申請を含むOptional、存在しない場合はOptional.empty()
     */
    Optional<AttRequest> findById(Long requestId);

    /**
     * IDおよび社員に対応する勤怠申請を取得する。
     *
     * @param requestId 対象の申請ID
     * @param employeeId 対象の社員ID
     * @return 対象が存在する場合は勤怠申請を含むOptional、存在しない場合はOptional.empty()
     */
    Optional<AttRequest> findByIdAndEmployee(Long requestId, Long employeeId);

    /**
     * 指定社員の勤怠申請を作成日時の降順で取得する。
     *
     * @param employeeId 対象の社員ID
     * @return 勤怠申請一覧。該当しない場合は空リスト
     */
    List<AttRequest> listByEmployee(Long employeeId);

    /**
     * 勤怠申請を保存する。
     *
     * @param request 処理対象の勤怠申請
     * @return 保存した勤怠申請
     */
    AttRequest save(AttRequest request);

    /**
     * 勤怠申請を更新する。
     *
     * @param request 処理対象の勤怠申請
     * @return 更新した勤怠申請
     */
    AttRequest update(AttRequest request);
}
