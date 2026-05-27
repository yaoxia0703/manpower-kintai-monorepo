package com.manpowergroup.kintai.common.exception;

// エラーコードの基底インターフェース
public interface BaseErrorCode {
    int code();
    String messageKey();
}