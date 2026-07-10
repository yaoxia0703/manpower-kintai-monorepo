package com.manpowergroup.kintai.system.application.dto.org.request;

import io.swagger.v3.oas.annotations.media.Schema;
import com.manpowergroup.kintai.common.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Node作成リクエスト")
public class NodeCreateRequest {

    @NotNull(message = "会社は必須です")
    @Schema(description = "会社ID")
    private Long companyId;

    @Schema(description = "親ID")
    private Long parentId;

    @Schema(description = "ノード責任者社員ID")
    private Long managerId;

    @NotBlank(message = "組織名は必須です")
    @Size(max = 100, message = "組織名は100文字以内で入力してください")
    @Schema(description = "名称")
    private String name;

    @NotBlank(message = "組織種別は必須です")
    @Size(max = 50, message = "組織種別は50文字以内で入力してください")
    @Schema(description = "タイプコード")
    private String typeCode;

    @Size(max = 50, message = "部門機能は50文字以内で入力してください")
    @Schema(description = "部門機能区分")
    private String deptFunction;

    @NotBlank(message = "組織コードは必須です")
    @Size(max = 50, message = "組織コードは50文字以内で入力してください")
    @Schema(description = "コード")
    private String code;

    @NotNull(message = "階層レベルは必須です")
    @Schema(description = "階層レベル")
    private Integer level;

    @NotNull(message = "表示順は必須です")
    @Schema(description = "表示順")
    private Integer sort;

    @Schema(description = "ステータス")
    private Status status;
}
