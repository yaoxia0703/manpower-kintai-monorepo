package com.manpowergroup.kintai.system.application.dto.sys.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Role作成リクエスト")
public class RoleCreateRequest {

    @Schema(description = "会社ID")
    private Long companyId;

    @NotBlank(message = "ロールコードは必須です")
    @Size(max = 50, message = "ロールコードは50文字以内で入力してください")
    @Schema(description = "コード")
    private String code;

    @NotBlank(message = "ロール名は必須です")
    @Size(max = 100, message = "ロール名は100文字以内で入力してください")
    @Schema(description = "名称")
    private String name;

    @Size(max = 255, message = "備考は255文字以内で入力してください")
    @Schema(description = "備考")
    private String remark;

    @NotNull(message = "表示順は必須です")
    @Schema(description = "表示順")
    private Integer sort;
}
