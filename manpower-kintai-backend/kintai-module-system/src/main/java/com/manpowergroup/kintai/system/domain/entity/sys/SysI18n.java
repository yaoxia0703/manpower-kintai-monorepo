package com.manpowergroup.kintai.system.domain.entity.sys;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

// 国際化翻訳テーブル（ENUM/GRADE/NODE 等に対応）
@Data
@Accessors(chain = true)
@TableName("sys_i18n")
public class SysI18n {

    @TableId(type = IdType.AUTO)
    // 国際化翻訳ID
    private Long id;

    // 参照タイプ（ENUM / GRADE / NODE）
    private String refType;

    // 参照レコードID
    private Long refId;

    // 言語コード（ja / zh / en）
    private String language;

    // 翻訳テキスト
    private String content;

    // 作成者ID
    private Long createdBy;

    // 作成日時
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    // 更新者ID
    private Long updatedBy;

    // 更新日時
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    // 論理削除（0=有効 1=削除）
    @TableLogic
    private Integer isDeleted;
}

