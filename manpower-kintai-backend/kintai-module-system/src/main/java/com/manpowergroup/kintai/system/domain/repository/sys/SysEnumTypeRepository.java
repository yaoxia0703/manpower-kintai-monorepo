package com.manpowergroup.kintai.system.domain.repository.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEnumType;

import java.util.List;

public interface SysEnumTypeRepository {
    SysEnumType findById(Long id);

    SysEnumType findByCode(String code);

    List<SysEnumType> listEnabled();

    PageResult<SysEnumType> page(int page, int size);

    boolean selectCountByCode(String code);

    boolean findByCodeExcluding(Long id,String code);

    void save(SysEnumType sysEnumType);

    void updateById(SysEnumType sysEnumType);

    void deleteById(Long id);

}
