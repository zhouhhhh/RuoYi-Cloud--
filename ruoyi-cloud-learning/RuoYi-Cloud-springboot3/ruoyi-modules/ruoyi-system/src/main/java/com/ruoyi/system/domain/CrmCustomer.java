package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 客户档案对象 crm_customer
 * 
 * @author zhouhuihhhh
 * @date 2026-08-05
 */
public class CrmCustomer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 客户ID */
    private Long customerId;

    /** 客户编号 */
    @Excel(name = "客户编号")
    private String customerNo;

    /** 客户名称 */
    @Excel(name = "客户名称")
    private String customerName;

    /** 客户类型 0 个人客户 1 企业客户 */
    @Excel(name = "客户类型 0 个人客户 1 企业客户")
    private String customerType;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phone;

    /** 电子邮箱 */
    private String email;

    /** 联系地址 */
    private String address;

    /** 客户来源 0 主动开发1 客户推荐2 网络推广3 其他 */
    private String source;

    /** 客户等级 ABC */
    @Excel(name = "客户等级 ABC")
    private String level;

    /** 客户状态 0 潜在 1 跟进中 2 已成交 3 已流失 */
    @Excel(name = "客户状态 0 潜在 1 跟进中 2 已成交 3 已流失")
    private String status;

    /** 负责人ID */
    @Excel(name = "负责人ID")
    private Long ownerId;

    /** 部门ID */
    private Long deptId;

    /** 最后跟进时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最后跟进时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date lastFollowTime;

    /** 删除标志 0 正常 2 删除 */
    private String delFlag;

    public void setCustomerId(Long customerId) 
    {
        this.customerId = customerId;
    }

    public Long getCustomerId() 
    {
        return customerId;
    }

    public void setCustomerNo(String customerNo) 
    {
        this.customerNo = customerNo;
    }

    public String getCustomerNo() 
    {
        return customerNo;
    }

    public void setCustomerName(String customerName) 
    {
        this.customerName = customerName;
    }

    public String getCustomerName() 
    {
        return customerName;
    }

    public void setCustomerType(String customerType) 
    {
        this.customerType = customerType;
    }

    public String getCustomerType() 
    {
        return customerType;
    }

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    @NotBlank(message = "联系电话不能为空")
    @Pattern(
            regexp = "^1[3-9]\\d{9}$",
            message = "请输入正确的手机号码"
    )
    public String getPhone() 
    {
        return phone;
    }

    public void setEmail(String email) 
    {
        this.email = email;
    }

    public String getEmail() 
    {
        return email;
    }

    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }

    public void setSource(String source) 
    {
        this.source = source;
    }

    public String getSource() 
    {
        return source;
    }

    public void setLevel(String level) 
    {
        this.level = level;
    }

    public String getLevel() 
    {
        return level;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    @NotBlank(message = "状态不能为空")
    @Pattern(
            regexp = "^[0-3]$",
            message = "请输入正确的状态"
    )
    public String getStatus() 
    {
        return status;
    }

    public void setOwnerId(Long ownerId) 
    {
        this.ownerId = ownerId;
    }

    public Long getOwnerId() 
    {
        return ownerId;
    }

    public void setDeptId(Long deptId) 
    {
        this.deptId = deptId;
    }

    public Long getDeptId() 
    {
        return deptId;
    }

    public void setLastFollowTime(Date lastFollowTime) 
    {
        this.lastFollowTime = lastFollowTime;
    }

    public Date getLastFollowTime() 
    {
        return lastFollowTime;
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
            .append("customerId", getCustomerId())
            .append("customerNo", getCustomerNo())
            .append("customerName", getCustomerName())
            .append("customerType", getCustomerType())
            .append("phone", getPhone())
            .append("email", getEmail())
            .append("address", getAddress())
            .append("source", getSource())
            .append("level", getLevel())
            .append("status", getStatus())
            .append("ownerId", getOwnerId())
            .append("deptId", getDeptId())
            .append("lastFollowTime", getLastFollowTime())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
