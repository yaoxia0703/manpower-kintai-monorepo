package com.manpowergroup.kintai.system.application.dto.sys.request;

import com.manpowergroup.kintai.system.domain.enums.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "通知リクエスト")
public record SysNotificationCreateRequest(


        @Schema(description = "所属会社ID", example = "1")
        @NotNull(message = "所属会社IDは必須です")
        Long companyId,

        @Schema(description = "受信者社員ID", example = "1")
        @NotNull(message = "受信者社員IDは必須です")
        Long recipientId,

        @Schema(description = "通知タイプ")
        @NotNull(message = "通知タイプは必須です")
        NotificationType type,

        @Schema(description = "通知タイトル")
        @NotBlank(message = "通知タイトルは必須です")
        @Size(max = 200, message = "通知タイトルは200文字以内にしてください")
        String title,

        @Schema(description = "通知内容")
        @Size(max = 500, message = "通知内容は500文字以内にしてください")
        String content,

        @Schema(description = "関連業務タイプ（REQUEST_TYPE参照）")
        @Size(max = 50, message = "関連業務タイプは50文字以内にしてください")
        String refType,

        @Schema(description = "関連業務ID")
        Long refId,

        @Schema(description = "既読フラグ")
        Boolean isRead
) {
}
