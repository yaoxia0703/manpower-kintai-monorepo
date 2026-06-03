package com.manpowergroup.kintai.system.application.dto.org.request;

import com.manpowergroup.kintai.common.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NodeCreateRequest {

    @NotNull(message = "会社は必須です")
    private Long companyId;

    private Long parentId;

    private Long managerId;

    @NotBlank(message = "組織名は必須です")
    @Size(max = 100, message = "組織名は100文字以内で入力してください")
    private String name;

    @NotBlank(message = "組織種別は必須です")
    @Size(max = 50, message = "組織種別は50文字以内で入力してください")
    private String typeCode;

    @Size(max = 50, message = "部門機能は50文字以内で入力してください")
    private String deptFunction;

    @NotBlank(message = "組織コードは必須です")
    @Size(max = 50, message = "組織コードは50文字以内で入力してください")
    private String code;

    @NotNull(message = "階層レベルは必須です")
    private Integer level;

    @NotNull(message = "表示順は必須です")
    private Integer sort;

    private Status status;
}
