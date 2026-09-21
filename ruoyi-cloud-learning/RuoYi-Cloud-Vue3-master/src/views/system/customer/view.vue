<template>
    <el-drawer title="客户档案详情" v-model="visible" direction="rtl" size="60%" append-to-body :before-close="handleClose"
        class="detail-drawer">
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
                        <label class="info-label">客户类型：</label>
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
                        <label class="info-label">电子邮箱：</label>
                        <span class="info-value plaintext">
                            {{ info.email }}
                        </span>
                    </div>
                </el-col>
                <el-col :span="12">
                    <div class="info-item">
                        <label class="info-label">联系地址：</label>
                        <span class="info-value plaintext">
                            {{ info.address }}
                        </span>
                    </div>
                </el-col>
            </el-row>
            <el-row :gutter="20" class="mb8">
                <el-col :span="12">
                    <div class="info-item">
                        <label class="info-label">客户来源：</label>
                        <span class="info-value plaintext">
                            <dict-tag :options="crm_customer_source" :value="info.source" />
                        </span>
                    </div>
                </el-col>
            </el-row>
            <el-row :gutter="20" class="mb8">
                <el-col :span="12">
                    <div class="info-item">
                        <label class="info-label">客户等级：</label>
                        <span class="info-value plaintext">
                            <dict-tag :options="crm_customer_level" :value="info.level" />
                        </span>
                    </div>
                </el-col>
                <el-col :span="12">
                    <div class="info-item">
                        <label class="info-label">客户状态：</label>
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
                        <label class="info-label">部门ID：</label>
                        <span class="info-value plaintext">
                            {{ info.deptId }}
                        </span>
                    </div>
                </el-col>
            </el-row>
            <el-row :gutter="20" class="mb8">
                <el-col :span="12">
                    <div class="info-item">
                        <label class="info-label">最后跟进时间：</label>
                        <span class="info-value plaintext">
                            {{
                                parseTime(
                                    info.lastFollowTime,
                                    '{y}-{m}-{d} {h}:{i}:{s}',
                                )
                            }}
                        </span>
                    </div>
                </el-col>
                <el-col :span="12">
                    <div class="info-item">
                        <label class="info-label">创建时间：</label>
                        <span class="info-value plaintext">
                            {{
                                parseTime(
                                    info.createTime,
                                    '{y}-{m}-{d} {h}:{i}:{s}',
                                )
                            }}
                        </span>
                    </div>
                </el-col>
            </el-row>
            <el-row :gutter="20" class="mb8">
                <el-col :span="12">
                    <div class="info-item">
                        <label class="info-label">更新时间：</label>
                        <span class="info-value plaintext">
                            {{
                                parseTime(
                                    info.updateTime,
                                    '{y}-{m}-{d} {h}:{i}:{s}',
                                )
                            }}
                        </span>
                    </div>
                </el-col>
                <el-col :span="12">
                    <div class="info-item">
                        <label class="info-label">备注：</label>
                        <span class="info-value plaintext">
                            {{ info.remark }}
                        </span>
                    </div>
                </el-col>
            </el-row>
            <h4 class="section-header">联系人</h4>
            <el-button type="primary" plain icon="Plus" class="mb8" @click="handleAddContact"
                v-hasPermi="['system:contact:add']">
                新增联系人
            </el-button>
            <el-table v-loading="contactLoading" :data="contactListByCustomerId" empty-text="暂无联系人">
                <el-table-column label="联系人ID" align="center" prop="contactId" />
                <el-table-column label="联系人姓名" align="center" prop="contactName" />
                <el-table-column label="联系电话" align="center" prop="phone" />
                <el-table-column label="备注" align="center" prop="remark" />
                <el-table-column label="创建时间" align="center" prop="createTime" />
            </el-table>
            <pagination v-show="contactTotal > 0" :total="contactTotal" v-model:page="contactQueryParams.pageNum"
                v-model:limit="contactQueryParams.pageSize" @pagination="fetchContactListByCustomerId" />
        </div>

        <!-- 新增联系人对话框 -->
        <el-dialog v-model="contactDialogVisible" title="新增联系人" width="500px" append-to-body>
            <el-form ref="contactFormRef" :model="contactForm" :rules="contactRules" label-width="100px">
                <el-form-item label="所属客户" prop="customerId">
                    <el-input :model-value="`${info.customerName}（ID：${contactForm.customerId}）`" disabled />
                </el-form-item>

                <el-form-item label="联系人姓名" prop="contactName">
                    <el-input v-model="contactForm.contactName" placeholder="请输入联系人姓名" />
                </el-form-item>

                <el-form-item label="联系电话">
                    <el-input v-model="contactForm.phone" placeholder="请输入联系电话" />
                </el-form-item>

                <el-form-item label="备注">
                    <el-input v-model="contactForm.remark" type="textarea" placeholder="请输入备注" />
                </el-form-item>
            </el-form>

            <template #footer>
                <el-button type="primary" :loading="contactSubmitting" @click="submitContact">
                    确定
                </el-button>
                <el-button @click="contactDialogVisible = false">取消</el-button>
            </template>
        </el-dialog>
    </el-drawer>
