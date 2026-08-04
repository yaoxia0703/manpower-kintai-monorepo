package com.manpowergroup.kintai.hr.application.port.emp;

import com.manpowergroup.kintai.common.enums.Status;

//全社員一覧クエリ(HR用)
public record EmployeeDirectoryQuery(

    Long companyId,
    String keyword,        // 氏名・社員コードの部分一致
    Long nodeId,         // 所属ノードで絞り込み
    Long gradeId,        // 職級で絞り込み
    Status status        // 在職状態で絞り込み
) {


}
