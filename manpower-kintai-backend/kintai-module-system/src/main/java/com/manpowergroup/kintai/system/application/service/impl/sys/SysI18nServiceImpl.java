package com.manpowergroup.kintai.system.application.service.impl.sys;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.system.application.command.sys.I18nUpsertCommand;
import com.manpowergroup.kintai.system.application.service.sys.SysI18nService;
import com.manpowergroup.kintai.system.domain.entity.sys.SysI18n;
import com.manpowergroup.kintai.system.domain.repository.sys.SysI18nRepository;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysI18nMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 国際化翻訳サービス実装（アプリケーション層）
@Service
@RequiredArgsConstructor
public class SysI18nServiceImpl implements SysI18nService {
    private final SysI18nRepository i18nRepository;

    @Override
    public SysI18n getById(Long id) {
        SysI18n i18n = i18nRepository.getById(id);
        if (i18n == null) throw new BizException(SystemErrorCode.I18N_NOT_FOUND);
        return i18n;
    }

    @Override
    public List<SysI18n> listByRef(String refType, Long refId) {
        return i18nRepository.listByRef(refType, refId);
    }

    @Override
    public SysI18n getByRefAndLanguage(String refType, Long refId, String language) {
        SysI18n i18n = i18nRepository.getByRefAndLanguage(refType, refId, language);
        if (i18n == null) throw new BizException(SystemErrorCode.I18N_NOT_FOUND);
        return i18n;
    }

    @Override
    @Transactional
    public SysI18n upsert(I18nUpsertCommand command) {
        // 既存の翻訳があれば更新、なければ新規作成
        SysI18n existing = i18nRepository.getByRefAndLanguage(command.refType(), command.refId(), command.language());
        if (existing != null) {
            existing.changeContent(command.content());
            i18nRepository.updateById(existing);
            return existing;
        }
        SysI18n i18n = SysI18n.create(
            command.refType(), command.refId(), command.language(), command.content());
        i18nRepository.save(i18n);
        return i18n;
    }

    @Override
    @Transactional
    public void remove(Long id) {
        getById(id);
        i18nRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void removeByRef(String refType, Long refId) {
        i18nRepository.deleteByRef(refType, refId);
    }

    enum SystemErrorCode implements BaseErrorCode {
        I18N_NOT_FOUND(404, "error.i18n.not_found");

        private final int code;
        private final String messageKey;

        SystemErrorCode(int code, String messageKey) {
            this.code = code;
            this.messageKey = messageKey;
        }

        @Override
        public int code() {
            return code;
        }

        @Override
        public String messageKey() {
            return messageKey;
        }
    }
}

