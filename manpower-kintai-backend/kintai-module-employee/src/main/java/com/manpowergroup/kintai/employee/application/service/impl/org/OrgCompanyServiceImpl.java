package com.manpowergroup.kintai.employee.application.service.impl.org;

import com.manpowergroup.kintai.common.dto.PageRequest;
import com.manpowergroup.kintai.common.dto.PageResult;
import com.manpowergroup.kintai.common.exception.BaseErrorCode;
import com.manpowergroup.kintai.common.exception.BizException;
import com.manpowergroup.kintai.employee.application.command.org.CompanyCreateCommand;
import com.manpowergroup.kintai.employee.application.command.org.CompanyUpdateCommand;
import com.manpowergroup.kintai.employee.application.service.org.OrgCompanyService;
import com.manpowergroup.kintai.employee.domain.entity.org.OrgCompany;
import com.manpowergroup.kintai.employee.domain.repository.org.OrgCompanyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 会社マスタサービス実装（アプリケーション層）
@Service
public class OrgCompanyServiceImpl implements OrgCompanyService {

    private final OrgCompanyRepository orgCompanyRepository;

    public OrgCompanyServiceImpl(OrgCompanyRepository orgCompanyRepository) {
        this.orgCompanyRepository = orgCompanyRepository;
    }

    @Override
    public OrgCompany getById(Long id) {
        OrgCompany company = orgCompanyRepository.findById(id);
        if (company == null) throw new BizException(SystemErrorCode.COMPANY_NOT_FOUND);
        return company;
    }

    @Override
    public PageResult<OrgCompany> page(PageRequest request) {
        return orgCompanyRepository.findPage(request.page(), request.size());
    }

    @Override
    public List<OrgCompany> listEnabled() {
        return orgCompanyRepository.listEnabled();
    }

    @Override
    @Transactional
    public OrgCompany create(CompanyCreateCommand command) {
        boolean exists = orgCompanyRepository.selecCountByCompanyCode(command.companyCode()) > 0;
        if (exists) throw new BizException(SystemErrorCode.COMPANY_CODE_DUPLICATE);
        OrgCompany company = OrgCompany.create(
            command.parentId(),
            command.name(),
            command.companyCode(),
            command.level(),
            command.sort(),
            command.status());
        orgCompanyRepository.save(company);
        return company;
    }

    @Override
    @Transactional
    public OrgCompany update(Long id, CompanyUpdateCommand command) {
        OrgCompany existing = getById(id);
        boolean exists = orgCompanyRepository.existsByCompanyAndCodeExcludingId(command.companyCode(), id);
        if (exists) throw new BizException(SystemErrorCode.COMPANY_CODE_DUPLICATE);
        existing.updateEditableFields(
            command.parentId(),
            command.name(),
            command.companyCode(),
            command.level(),
            command.sort());
        orgCompanyRepository.updateById(existing);
        return existing;
    }

    @Override
    @Transactional
    public void enable(Long id) {
        OrgCompany company = getById(id);
        company.enable();
        orgCompanyRepository.updateById(company);
    }

    @Override
    @Transactional
    public void disable(Long id) {
        OrgCompany company = getById(id);
        company.disable();
        orgCompanyRepository.updateById(company);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        getById(id);
        orgCompanyRepository.deleteById(id);
    }

    enum SystemErrorCode implements BaseErrorCode {
        COMPANY_NOT_FOUND(404, "error.company.not_found"),
        COMPANY_CODE_DUPLICATE(409, "error.company.code_duplicate");

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

