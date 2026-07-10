package com.manpowergroup.kintai.system.application.dto.manager.request;

import io.swagger.v3.oas.annotations.media.Schema;
import com.manpowergroup.kintai.common.enums.Status;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

// 配下従業員検索リクエスト（フロント入力）
@Data
@Schema(description = "Subordinate検索リクエスト")
public class SubordinateQueryRequest {

    @Schema(description = "検索キーワード")
    private String keyword;   // 氏名・社員コードの部分一致（任意）
    @Schema(description = "組織ノードID")
    private Long nodeId;      // 所属ノードで絞り込み（任意）
    @Schema(description = "職級ID")
    private Long gradeId;     // 職級で絞り込み（任意）
    @Schema(description = "ステータス")
    private Status status;    // 在職状態で絞り込み（任意）

    @Min(1)
    @Schema(description = "ページ番号")
    private int page = 1;
    @Min(1) @Max(100)
    @Schema(description = "1ページあたりの件数")
    private int size = 10;
}