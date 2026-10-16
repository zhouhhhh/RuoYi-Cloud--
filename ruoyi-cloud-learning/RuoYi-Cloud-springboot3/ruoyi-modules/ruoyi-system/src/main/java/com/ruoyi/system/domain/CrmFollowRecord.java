package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。对象 crm_follow_record
 * 
 * @author ruoyi
 * @date 2026-10-16
 */
public class CrmFollowRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键，跟进记录 ID */
    private Long followId;

    /** 所属客户 ID，逻辑关联客户表 */
    @Excel(name = "所属客户 ID，逻辑关联客户表")
    private Long customerId;

    /** 客户联系人 ID；不指定联系人时为空 */
    @Excel(name = "客户联系人 ID；不指定联系人时为空")
    private Long contactId;

    /** 跟进方式：0 电话、1 微信、2 拜访、3 其他 */
    @Excel(name = "跟进方式：0 电话、1 微信、2 拜访、3 其他")
    private String followType;

    /** 跟进内容，业务上禁止空字符串和纯空格 */
    @Excel(name = "跟进内容，业务上禁止空字符串和纯空格")
    private String content;

    /** 实际跟进时间，精确到秒 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "实际跟进时间，精确到秒", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date followTime;

    /** 本次约定的下次跟进时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "本次约定的下次跟进时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date nextFollowTime;

    /** 0 正常、2 删除 */
    private String delFlag;

    public void setFollowId(Long followId) 
    {
        this.followId = followId;
    }

    public Long getFollowId() 
    {
        return followId;
    }

    public void setCustomerId(Long customerId) 
    {
        this.customerId = customerId;
    }

    @NotNull(message = "所属客户不能为空")
    public Long getCustomerId() 
    {
        return customerId;
    }

    public void setContactId(Long contactId) 
    {
        this.contactId = contactId;
    }

    public Long getContactId() 
    {
        return contactId;
    }

    public void setFollowType(String followType) 
    {
        this.followType = followType;
    }

    @NotBlank(message = "跟进方式不能为空")
    @Pattern(regexp = "[0-3]", message = "跟进方式只能为0、1、2、3")
    public String getFollowType() 
    {
        return followType;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    @NotBlank(message = "跟进内容不能为空")
    @Size(max = 2000, message = "跟进内容不能超过2000个字符")
    public String getContent() 
    {
        return content;
    }

    public void setFollowTime(Date followTime) 
    {
        this.followTime = followTime;
    }

    @NotNull(message = "跟进时间不能为空")
    public Date getFollowTime() 
    {
        return followTime;
    }

    public void setNextFollowTime(Date nextFollowTime) 
    {
        this.nextFollowTime = nextFollowTime;
    }

    public Date getNextFollowTime() 
    {
        return nextFollowTime;
    }

    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("followId", getFollowId())
            .append("customerId", getCustomerId())
            .append("contactId", getContactId())
            .append("followType", getFollowType())
            .append("content", getContent())
            .append("followTime", getFollowTime())
            .append("nextFollowTime", getNextFollowTime())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
