package com.manpowergroup.kintai.employee.controller.manager;

import com.manpowergroup.kintai.common.dto.JoinPageResult;
import com.manpowergroup.kintai.common.result.Result;
import com.manpowergroup.kintai.framework.security.jwt.LoginPrincipal;
import com.manpowergroup.kintai.employee.application.assembler.manager.SubordinateAssembler;
import com.manpowergroup.kintai.employee.application.dto.manager.request.SubordinateQueryRequest;
import com.manpowergroup.kintai.employee.application.dto.manager.response.SubordinateEmployeeResponse;
import com.manpowergroup.kintai.employee.application.dto.manager.response.SubordinateFilterOptionsResponse;
import com.manpowergroup.kintai.employee.application.query.manager.SubordinateQuery;
import com.manpowergroup.kintai.employee.application.service.manager.ManagerSubordinateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 管理者視点：配下従業員照会コントローラー
@RestController
@RequestMapping("/manager/emp/subordinates")
@RequiredArgsConstructor
public class ManagerSubordinateController {

    private final ManagerSubordinateService subordinateService;

    /**
     * 画面表示に必要な選択肢を取得する。
     *
     * @param principal ログインユーザー情報
     * @return 部下情報Filter選択肢レスポンスを含むAPIレスポンス
     */
    @GetMapping("/options")
    public Result<SubordinateFilterOptionsResponse> options(
            @AuthenticationPrincipal LoginPrincipal principal) {
        return Result.ok(subordinateService.options(principal.employeeId()));
    }

    /**
     * 対象管理者の部下をページング取得する。
     *
     * @param principal ログインユーザー情報
     * @param request 処理対象の部下情報検索リクエスト
     * @return 関連情報を含むページングされた部下社員レスポンス一覧のAPIレスポンス
     */
    @GetMapping
    public Result<JoinPageResult<SubordinateEmployeeResponse>> pageSubordinates(
            @AuthenticationPrincipal LoginPrincipal principal,
            @Valid SubordinateQueryRequest request) {

        // managerId はログイン者本人から注入（フロントからは受け取らない）
        SubordinateQuery query = SubordinateAssembler.toQuery(request, principal.employeeId());

        return Result.ok(subordinateService.pageSubordinates(
                query, request.getPage(), request.getSize()));
    }
}
