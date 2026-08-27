package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
        return crmCustomerMapper.insertCrmCustomer(crmCustomer);
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
        return crmCustomerMapper.updateCrmCustomer(crmCustomer);
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

    @Override
    public boolean checkPhoneUnique(CrmCustomer customer) {
        Long customerId = StringUtils.isNull(customer.getCustomerId()) ? -1L : customer.getCustomerId();
        CrmCustomer checkedCrmCustomer = crmCustomerMapper.selectCrmCustomerByPhone(customer.getPhone());
        if (StringUtils.isNotNull(checkedCrmCustomer) && checkedCrmCustomer.getCustomerId().longValue() != customerId.longValue()) {
            return false;
        }
        return true;
    }
}
