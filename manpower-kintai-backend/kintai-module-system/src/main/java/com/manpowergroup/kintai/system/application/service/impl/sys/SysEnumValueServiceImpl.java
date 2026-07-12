package com.manpowergroup.kintai.system.application.service.impl.sys;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.system.application.command.sys.EnumValueCreateCommand;
import com.manpowergroup.kintai.system.application.command.sys.EnumValueUpdateCommand;
import com.manpowergroup.kintai.system.application.service.sys.SysEnumValueService;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEnumValue;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysEnumValueMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 列挙値定義サービス実装（アプリケーション層）
@Service
public class SysEnumValueServiceImpl extends ServiceImpl<SysEnumValueMapper, SysEnumValue>
        implements SysEnumValueService {

    @Override
    public SysEnumValue getById(Long id) {
        SysEnumValue ev = super.getById(id);
        if (ev == null) throw new BizException(SystemErrorCode.ENUM_VALUE_NOT_FOUND);
        return ev;
    }

    @Override
    public List<SysEnumValue> listByEnumTypeCode(String enumTypeCode) {
        return lambdaQuery()
                .eq(SysEnumValue::getEnumTypeCode, enumTypeCode)
                .eq(SysEnumValue::getStatus, Status.ENABLED)
                .orderByAsc(SysEnumValue::getSort)
                .list();
    }

    @Override
    @Transactional
    public SysEnumValue create(EnumValueCreateCommand command) {
        boolean exists = lambdaQuery()
                .eq(SysEnumValue::getEnumTypeCode, command.enumTypeCode())
                .eq(SysEnumValue::getCode, command.code())
                .count() > 0;
        if (exists) throw new BizException(SystemErrorCode.ENUM_VALUE_CODE_DUPLICATE);
        SysEnumValue enumValue = SysEnumValue.create(
                command.enumTypeCode(), command.code(), command.sort(), command.status());
        save(enumValue);
        return enumValue;
    }

    @Override
    @Transactional
    public SysEnumValue update(Long id, EnumValueUpdateCommand command) {
        SysEnumValue existing = getById(id);
        boolean exists = lambdaQuery()
                .eq(SysEnumValue::getEnumTypeCode, command.enumTypeCode())
                .eq(SysEnumValue::getCode, command.code())
                .ne(SysEnumValue::getId, id)
                .count() > 0;
        if (exists) throw new BizException(SystemErrorCode.ENUM_VALUE_CODE_DUPLICATE);
        existing.updateEditableFields(command.enumTypeCode(), command.code(), command.sort());
        updateById(existing);
        return existing;
    }

    @Override
    @Transactional
    public void enable(Long id) {
        SysEnumValue ev = getById(id);
        ev.enable();
        updateById(ev);
    }

    @Override
    @Transactional
    public void disable(Long id) {
        SysEnumValue ev = getById(id);
        ev.disable();
        updateById(ev);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        getById(id);
        removeById(id);
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

        @Override public int code() { return code; }
        @Override public String messageKey() { return messageKey; }
    }
}

