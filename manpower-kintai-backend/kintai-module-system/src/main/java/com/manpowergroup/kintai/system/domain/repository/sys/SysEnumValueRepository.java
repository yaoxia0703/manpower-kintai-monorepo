package com.manpowergroup.kintai.system.domain.repository.sys;

import com.manpowergroup.kintai.system.domain.entity.sys.SysEnumValue;

import java.util.List;

public interface SysEnumValueRepository {

    SysEnumValue findById(Long id);

    List<SysEnumValue> listEnabledByEnumTypeCode(String enumTypeCode);

    boolean existsByEnumTypeCodeAndCodeExcludingId(String enumTypeCode, String code, Long excludeId);

    void save(SysEnumValue sysEnumValue);

    void updateById(SysEnumValue sysEnumValue);

    void deleteById(Long id);
}
