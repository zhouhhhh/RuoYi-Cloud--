package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.CrmFollowRecord;

/**
 * CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。Mapper接口
 * 
 * @author ruoyi
 * @date 2026-10-15
 */
public interface CrmFollowRecordMapper 
{
    /**
     * 查询CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。
     * 
     * @param followId CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。主键
     * @return CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。
     */
    public CrmFollowRecord selectCrmFollowRecordByFollowId(Long followId);

    /**
     * 查询CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。列表
     * 
     * @param crmFollowRecord CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。
     * @return CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。集合
     */
    public List<CrmFollowRecord> selectCrmFollowRecordList(CrmFollowRecord crmFollowRecord);

    /**
     * 新增CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。
     * 
     * @param crmFollowRecord CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。
     * @return 结果
     */
    public int insertCrmFollowRecord(CrmFollowRecord crmFollowRecord);

    /**
     * 修改CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。
     * 
     * @param crmFollowRecord CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。
     * @return 结果
     */
    public int updateCrmFollowRecord(CrmFollowRecord crmFollowRecord);

    /**
     * 删除CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。
     * 
     * @param followId CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。主键
     * @return 结果
     */
    public int deleteCrmFollowRecordByFollowId(Long followId);

    /**
     * 批量删除CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。
     * 
     * @param followIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCrmFollowRecordByFollowIds(Long[] followIds);
}
