<template>
  <el-drawer title="客户档案详情" v-model="visible" direction="rtl" size="60%" append-to-body :before-close="handleClose" class="detail-drawer">
    <div v-loading="loading" class="drawer-content">
      <h4 class="section-header">基本信息</h4>
      <el-row :gutter="20" class="mb8">
        <el-col :span="12">
          <div class="info-item">
            <label class="info-label">客户编号：</label>
            <span class="info-value plaintext">
              {{ info.customerNo }}
            </span>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="info-item">
            <label class="info-label">客户名称：</label>
            <span class="info-value plaintext">
              {{ info.customerName }}
            </span>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20" class="mb8">
        <el-col :span="12">
          <div class="info-item">
            <label class="info-label">客户类型 0 个人客户 1 企业客户：</label>
            <span class="info-value plaintext">
              <dict-tag :options="crm_customer_type" :value="info.customerType" />
            </span>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="info-item">
            <label class="info-label">联系电话：</label>
            <span class="info-value plaintext">
              {{ info.phone }}
            </span>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20" class="mb8">
        <el-col :span="12">
          <div class="info-item">
            <label class="info-label">客户等级 ABC：</label>
            <span class="info-value plaintext">
              <dict-tag :options="crm_customer_level" :value="info.level" />
            </span>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="info-item">
            <label class="info-label">客户状态 0 潜在 1 跟进中 2 已成交 3 已流失：</label>
            <span class="info-value plaintext">
              <dict-tag :options="crm_customer_status" :value="info.status" />
            </span>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20" class="mb8">
        <el-col :span="12">
          <div class="info-item">
            <label class="info-label">负责人ID：</label>
            <span class="info-value plaintext">
              {{ info.ownerId }}
            </span>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="info-item">
            <label class="info-label">最后跟进时间：</label>
            <span class="info-value plaintext">
              {{ parseTime(info.lastFollowTime, '{y}-{m}-{d}') }}
            </span>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20" class="mb8">
        <el-col :span="12">
          <div class="info-item">
            <label class="info-label">创建时间：</label>
            <span class="info-value plaintext">
              {{ parseTime(info.createTime, '{y}-{m}-{d}') }}
            </span>
          </div>
        </el-col>
      </el-row>
    </div>
  </el-drawer>
</template>

<script setup name="CustomerViewDrawer">
import { getCustomer } from '@/api/system/customer'

const { crm_customer_source, crm_customer_level, crm_customer_type, crm_customer_status } = useDict('crm_customer_source', 'crm_customer_level', 'crm_customer_type', 'crm_customer_status')

const visible = ref(false)
const loading = ref(false)
const info = reactive({})

const open = async (customerId) => {
  visible.value = true
  loading.value = true
  try {
    const res = await getCustomer(customerId)
    Object.assign(info, res.data || {})
  } catch (error) {
    console.error('获取客户档案信息失败:', error)
  } finally {
    loading.value = false
  }
}

function handleClose() {
  visible.value = false
  Object.keys(info).forEach(key => delete info[key])
}

defineExpose({ open })
</script>
