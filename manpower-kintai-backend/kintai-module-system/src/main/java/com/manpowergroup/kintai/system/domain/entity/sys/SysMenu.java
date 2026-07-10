package com.manpowergroup.kintai.system.domain.entity.sys;

import com.baomidou.mybatisplus.annotation.*;
import com.manpowergroup.kintai.common.enums.Status;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

// メニューマスタ
@Data
@Accessors(chain = true)
@TableName("sys_menu")
public class SysMenu {

    @TableId(type = IdType.AUTO)
    // メニューID
    private Long id;

    // 親メニューID（NULLがルート）
    private Long parentId;

    // メニュー名
    private String name;

    // メニューコード
    private String code;

    // フロントエンドルートパス
    private String path;

    // フロントエンドコンポーネントパス
    private String component;

    // アイコン
    private String icon;

    // タイプ（1=ディレクトリ 2=メニュー 3=ボタン）
    private Integer type;

    // 表示順
    private Integer sort;

    // 表示フラグ（1=表示 0=非表示）
    private Integer visible;

    // ステータス
    private Status status;

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

    public void show() {
        this.visible = 1;
    }

    public void hide() {
        this.visible = 0;
    }

    public void enable() {
        this.status = Status.ENABLED;
    }

    public void disable() {
        this.status = Status.DISABLED;
    }
}

