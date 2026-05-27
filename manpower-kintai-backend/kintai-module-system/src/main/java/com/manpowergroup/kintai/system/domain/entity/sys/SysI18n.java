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
    private Long id;

    // 参照タイプ（ENUM / GRADE / NODE）
    private String refType;

    // 参照レコードID
    private Long refId;

    // 言語コード（ja / zh / en）
    private String language;

    // 翻訳テキスト
    private String content;

    private Long createdBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    private Long updatedBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer isDeleted;
}

