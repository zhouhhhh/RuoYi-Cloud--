package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.CrmFollowRecordMapper;
import com.ruoyi.system.domain.CrmFollowRecord;
import com.ruoyi.system.service.ICrmFollowRecordService;

/**
 * CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-10-15
 */
@Service
public class CrmFollowRecordServiceImpl implements ICrmFollowRecordService 
{
    @Autowired
    private CrmFollowRecordMapper crmFollowRecordMapper;

    /**
     * 查询CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。
     * 
     * @param followId CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。主键
     * @return CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。
     */
    @Override
    public CrmFollowRecord selectCrmFollowRecordByFollowId(Long followId)
    {
        return crmFollowRecordMapper.selectCrmFollowRecordByFollowId(followId);
    }

    /**
     * 查询CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。列表
     * 
     * @param crmFollowRecord CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。
     * @return CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。
     */
    @Override
    public List<CrmFollowRecord> selectCrmFollowRecordList(CrmFollowRecord crmFollowRecord)
    {
        return crmFollowRecordMapper.selectCrmFollowRecordList(crmFollowRecord);
    }

    /**
     * 新增CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。
     * 
     * @param crmFollowRecord CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。
     * @return 结果
     */
    @Override
    public int insertCrmFollowRecord(CrmFollowRecord crmFollowRecord)
    {
        crmFollowRecord.setCreateTime(DateUtils.getNowDate());
        return crmFollowRecordMapper.insertCrmFollowRecord(crmFollowRecord);
    }

    /**
     * 修改CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。
     * 
     * @param crmFollowRecord CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。
     * @return 结果
     */
    @Override
    public int updateCrmFollowRecord(CrmFollowRecord crmFollowRecord)
    {
        crmFollowRecord.setUpdateTime(DateUtils.getNowDate());
        return crmFollowRecordMapper.updateCrmFollowRecord(crmFollowRecord);
    }

    /**
     * 批量删除CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。
     * 
     * @param followIds 需要删除的CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。主键
     * @return 结果
     */
    @Override
    public int deleteCrmFollowRecordByFollowIds(Long[] followIds)
    {
        return crmFollowRecordMapper.deleteCrmFollowRecordByFollowIds(followIds);
    }

    /**
     * 删除CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。信息
     * 
     * @param followId CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。主键
     * @return 结果
     */
    @Override
    public int deleteCrmFollowRecordByFollowId(Long followId)
    {
        return crmFollowRecordMapper.deleteCrmFollowRecordByFollowId(followId);
    }
}
