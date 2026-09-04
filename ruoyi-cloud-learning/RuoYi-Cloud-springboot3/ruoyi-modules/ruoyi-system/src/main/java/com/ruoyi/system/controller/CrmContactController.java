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
import com.ruoyi.system.domain.CrmContact;
import com.ruoyi.system.service.ICrmContactService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 联系人管理Controller
 * 
 * @author zhouhuihhhh
 * @date 2026-09-04
 */
@RestController
@RequestMapping("/contact")
public class CrmContactController extends BaseController
{
    @Autowired
    private ICrmContactService crmContactService;

    /**
     * 查询联系人管理列表
     */
    @RequiresPermissions("system:contact:list")
    @GetMapping("/list")
    public TableDataInfo list(CrmContact crmContact)
    {
        startPage();
        List<CrmContact> list = crmContactService.selectCrmContactList(crmContact);
        return getDataTable(list);
    }

    /**
     * 导出联系人管理列表
     */
    @RequiresPermissions("system:contact:export")
    @Log(title = "联系人管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CrmContact crmContact)
    {
        List<CrmContact> list = crmContactService.selectCrmContactList(crmContact);
        ExcelUtil<CrmContact> util = new ExcelUtil<CrmContact>(CrmContact.class);
        util.exportExcel(response, list, "联系人管理数据");
    }

    /**
     * 获取联系人管理详细信息
     */
    @RequiresPermissions("system:contact:query")
    @GetMapping(value = "/{contactId}")
    public AjaxResult getInfo(@PathVariable("contactId") Long contactId)
    {
        return success(crmContactService.selectCrmContactByContactId(contactId));
    }

    /**
     * 新增联系人管理
     */
    @RequiresPermissions("system:contact:add")
    @Log(title = "联系人管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CrmContact crmContact)
    {
        return toAjax(crmContactService.insertCrmContact(crmContact));
    }

    /**
     * 修改联系人管理
     */
    @RequiresPermissions("system:contact:edit")
    @Log(title = "联系人管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CrmContact crmContact)
    {
        return toAjax(crmContactService.updateCrmContact(crmContact));
    }

    /**
     * 删除联系人管理
     */
    @RequiresPermissions("system:contact:remove")
    @Log(title = "联系人管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{contactIds}")
    public AjaxResult remove(@PathVariable Long[] contactIds)
    {
        return toAjax(crmContactService.deleteCrmContactByContactIds(contactIds));
    }
}
