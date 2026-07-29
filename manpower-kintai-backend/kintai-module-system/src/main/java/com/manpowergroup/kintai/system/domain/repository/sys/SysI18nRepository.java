package com.manpowergroup.kintai.system.domain.repository.sys;

import com.manpowergroup.kintai.system.domain.entity.sys.SysI18n;

import java.util.List;

public interface SysI18nRepository {

    SysI18n getById(Long id);

    List<SysI18n> listByRef(String refType, Long refId);

    SysI18n getByRefAndLanguage(String refType, Long refId, String language);

    void save(SysI18n sysI18n);

    void updateById(SysI18n sysI18n);

    void deleteById(Long id);

    void deleteByRef(String refType, Long refId);
}
