package com.manpowergroup.kintai.system.application.query.manager;

import com.manpowergroup.kintai.common.enums.Status;

// 配下従業員検索クエリ（アプリケーション層・読み取り側入力）
public record SubordinateQuery(
        Long managerId,      // 管理者ID（必須）
        String keyword,      // 氏名・社員コードの部分一致
        Long nodeId,         // 所属ノードで絞り込み
        Long gradeId,        // 職級で絞り込み
        Status status        // 在職状態で絞り込み

) {
}