package com.manpowergroup.kintai.system.application.service.auth;

import com.manpowergroup.kintai.common.dto.auth.CurrentUserResponse;
import com.manpowergroup.kintai.common.dto.auth.LoginRequest;
import com.manpowergroup.kintai.common.dto.auth.LoginResponse;

// 認証サービス（アプリケーション層）
public interface AuthService {

    /**
     * 認証情報を検証し、ログイン処理を実行する。
     *
     * @param request 処理対象のログインリクエスト
     * @return Loginレスポンス
     */
    LoginResponse login(LoginRequest request);

    /**
     * ログイン中のユーザー情報を取得する。
     *
     * @param employeeId 対象の社員ID
     * @param accountId 対象の社員アカウントID
     * @return CurrentUserレスポンス
     */
    CurrentUserResponse me(Long employeeId, Long accountId);
}
