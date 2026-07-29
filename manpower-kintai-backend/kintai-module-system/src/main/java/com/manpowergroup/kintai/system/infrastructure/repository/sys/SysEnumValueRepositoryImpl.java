package com.manpowergroup.kintai.system.infrastructure.repository.sys;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEnumValue;
import com.manpowergroup.kintai.system.domain.repository.sys.SysEnumValueRepository;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysEnumValueMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SysEnumValueRepositoryImpl implements SysEnumValueRepository {

    private final SysEnumValueMapper sysEnumValueMapper;

    @Override
    public SysEnumValue findById(Long id) {
        return sysEnumValueMapper.selectById(id);
    }

    @Override
    public List<SysEnumValue> listEnabledByEnumTypeCode(String enumTypeCode) {
        return sysEnumValueMapper.selectList(Wrappers.<SysEnumValue>lambdaQuery()
            .eq(SysEnumValue::getEnumTypeCode, enumTypeCode)
            .eq(SysEnumValue::getStatus, Status.ENABLED)
            .orderByAsc(SysEnumValue::getSort));
    }

    @Override
    public boolean existsByEnumTypeCodeAndCodeExcludingId(
        String enumTypeCode, String code, Long excludeId) {
        return sysEnumValueMapper.selectCount(Wrappers.<SysEnumValue>lambdaQuery()
            .eq(SysEnumValue::getEnumTypeCode, enumTypeCode)
            .eq(SysEnumValue::getCode, code)
            .ne(excludeId != null, SysEnumValue::getId, excludeId)) > 0;
    }

    @Override
    public void save(SysEnumValue sysEnumValue) {
        sysEnumValueMapper.insert(sysEnumValue);
    }

    @Override
    public void updateById(SysEnumValue sysEnumValue) {
        sysEnumValueMapper.updateById(sysEnumValue);
    }

    @Override
    public void deleteById(Long id) {
        sysEnumValueMapper.deleteById(id);
    }
}
