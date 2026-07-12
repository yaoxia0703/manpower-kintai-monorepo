package com.manpowergroup.kintai.system.domain.entity.sys;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

// 国際化翻訳テーブル（ENUM/GRADE/NODE 等に対応）
@Getter
@Setter(AccessLevel.PRIVATE)
@Accessors(chain = true)
@TableName("sys_i18n")
/** 業務データの参照キーと言語に対応する翻訳内容を管理する。 */
public class SysI18n {

    @TableId(type = IdType.AUTO)
    @Setter
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

    /** 参照キーと言語を固定した翻訳を作成する。 */
    public static SysI18n create(String refType, Long refId, String language, String content) {
        return new SysI18n()
                .setRefType(refType)
                .setRefId(refId)
                .setLanguage(language)
                .setContent(content);
    }

    /** 参照キーを変更せずに翻訳内容を差し替える。 */
    public void changeContent(String content) {
        this.content = content;
    }
}

