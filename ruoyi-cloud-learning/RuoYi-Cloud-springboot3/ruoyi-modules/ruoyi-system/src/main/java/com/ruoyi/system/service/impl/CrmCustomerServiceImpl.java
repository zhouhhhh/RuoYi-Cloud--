package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.CrmCustomerMapper;
import com.ruoyi.system.domain.CrmCustomer;
import com.ruoyi.system.service.ICrmCustomerService;

/**
 * 客户档案Service业务层处理
 * 
 * @author zhouhuihhhh
 * @date 2026-08-05
 */
@Service
public class CrmCustomerServiceImpl implements ICrmCustomerService
{
    private static final Logger log = LoggerFactory.getLogger(CrmCustomerServiceImpl.class);

    @Autowired
    private CrmCustomerMapper crmCustomerMapper;

    /**
     * 查询客户档案
     * 
     * @param customerId 客户档案主键
     * @return 客户档案
     */
    @Override
    public CrmCustomer selectCrmCustomerByCustomerId(Long customerId)
    {
        return crmCustomerMapper.selectCrmCustomerByCustomerId(customerId);
    }

    /**
     * 查询客户档案列表
     * 
     * @param crmCustomer 客户档案
     * @return 客户档案
     */
    @Override
    public List<CrmCustomer> selectCrmCustomerList(CrmCustomer crmCustomer)
    {
        return crmCustomerMapper.selectCrmCustomerList(crmCustomer);
    }

    /**
     * 新增客户档案
     * 
     * @param crmCustomer 客户档案
     * @return 结果
     */
    @Override
    public int insertCrmCustomer(CrmCustomer crmCustomer)
    {
        crmCustomer.setCreateTime(DateUtils.getNowDate());
        try
        {
            return crmCustomerMapper.insertCrmCustomer(crmCustomer);
        }
        catch (DuplicateKeyException e)
        {
            throw translateDuplicateKeyException("新增", e);
        }
    }

    /**
     * 修改客户档案
     * 
     * @param crmCustomer 客户档案
     * @return 结果
     */
    @Override
    public int updateCrmCustomer(CrmCustomer crmCustomer)
    {
        crmCustomer.setUpdateTime(DateUtils.getNowDate());
        try
        {
            return crmCustomerMapper.updateCrmCustomer(crmCustomer);
        }
        catch (DuplicateKeyException e)
        {
            throw translateDuplicateKeyException("修改", e);
        }
    }

    /**
     * 批量删除客户档案
     * 
     * @param customerIds 需要删除的客户档案主键
     * @return 结果
     */
    @Override
    public int deleteCrmCustomerByCustomerIds(Long[] customerIds)
    {
        return crmCustomerMapper.deleteCrmCustomerByCustomerIds(customerIds);
    }

    /**
     * 删除客户档案信息
     * 
     * @param customerId 客户档案主键
     * @return 结果
     */
    @Override
    public int deleteCrmCustomerByCustomerId(Long customerId)
    {
        return crmCustomerMapper.deleteCrmCustomerByCustomerId(customerId);
    }

    /**
     * 确认手机号码是否存在
     * @param customer
     * @return
     */
    @Override
    public boolean checkPhoneUnique(CrmCustomer customer) {
        Long customerId = StringUtils.isNull(customer.getCustomerId()) ? -1L : customer.getCustomerId();
        CrmCustomer checkedCrmCustomer = crmCustomerMapper.selectCrmCustomerByPhone(customer.getPhone());
        if (StringUtils.isNotNull(checkedCrmCustomer) && checkedCrmCustomer.getCustomerId().longValue() != customerId.longValue()) {
            return false;
        }
        return true;
    }

    /**
     * 封装的内部使用的工具方法 主要用在索引异常处理
     * @param operation
     * @param e
     * @return
     */
    private ServiceException translateDuplicateKeyException(
            String operation, DuplicateKeyException e)
    {
        log.error("{}客户时发生唯一索引冲突", operation, e);

        Throwable rootCause = e.getMostSpecificCause();
        String rootMessage = rootCause.getMessage();

        if (StringUtils.contains(rootMessage, "uk_customer_phone"))
        {
            return new ServiceException("手机号码已存在");
        }
        return new ServiceException("客户数据存在唯一性冲突");
    }
}
