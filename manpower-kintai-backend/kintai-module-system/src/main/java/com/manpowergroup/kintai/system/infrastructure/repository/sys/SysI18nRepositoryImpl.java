package com.manpowergroup.kintai.system.infrastructure.repository.sys;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manpowergroup.kintai.system.domain.entity.sys.SysI18n;
import com.manpowergroup.kintai.system.domain.repository.sys.SysI18nRepository;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysI18nMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SysI18nRepositoryImpl implements SysI18nRepository {

    private final SysI18nMapper i18nMapper;

    @Override
    public SysI18n getById(Long id) {
        return i18nMapper.selectById(id);
    }

    @Override
    public List<SysI18n> listByRef(String refType, Long refId) {
        return i18nMapper.selectList(Wrappers.<SysI18n>lambdaQuery().eq(SysI18n::getRefType, refType)
            .eq(SysI18n::getRefId, refId));
    }

    @Override
    public SysI18n getByRefAndLanguage(String refType, Long refId, String language) {
        return i18nMapper.selectOne(Wrappers.<SysI18n>lambdaQuery()
            .eq(SysI18n::getRefType, refType)
            .eq(SysI18n::getRefId, refId)
            .eq(SysI18n::getLanguage, language));
    }

    @Override
    public void save(SysI18n sysI18n) {
        i18nMapper.insert(sysI18n);
    }

    @Override
    public void updateById(SysI18n sysI18n) {
        i18nMapper.updateById(sysI18n);
    }

    @Override
    public void deleteById(Long id) {
        i18nMapper.deleteById(id);
    }

    @Override
    public void deleteByRef(String refType, Long refId) {
        i18nMapper.delete(Wrappers.<SysI18n>lambdaQuery()
            .eq(SysI18n::getRefType, refType)
            .eq(SysI18n::getRefId, refId)
        );
    }

}
