package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.CrmContact;

/**
 * 联系人管理Service接口
 * 
 * @author zhouhuihhhh
 * @date 2026-09-04
 */
public interface ICrmContactService 
{
    /**
     * 查询联系人管理
     * 
     * @param contactId 联系人管理主键
     * @return 联系人管理
     */
    public CrmContact selectCrmContactByContactId(Long contactId);

    /**
     * 查询联系人管理列表
     * 
     * @param crmContact 联系人管理
     * @return 联系人管理集合
     */
    public List<CrmContact> selectCrmContactList(CrmContact crmContact);

    /**
     * 新增联系人管理
     * 
     * @param crmContact 联系人管理
     * @return 结果
     */
    public int insertCrmContact(CrmContact crmContact);

    /**
     * 修改联系人管理
     * 
     * @param crmContact 联系人管理
     * @return 结果
     */
    public int updateCrmContact(CrmContact crmContact);

    /**
     * 批量删除联系人管理
     * 
     * @param contactIds 需要删除的联系人管理主键集合
     * @return 结果
     */
    public int deleteCrmContactByContactIds(Long[] contactIds);

    /**
     * 删除联系人管理信息
     * 
     * @param contactId 联系人管理主键
     * @return 结果
     */
    public int deleteCrmContactByContactId(Long contactId);
}
