package com.ruoyi.system.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.system.domain.CrmFollowRecord;
import com.ruoyi.system.service.ICrmFollowRecordService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。Controller
 * 
 * @author ruoyi
 * @date 2026-10-15
 */
@RestController
@RequestMapping("/followRecord")
public class CrmFollowRecordController extends BaseController
{
    @Autowired
    private ICrmFollowRecordService crmFollowRecordService;

    /**
     * 查询CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。列表
     */
    @RequiresPermissions("system:record:list")
    @GetMapping("/list")
    public TableDataInfo list(CrmFollowRecord crmFollowRecord)
    {
        startPage();
        List<CrmFollowRecord> list = crmFollowRecordService.selectCrmFollowRecordList(crmFollowRecord);
        return getDataTable(list);
    }

    /**
     * 导出CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。列表
     */
    @RequiresPermissions("system:record:export")
    @Log(title = "CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CrmFollowRecord crmFollowRecord)
    {
        List<CrmFollowRecord> list = crmFollowRecordService.selectCrmFollowRecordList(crmFollowRecord);
        ExcelUtil<CrmFollowRecord> util = new ExcelUtil<CrmFollowRecord>(CrmFollowRecord.class);
        util.exportExcel(response, list, "CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。数据");
    }

    /**
     * 获取CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。详细信息
     */
    @RequiresPermissions("system:record:query")
    @GetMapping(value = "/{followId}")
    public AjaxResult getInfo(@PathVariable("followId") Long followId)
    {
        return success(crmFollowRecordService.selectCrmFollowRecordByFollowId(followId));
    }

    /**
     * 新增CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。
     */
    @RequiresPermissions("system:record:add")
    @Log(title = "CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CrmFollowRecord crmFollowRecord)
    {
        return toAjax(crmFollowRecordService.insertCrmFollowRecord(crmFollowRecord));
    }

    /**
     * 修改CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。
     */
    @RequiresPermissions("system:record:edit")
    @Log(title = "CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CrmFollowRecord crmFollowRecord)
    {
        return toAjax(crmFollowRecordService.updateCrmFollowRecord(crmFollowRecord));
    }

    /**
     * 删除CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。
     */
    @RequiresPermissions("system:record:remove")
    @Log(title = "CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。", businessType = BusinessType.DELETE)
	@DeleteMapping("/{followIds}")
    public AjaxResult remove(@PathVariable Long[] followIds)
    {
        return toAjax(crmFollowRecordService.deleteCrmFollowRecordByFollowIds(followIds));
    }
}
