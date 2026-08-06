package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.CrmCustomer;

/**
 * 客户档案Mapper接口
 * 
 * @author zhouhuihhhh
 * @date 2026-08-05
 */
public interface CrmCustomerMapper 
{
    /**
     * 查询客户档案
     * 
     * @param customerId 客户档案主键
     * @return 客户档案
     */
    public CrmCustomer selectCrmCustomerByCustomerId(Long customerId);

    /**
     * 查询客户档案列表
     * 
     * @param crmCustomer 客户档案
     * @return 客户档案集合
     */
    public List<CrmCustomer> selectCrmCustomerList(CrmCustomer crmCustomer);

    /**
     * 新增客户档案
     * 
     * @param crmCustomer 客户档案
     * @return 结果
     */
    public int insertCrmCustomer(CrmCustomer crmCustomer);

    /**
     * 修改客户档案
     * 
     * @param crmCustomer 客户档案
     * @return 结果
     */
    public int updateCrmCustomer(CrmCustomer crmCustomer);

    /**
     * 删除客户档案
     * 
     * @param customerId 客户档案主键
     * @return 结果
     */
    public int deleteCrmCustomerByCustomerId(Long customerId);

    /**
     * 批量删除客户档案
     * 
     * @param customerIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCrmCustomerByCustomerIds(Long[] customerIds);
}
