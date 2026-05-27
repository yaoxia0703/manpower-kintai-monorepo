package com.manpowergroup.kintai.system.application.service.impl.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.enums.Status;
import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.system.domain.entity.sys.SysEnumType;
import com.manpowergroup.kintai.system.infrastructure.mapper.sys.SysEnumTypeMapper;
import com.manpowergroup.kintai.system.application.service.sys.SysEnumTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 列挙型マスタサービス実装（アプリケーション層）
@Service
public class SysEnumTypeServiceImpl extends ServiceImpl<SysEnumTypeMapper, SysEnumType>
        implements SysEnumTypeService {

    @Override
    public SysEnumType getById(Long id) {
        SysEnumType et = super.getById(id);
        if (et == null) throw new BizException(SystemErrorCode.ENUM_TYPE_NOT_FOUND);
        return et;
    }

    @Override
    public SysEnumType getByCode(String code) {
        SysEnumType et = lambdaQuery().eq(SysEnumType::getCode, code).one();
        if (et == null) throw new BizException(SystemErrorCode.ENUM_TYPE_NOT_FOUND);
        return et;
    }

    @Override
    public List<SysEnumType> listEnabled() {
        return lambdaQuery()
                .eq(SysEnumType::getStatus, Status.ENABLED)
                .orderByAsc(SysEnumType::getSort)
                .list();
    }

    @Override
    public PageResult<SysEnumType> page(PageRequest request) {
        Page<SysEnumType> p = new Page<>(request.page(), request.size());
        page(p, lambdaQuery().orderByAsc(SysEnumType::getSort).getWrapper());
        return PageResult.of(p);
    }

    @Override
    @Transactional
    public SysEnumType create(SysEnumType enumType) {
        boolean exists = lambdaQuery().eq(SysEnumType::getCode, enumType.getCode()).count() > 0;
        if (exists) throw new BizException(SystemErrorCode.ENUM_TYPE_CODE_DUPLICATE);
        save(enumType);
        return enumType;
    }

    @Override
    @Transactional
    public SysEnumType update(Long id, SysEnumType enumType) {
        SysEnumType existing = getById(id);
        boolean exists = lambdaQuery()
                .eq(SysEnumType::getCode, enumType.getCode())
                .ne(SysEnumType::getId, id)
                .count() > 0;
        if (exists) throw new BizException(SystemErrorCode.ENUM_TYPE_CODE_DUPLICATE);
        existing.setName(enumType.getName())
                .setCode(enumType.getCode())
                .setRemark(enumType.getRemark())
                .setSort(enumType.getSort());
        updateById(existing);
        return existing;
    }

    @Override
    @Transactional
    public void enable(Long id) {
        SysEnumType et = getById(id);
        et.setStatus(Status.ENABLED);
        updateById(et);
    }

    @Override
    @Transactional
    public void disable(Long id) {
        SysEnumType et = getById(id);
        et.setStatus(Status.DISABLED);
        updateById(et);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        getById(id);
        removeById(id);
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

        @Override public int code() { return code; }
        @Override public String messageKey() { return messageKey; }
    }
}

