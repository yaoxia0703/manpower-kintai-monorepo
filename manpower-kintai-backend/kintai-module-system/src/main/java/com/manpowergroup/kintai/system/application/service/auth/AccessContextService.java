package com.manpowergroup.kintai.system.application.service.auth;

import com.manpowergroup.kintai.common.dto.auth.AccessContext;

public interface AccessContextService {

    /**
     * ログイン中のユーザーのアクセスコンテキストを読み込む。
     *
     * @param employeeId 対象の社員ID
     * @param accountId 対象の社員アカウントID
     * @return 取得したアクセスコンテキスト
     */
    AccessContext load(Long employeeId, Long accountId);
}
