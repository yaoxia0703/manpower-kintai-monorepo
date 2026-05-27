package com.manpowergroup.kintai.system.application.service.auth;

import com.manpowergroup.kintai.common.dto.auth.LoginRequest;
import com.manpowergroup.kintai.common.dto.auth.LoginResponse;

// 認証サービス（アプリケーション層）
public interface AuthService {

    // ログイン認証：社員メールアドレス＋パスワードを検証しJWTトークンを返す
    LoginResponse login(LoginRequest request);
}
