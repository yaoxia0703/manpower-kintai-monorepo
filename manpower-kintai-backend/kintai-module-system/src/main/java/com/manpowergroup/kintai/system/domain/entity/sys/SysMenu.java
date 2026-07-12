package com.manpowergroup.kintai.system.domain.entity.sys;

import com.baomidou.mybatisplus.annotation.*;
import com.manpowergroup.kintai.common.enums.Status;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

// メニューマスタ
@Getter
@Setter(AccessLevel.PRIVATE)
@Accessors(chain = true)
@TableName("sys_menu")
/** メニュー階層、表示状態および有効状態を管理する。 */
public class SysMenu {

    @TableId(type = IdType.AUTO)
    @Setter
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

    /** 有効なメニューを作成し、未指定の表示状態を表示に初期化する。 */
    public static SysMenu create(Long parentId, String name, String code, String path,
                                 String component, String icon, Integer type, Integer sort,
                                 Integer visible) {
        return new SysMenu()
                .setParentId(parentId)
                .setName(name)
                .setCode(code)
                .setPath(path)
                .setComponent(component)
                .setIcon(icon)
                .setType(type)
                .setSort(sort)
                .setVisible(defaultVisible(visible))
                .setStatus(Status.ENABLED);
    }

    /** メニューの編集可能な属性を更新する。 */
    public void updateEditableFields(Long parentId, String name, String code, String path,
                                     String component, String icon, Integer type, Integer sort,
                                     Integer visible) {
        this.parentId = parentId;
        this.name = name;
        this.code = code;
        this.path = path;
        this.component = component;
        this.icon = icon;
        this.type = type;
        this.sort = sort;
        this.visible = defaultVisible(visible);
    }

    private static Integer defaultVisible(Integer visible) {
        return visible == null ? 1 : visible;
    }

    /** メニューを表示対象にする。 */
    public void show() {
        this.visible = 1;
    }

    /** メニューを非表示にする。 */
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

