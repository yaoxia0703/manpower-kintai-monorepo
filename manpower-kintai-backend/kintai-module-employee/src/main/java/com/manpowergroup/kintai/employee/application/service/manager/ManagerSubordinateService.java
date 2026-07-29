package com.manpowergroup.kintai.employee.application.service.manager;

import com.manpowergroup.kintai.common.dto.JoinPageResult;
import com.manpowergroup.kintai.employee.application.dto.manager.response.SubordinateEmployeeResponse;
import com.manpowergroup.kintai.employee.application.dto.manager.response.SubordinateFilterOptionsResponse;
import com.manpowergroup.kintai.employee.application.query.manager.SubordinateQuery;

public interface ManagerSubordinateService {

    /**
     * 対象管理者の部下をページング取得する。
     *
     * @param query 処理対象の部下情報検索条件
     * @param pageNum ページ番号
     * @param pageSize 1ページあたりの取得件数
     * @return 関連情報を含むページングされた部下社員レスポンス一覧
     */
    JoinPageResult<SubordinateEmployeeResponse> pageSubordinates(SubordinateQuery query, int pageNum, int pageSize);

    /**
     * 画面表示に必要な選択肢を取得する。
     *
     * @param managerId 管理者の社員ID
     * @return 部下情報Filter選択肢レスポンス
     */
    SubordinateFilterOptionsResponse options(Long managerId);
}
