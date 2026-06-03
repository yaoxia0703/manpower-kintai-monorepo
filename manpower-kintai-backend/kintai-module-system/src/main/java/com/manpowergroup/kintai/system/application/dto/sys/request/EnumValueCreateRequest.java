package com.manpowergroup.kintai.system.application.dto.sys.request;

import com.manpowergroup.kintai.common.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EnumValueCreateRequest {

    @NotBlank(message = "列挙型コードは必須です")
    @Size(max = 50, message = "列挙型コードは50文字以内で入力してください")
    private String enumTypeCode;

    @NotBlank(message = "列挙値コードは必須です")
    @Size(max = 50, message = "列挙値コードは50文字以内で入力してください")
    private String code;

    @NotNull(message = "表示順は必須です")
    private Integer sort;

    private Status status;
}
