package com.manpowergroup.kintai.system.application.service.impl.sys;

import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.system.application.command.sys.EnumValueCreateCommand;
import com.manpowergroup.kintai.system.application.command.sys.EnumValueUpdateCommand;
import com.manpowergroup.kintai.system.application.service.sys.SysEnumValueService;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEnumValue;
import com.manpowergroup.kintai.system.domain.repository.sys.SysEnumValueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 列挙値定義サービス実装（アプリケーション層）
@Service
public class SysEnumValueServiceImpl implements SysEnumValueService {

    private final SysEnumValueRepository enumValueRepository;

    public SysEnumValueServiceImpl(SysEnumValueRepository enumValueRepository) {
        this.enumValueRepository = enumValueRepository;
    }

    @Override
    public SysEnumValue getById(Long id) {
        SysEnumValue ev = enumValueRepository.findById(id);
        if (ev == null) throw new BizException(SystemErrorCode.ENUM_VALUE_NOT_FOUND);
        return ev;
    }

    @Override
    public List<SysEnumValue> listByEnumTypeCode(String enumTypeCode) {
        return enumValueRepository.listEnabledByEnumTypeCode(enumTypeCode);
    }

    @Override
    @Transactional
    public SysEnumValue create(EnumValueCreateCommand command) {
        boolean exists = enumValueRepository.existsByEnumTypeCodeAndCodeExcludingId(
            command.enumTypeCode(), command.code(), null);
        if (exists) throw new BizException(SystemErrorCode.ENUM_VALUE_CODE_DUPLICATE);
        SysEnumValue enumValue = SysEnumValue.create(
            command.enumTypeCode(), command.code(), command.sort(), command.status());
        enumValueRepository.save(enumValue);
        return enumValue;
    }

    @Override
    @Transactional
    public SysEnumValue update(Long id, EnumValueUpdateCommand command) {
        SysEnumValue existing = getById(id);
        boolean exists = enumValueRepository.existsByEnumTypeCodeAndCodeExcludingId(
            command.enumTypeCode(), command.code(), id);
        if (exists) throw new BizException(SystemErrorCode.ENUM_VALUE_CODE_DUPLICATE);
        existing.updateEditableFields(command.enumTypeCode(), command.code(), command.sort());
        enumValueRepository.updateById(existing);
        return existing;
    }

    @Override
    @Transactional
    public void enable(Long id) {
        SysEnumValue ev = getById(id);
        ev.enable();
        enumValueRepository.updateById(ev);
    }

    @Override
    @Transactional
    public void disable(Long id) {
        SysEnumValue ev = getById(id);
        ev.disable();
        enumValueRepository.updateById(ev);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        getById(id);
        enumValueRepository.deleteById(id);
    }

    enum SystemErrorCode implements BaseErrorCode {
        ENUM_VALUE_NOT_FOUND(404, "error.enum_value.not_found"),
        ENUM_VALUE_CODE_DUPLICATE(409, "error.enum_value.code_duplicate");

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
