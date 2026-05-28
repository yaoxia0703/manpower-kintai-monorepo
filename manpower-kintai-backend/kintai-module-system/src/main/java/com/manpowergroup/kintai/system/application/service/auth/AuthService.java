package com.manpowergroup.kintai.system.application.service.auth;

import com.manpowergroup.kintai.common.dto.auth.CurrentUserResponse;
import com.manpowergroup.kintai.common.dto.auth.LoginRequest;
import com.manpowergroup.kintai.common.dto.auth.LoginResponse;

// 認証サービス（アプリケーション層）
public interface AuthService {

    // ログイン認証：社員メールアドレス＋パスワードを検証しJWTトークンを返す
    LoginResponse login(LoginRequest request);

    // 現在ログイン中のユーザー情報・権限・メニューを取得
    CurrentUserResponse me(Long employeeId, Long accountId);
}
