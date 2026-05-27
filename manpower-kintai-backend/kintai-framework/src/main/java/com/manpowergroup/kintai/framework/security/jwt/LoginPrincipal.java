package com.manpowergroup.kintai.framework.security.jwt;

// JWT認証後にSecurityContextに格納する最小プリンシパル
public record LoginPrincipal(Long employeeId , Long accountId) {}