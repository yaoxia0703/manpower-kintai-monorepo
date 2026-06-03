package com.manpowergroup.kintai.system.application.dto.sys.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MenuCreateRequest {

    private Long parentId;

    @NotBlank(message = "メニュー名は必須です")
    @Size(max = 100, message = "メニュー名は100文字以内で入力してください")
    private String name;

    @NotBlank(message = "メニューコードは必須です")
    @Size(max = 100, message = "メニューコードは100文字以内で入力してください")
    private String code;

    @Size(max = 255, message = "パスは255文字以内で入力してください")
    private String path;

    @Size(max = 255, message = "コンポーネントは255文字以内で入力してください")
    private String component;

    @Size(max = 100, message = "アイコンは100文字以内で入力してください")
    private String icon;

    @NotNull(message = "メニュー種別は必須です")
    @Min(value = 1, message = "メニュー種別は1から3の範囲で指定してください")
    @Max(value = 3, message = "メニュー種別は1から3の範囲で指定してください")
    private Integer type;

    @NotNull(message = "表示順は必須です")
    private Integer sort;

    @Min(value = 0, message = "表示設定は0または1で指定してください")
    @Max(value = 1, message = "表示設定は0または1で指定してください")
    private Integer visible;
}
