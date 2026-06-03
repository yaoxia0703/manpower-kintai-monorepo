package com.manpowergroup.kintai.system.application.dto.sys.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RoleCreateRequest {

    private Long companyId;

    @NotBlank(message = "ロールコードは必須です")
    @Size(max = 50, message = "ロールコードは50文字以内で入力してください")
    private String code;

    @NotBlank(message = "ロール名は必須です")
    @Size(max = 100, message = "ロール名は100文字以内で入力してください")
    private String name;

    @Size(max = 255, message = "備考は255文字以内で入力してください")
    private String remark;

    @NotNull(message = "表示順は必須です")
    private Integer sort;
}
