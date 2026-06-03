package com.manpowergroup.kintai.system.application.dto.org.request;

import com.manpowergroup.kintai.common.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GradeCreateRequest {

    @NotNull(message = "会社は必須です")
    private Long companyId;

    @NotBlank(message = "職級名は必須です")
    @Size(max = 100, message = "職級名は100文字以内で入力してください")
    private String name;

    @NotBlank(message = "職級コードは必須です")
    @Size(max = 50, message = "職級コードは50文字以内で入力してください")
    private String code;

    @NotBlank(message = "職級レベルは必須です")
    @Size(max = 50, message = "職級レベルは50文字以内で入力してください")
    private String gradeLevel;

    @NotNull(message = "表示順は必須です")
    private Integer sort;

    private Status status;
}
