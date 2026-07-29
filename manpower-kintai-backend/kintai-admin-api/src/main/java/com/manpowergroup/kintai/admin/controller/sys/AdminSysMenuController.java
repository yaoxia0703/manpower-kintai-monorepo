package com.manpowergroup.kintai.admin.controller.sys;

import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.system.application.assembler.sys.MenuAssembler;
import com.manpowergroup.kintai.system.application.dto.sys.response.MenuResponse;
import com.manpowergroup.kintai.system.application.dto.sys.request.MenuCreateRequest;
import com.manpowergroup.kintai.system.application.dto.sys.request.MenuUpdateRequest;
import com.manpowergroup.kintai.system.application.service.sys.SysMenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/sys/menus")
@RequiredArgsConstructor
public class AdminSysMenuController {

    private final SysMenuService service;

    /**
     * すべてのメニューを一覧取得する。
     *
     * @return メニューレスポンス一覧を含むAPIレスポンス
     */
    @GetMapping
    public Result<List<MenuResponse>> listAll() {
        return Result.ok(service.listAll().stream().map(MenuAssembler::toResponse).toList());
    }

    /**
     * IDに対応するメニューを取得する。
     *
     * @param id 対象のメニューID
     * @return メニューレスポンスを含むAPIレスポンス
     */
    @GetMapping("/{id}")
    public Result<MenuResponse> getById(@PathVariable Long id) {
        return Result.ok(MenuAssembler.toResponse(service.getById(id)));
    }

    /**
     * メニューを新規作成する。
     *
     * @param request 処理対象のメニュー作成リクエスト
     * @return メニューレスポンスを含むAPIレスポンス
     */
    @PostMapping
    public Result<MenuResponse> create(@RequestBody @Valid MenuCreateRequest request) {
        return Result.ok(MenuAssembler.toResponse(service.create(MenuAssembler.toCommand(request))));
    }

    /**
     * メニューを更新する。
     *
     * @param id 対象のメニューID
     * @param request 処理対象のメニュー更新リクエスト
     * @return メニューレスポンスを含むAPIレスポンス
     */
    @PutMapping("/{id}")
    public Result<MenuResponse> update(@PathVariable Long id, @RequestBody @Valid MenuUpdateRequest request) {
        return Result.ok(MenuAssembler.toResponse(service.update(id, MenuAssembler.toCommand(request))));
    }

    /**
     * メニューを表示状態に変更する。
     *
     * @param id 対象のメニューID
     * @return 処理結果を示すAPIレスポンス
     */
    @PutMapping("/{id}/show")
    public Result<Void> show(@PathVariable Long id) {
        service.show(id);
        return Result.ok();
    }

    /**
     * メニューを非表示状態に変更する。
     *
     * @param id 対象のメニューID
     * @return 処理結果を示すAPIレスポンス
     */
    @PutMapping("/{id}/hide")
    public Result<Void> hide(@PathVariable Long id) {
        service.hide(id);
        return Result.ok();
    }

    /**
     * メニューを有効化する。
     *
     * @param id 対象のメニューID
     * @return 処理結果を示すAPIレスポンス
     */
    @PutMapping("/{id}/enable")
    public Result<Void> enable(@PathVariable Long id) {
        service.enable(id);
        return Result.ok();
    }

    /**
     * メニューを無効化する。
     *
     * @param id 対象のメニューID
     * @return 処理結果を示すAPIレスポンス
     */
    @PutMapping("/{id}/disable")
    public Result<Void> disable(@PathVariable Long id) {
        service.disable(id);
        return Result.ok();
    }

    /**
     * 指定IDのメニューを削除する。
     *
     * @param id 対象のメニューID
     * @return 処理結果を示すAPIレスポンス
     */
    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id) {
        service.remove(id);
        return Result.ok();
    }
}
