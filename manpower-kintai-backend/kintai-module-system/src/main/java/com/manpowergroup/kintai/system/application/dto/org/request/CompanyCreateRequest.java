package com.manpowergroup.kintai.system.application.dto.org.request;

import com.manpowergroup.kintai.common.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CompanyCreateRequest {

    private Long parentId;

    @NotBlank(message = "会社名は必須です")
    @Size(max = 100, message = "会社名は100文字以内で入力してください")
    private String name;

    @NotBlank(message = "会社コードは必須です")
    @Size(max = 50, message = "会社コードは50文字以内で入力してください")
    private String companyCode;

    @NotNull(message = "階層レベルは必須です")
    private Integer level;

    @NotNull(message = "表示順は必須です")
    private Integer sort;

    private Status status;
}
