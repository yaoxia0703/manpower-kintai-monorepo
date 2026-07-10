package com.manpowergroup.kintai.system.application.dto.sys.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Permission作成リクエスト")
public class PermissionCreateRequest {

    @NotNull(message = "所属メニューは必須です")
    @Schema(description = "メニューID")
    private Long menuId;

    @NotBlank(message = "権限コードは必須です")
    @Size(max = 100, message = "権限コードは100文字以内で入力してください")
    @Schema(description = "コード")
    private String code;

    @NotBlank(message = "権限名は必須です")
    @Size(max = 100, message = "権限名は100文字以内で入力してください")
    @Schema(description = "名称")
    private String name;

    @NotBlank(message = "HTTPメソッドは必須です")
    @Size(max = 20, message = "HTTPメソッドは20文字以内で入力してください")
    @Schema(description = "HTTPメソッド")
    private String method;

    @NotBlank(message = "APIパスは必須です")
    @Size(max = 255, message = "APIパスは255文字以内で入力してください")
    @Schema(description = "パス")
    private String path;

    @Size(max = 255, message = "備考は255文字以内で入力してください")
    @Schema(description = "備考")
    private String remark;

    @NotNull(message = "表示順は必須です")
    @Schema(description = "表示順")
    private Integer sort;
}
