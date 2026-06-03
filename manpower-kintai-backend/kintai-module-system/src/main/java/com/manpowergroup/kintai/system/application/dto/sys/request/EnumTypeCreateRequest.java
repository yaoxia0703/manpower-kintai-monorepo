package com.manpowergroup.kintai.system.application.dto.sys.request;

import com.manpowergroup.kintai.common.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EnumTypeCreateRequest {

    @NotBlank(message = "列挙型コードは必須です")
    @Size(max = 50, message = "列挙型コードは50文字以内で入力してください")
    private String code;

    @NotBlank(message = "列挙型名は必須です")
    @Size(max = 100, message = "列挙型名は100文字以内で入力してください")
    private String name;

    @Size(max = 255, message = "備考は255文字以内で入力してください")
    private String remark;

    @NotNull(message = "表示順は必須です")
    private Integer sort;

    private Status status;
}
