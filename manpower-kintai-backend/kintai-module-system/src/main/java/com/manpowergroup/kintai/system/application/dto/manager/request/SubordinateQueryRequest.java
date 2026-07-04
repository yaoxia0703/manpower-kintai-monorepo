package com.manpowergroup.kintai.system.application.dto.manager.request;

import com.manpowergroup.kintai.common.enums.Status;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

// 配下従業員検索リクエスト（フロント入力）
@Data
public class SubordinateQueryRequest {

    private String keyword;   // 氏名・社員コードの部分一致（任意）
    private Long nodeId;      // 所属ノードで絞り込み（任意）
    private Long gradeId;     // 職級で絞り込み（任意）
    private Status status;    // 在職状態で絞り込み（任意）

    @Min(1)
    private int page = 1;
    @Min(1) @Max(100)
    private int size = 10;
}