</template>

<script setup name="CustomerViewDrawer">
import { getCustomer } from '@/api/system/customer'
import { listContact, addContact } from '@/api/system/contact'

const { proxy } = getCurrentInstance()
const {
    crm_customer_source,
    crm_customer_level,
    crm_customer_type,
    crm_customer_status,
} = useDict(
    'crm_customer_source',
    'crm_customer_level',
    'crm_customer_type',
    'crm_customer_status',
)

const visible = ref(false)
const loading = ref(false)
const info = reactive({})

const contactLoading = ref(false)
const contactListByCustomerId = ref([])
const contactQueryParams = reactive({
    pageNum: 1,
    pageSize: 5,
    customerId: null,
})
const contactTotal = ref(0)

const contactFormRef = ref();
const contactDialogVisible = ref(false);
const contactSubmitting = ref(false);

const contactForm = reactive({
  customerId: null,
  contactName: "",
  phone: "",
  remark: ""
});

const contactRules = {
  customerId: [
    { required: true, message: "所属客户不能为空", trigger: "change" }
  ],
  contactName: [
    { required: true, message: "联系人姓名不能为空", trigger: "blur" }
  ]
};

const open = async (customerId) => {
    visible.value = true
    loading.value = true
    contactListByCustomerId.value = []
    contactQueryParams.customerId = customerId
    contactQueryParams.pageNum = 1
    try {
        const res = await getCustomer(customerId)
        Object.assign(info, res.data || {})
        const contactRes = await listContact(contactQueryParams)
        contactListByCustomerId.value = contactRes.rows || []
        contactTotal.value = contactRes.total || 0
    } catch (error) {
        console.error('获取客户档案信息失败:', error)
    } finally {
        loading.value = false
    }
}

function handleClose () {
    visible.value = false
    contactLoading.value = false
    contactListByCustomerId.value = []
    contactQueryParams.pageNum = 1
    contactQueryParams.pageSize = 5
    contactQueryParams.customerId = null
    contactTotal.value = 0
    Object.keys(info).forEach((key) => delete info[key])
}

function fetchContactListByCustomerId () {
    contactLoading.value = true
    contactQueryParams.customerId = info.customerId
    listContact(contactQueryParams)
        .then((res) => {
            contactListByCustomerId.value = res.rows || []
            contactTotal.value = res.total || 0
        })
        .catch((error) => {
            console.error('获取联系人列表失败:', error)
        })
        .finally(() => {
            contactLoading.value = false
        })
}

function handleAddContact() {
  Object.assign(contactForm, {
    customerId: info.customerId,
    contactName: "",
    phone: "",
    remark: ""
  });

  contactDialogVisible.value = true;

  nextTick(() => {
    contactFormRef.value?.clearValidate();
  });
}

async function submitContact() {
  const valid = await contactFormRef.value
    .validate()
    .catch(() => false);

  if (!valid) {
    return;
  }

  contactSubmitting.value = true;

  try {
    await addContact({ ...contactForm });
    proxy.$modal.msgSuccess("新增成功");
    contactDialogVisible.value = false;

    contactQueryParams.pageNum = 1;
    fetchContactListByCustomerId();
  } finally {
    contactSubmitting.value = false;
  }
}

defineExpose({ open })
</script>
