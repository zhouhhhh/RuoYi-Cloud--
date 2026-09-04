package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.CrmContactMapper;
import com.ruoyi.system.domain.CrmContact;
import com.ruoyi.system.service.ICrmContactService;

/**
 * 联系人管理Service业务层处理
 * 
 * @author zhouhuihhhh
 * @date 2026-09-04
 */
@Service
public class CrmContactServiceImpl implements ICrmContactService 
{
    @Autowired
    private CrmContactMapper crmContactMapper;

    /**
     * 查询联系人管理
     * 
     * @param contactId 联系人管理主键
     * @return 联系人管理
     */
    @Override
    public CrmContact selectCrmContactByContactId(Long contactId)
    {
        return crmContactMapper.selectCrmContactByContactId(contactId);
    }

    /**
     * 查询联系人管理列表
     * 
     * @param crmContact 联系人管理
     * @return 联系人管理
     */
    @Override
    public List<CrmContact> selectCrmContactList(CrmContact crmContact)
    {
        return crmContactMapper.selectCrmContactList(crmContact);
    }

    /**
     * 新增联系人管理
     * 
     * @param crmContact 联系人管理
     * @return 结果
     */
    @Override
    public int insertCrmContact(CrmContact crmContact)
    {
        crmContact.setCreateTime(DateUtils.getNowDate());
        return crmContactMapper.insertCrmContact(crmContact);
    }

    /**
     * 修改联系人管理
     * 
     * @param crmContact 联系人管理
     * @return 结果
     */
    @Override
    public int updateCrmContact(CrmContact crmContact)
    {
        crmContact.setUpdateTime(DateUtils.getNowDate());
        return crmContactMapper.updateCrmContact(crmContact);
    }

    /**
     * 批量删除联系人管理
     * 
     * @param contactIds 需要删除的联系人管理主键
     * @return 结果
     */
    @Override
    public int deleteCrmContactByContactIds(Long[] contactIds)
    {
        return crmContactMapper.deleteCrmContactByContactIds(contactIds);
    }

    /**
     * 删除联系人管理信息
     * 
     * @param contactId 联系人管理主键
     * @return 结果
     */
    @Override
    public int deleteCrmContactByContactId(Long contactId)
    {
        return crmContactMapper.deleteCrmContactByContactId(contactId);
    }
}
