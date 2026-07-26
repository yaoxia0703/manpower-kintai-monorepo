package com.manpowergroup.kintai.system.infrastructure.repository.sys;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEnumType;
import com.manpowergroup.kintai.system.domain.repository.sys.SysEnumTypeRepository;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysEnumTypeMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysEnumTypeRepositoryImpl implements SysEnumTypeRepository {
    private final SysEnumTypeMapper sysEnumTypeMapper;

    public SysEnumTypeRepositoryImpl(SysEnumTypeMapper sysEnumTypeMapper) {
        this.sysEnumTypeMapper = sysEnumTypeMapper;
    }

    @Override
    public SysEnumType findById(Long id) {
        return sysEnumTypeMapper.selectById(id);
    }

    @Override
    public SysEnumType findByCode(String code) {
        return sysEnumTypeMapper.selectOne(Wrappers.<SysEnumType>lambdaQuery().eq(SysEnumType::getCode, code));
    }

    @Override
    public List<SysEnumType> listEnabled() {
        return sysEnumTypeMapper.selectList(Wrappers.<SysEnumType>lambdaQuery()
            .eq(SysEnumType::getStatus, Status.ENABLED)
            .orderByAsc(SysEnumType::getSort));
    }

    @Override
    public PageResult<SysEnumType> page(int page, int size) {
        Page<SysEnumType> p = new Page<>(page, size);
        sysEnumTypeMapper.selectList(p, Wrappers.<SysEnumType>lambdaQuery().orderByAsc(SysEnumType::getSort));
        return PageResult.of(p);
    }

    @Override
    public boolean selectCountByCode(String code) {
        return sysEnumTypeMapper.selectCount(Wrappers.<SysEnumType>lambdaQuery().eq(SysEnumType::getCode, code)) > 0;
    }

    @Override
    public boolean findByCodeExcluding(Long id, String code) {
        return sysEnumTypeMapper.selectCount(Wrappers.<SysEnumType>lambdaQuery()
            .eq(SysEnumType::getCode, code)
            .ne(SysEnumType::getId, id)) > 0;
    }

    @Override
    public void save(SysEnumType sysEnumType) {
        sysEnumTypeMapper.insert(sysEnumType);
    }

    @Override
    public void updateById(SysEnumType sysEnumType) {
        sysEnumTypeMapper.updateById(sysEnumType);
    }

    @Override
    public void deleteById(Long id) {
        sysEnumTypeMapper.deleteById(id);
    }


}
