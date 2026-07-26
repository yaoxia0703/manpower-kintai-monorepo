package com.manpowergroup.kintai.system.application.service.impl.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.system.application.command.sys.EnumTypeCreateCommand;
import com.manpowergroup.kintai.system.application.command.sys.EnumTypeUpdateCommand;
import com.manpowergroup.kintai.system.application.service.sys.SysEnumTypeService;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEnumType;
import com.manpowergroup.kintai.system.domain.repository.sys.SysEnumTypeRepository;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysEnumTypeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 列挙型マスタサービス実装（アプリケーション層）
@Service
public class SysEnumTypeServiceImpl implements SysEnumTypeService {

    private final SysEnumTypeRepository sysEnumTypeRepository;

    public SysEnumTypeServiceImpl(SysEnumTypeRepository sysEnumTypeRepository) {
        this.sysEnumTypeRepository = sysEnumTypeRepository;
    }

    @Override
    public SysEnumType getById(Long id) {
        SysEnumType et = sysEnumTypeRepository.findById(id);
        if (et == null) throw new BizException(SystemErrorCode.ENUM_TYPE_NOT_FOUND);
        return et;
    }

    @Override
    public SysEnumType getByCode(String code) {
        SysEnumType et = sysEnumTypeRepository.findByCode(code);
        if (et == null) throw new BizException(SystemErrorCode.ENUM_TYPE_NOT_FOUND);
        return et;
    }

    @Override
    public List<SysEnumType> listEnabled() {
        return sysEnumTypeRepository.listEnabled();
    }

    @Override
    public PageResult<SysEnumType> page(PageRequest request) {

        return sysEnumTypeRepository.page(request.page(), request.size());
    }

    @Override
    @Transactional
    public SysEnumType create(EnumTypeCreateCommand command) {
        boolean exists = sysEnumTypeRepository.selectCountByCode(command.code());
        if (exists) throw new BizException(SystemErrorCode.ENUM_TYPE_CODE_DUPLICATE);
        SysEnumType enumType = SysEnumType.create(
            command.code(), command.name(), command.remark(), command.sort(), command.status());
        sysEnumTypeRepository.save(enumType);
        return enumType;
    }

    @Override
    @Transactional
    public SysEnumType update(Long id, EnumTypeUpdateCommand command) {
        SysEnumType existing = getById(id);
        boolean exists = sysEnumTypeRepository.findByCodeExcluding(id, command.code());
        if (exists) throw new BizException(SystemErrorCode.ENUM_TYPE_CODE_DUPLICATE);
        existing.updateEditableFields(command.code(), command.name(), command.remark(), command.sort());
        sysEnumTypeRepository.updateById(existing);
        return existing;
    }

    @Override
    @Transactional
    public void enable(Long id) {
        SysEnumType et = getById(id);
        et.enable();
        sysEnumTypeRepository.updateById(et);
    }

    @Override
    @Transactional
    public void disable(Long id) {
        SysEnumType et = getById(id);
        et.disable();
        sysEnumTypeRepository.updateById(et);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        getById(id);
        sysEnumTypeRepository.deleteById(id);
    }

    enum SystemErrorCode implements BaseErrorCode {
        ENUM_TYPE_NOT_FOUND(404, "error.enum_type.not_found"),
        ENUM_TYPE_CODE_DUPLICATE(409, "error.enum_type.code_duplicate");

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

