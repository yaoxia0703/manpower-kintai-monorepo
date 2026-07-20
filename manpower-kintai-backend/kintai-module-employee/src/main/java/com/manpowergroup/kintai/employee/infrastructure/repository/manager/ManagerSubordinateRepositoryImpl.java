package com.manpowergroup.kintai.employee.infrastructure.repository.manager;

import com.manpowergroup.kintai.common.dto.JoinPageResult;
import com.manpowergroup.kintai.employee.application.dto.manager.response.SubordinateEmployeeResponse;
import com.manpowergroup.kintai.employee.application.dto.manager.response.SubordinateFilterOptionsResponse;
import com.manpowergroup.kintai.employee.application.query.manager.SubordinateQuery;
import com.manpowergroup.kintai.employee.domain.repository.manager.ManagerSubordinateRepository;
import com.manpowergroup.kintai.employee.infrastructure.mapper.manager.ManagerSubordinateQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

// 管理者視点：配下従業員クエリのリポジトリ実装
@Repository
@RequiredArgsConstructor
public class ManagerSubordinateRepositoryImpl implements ManagerSubordinateRepository {

    private final ManagerSubordinateQueryMapper managerSubordinateQueryMapper;

    @Override
    public JoinPageResult<SubordinateEmployeeResponse> pageSubordinates(
            SubordinateQuery query, int pageNum, int pageSize) {

        // 総件数（重複排除後）を先に取得
        long total = managerSubordinateQueryMapper.countSubordinatePage(query);

        // 件数が 0 の場合は DB を再度叩かず空ページを返す
        if (total == 0) {
            return JoinPageResult.of(List.of(), 0, pageNum, pageSize);
        }

        // オフセット計算（LIMIT 用。SQL 層の詳細としてここに閉じ込める）
        int offset = (pageNum - 1) * pageSize;

        // 当該ページのデータを取得
        List<SubordinateEmployeeResponse> records =
                managerSubordinateQueryMapper.selectSubordinatePage(query, offset, pageSize);

        return JoinPageResult.of(records, total, pageNum, pageSize);
    }

    @Override
    public SubordinateFilterOptionsResponse filterOptions(Long managerId) {
        SubordinateQuery query = new SubordinateQuery(managerId, null, null, null, null);
        return SubordinateFilterOptionsResponse.builder()
                .nodes(managerSubordinateQueryMapper.selectSubordinateNodeOptions(query))
                .grades(managerSubordinateQueryMapper.selectSubordinateGradeOptions(query))
                .build();
    }
}
