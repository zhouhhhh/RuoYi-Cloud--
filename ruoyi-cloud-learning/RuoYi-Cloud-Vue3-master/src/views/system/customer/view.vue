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
                <el-table-column label="操作" align="center" width="210" class-name="small-padding fixed-width">
                    <template #default="scope">
                        <el-button link type="primary" icon="Edit" @click="handleContactUpdate(scope.row)"
                            v-hasPermi="['system:contact:edit']">修改</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <pagination v-show="contactTotal > 0" :total="contactTotal" v-model:page="contactQueryParams.pageNum"
                v-model:limit="contactQueryParams.pageSize" @pagination="fetchContactListByCustomerId" />
        </div>

        <!-- 新增或修改联系人对话框 -->
        <el-dialog v-model="contactDialogVisible" :title="contactDialogTitle" width="500px" append-to-body>
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
import { listContact, addContact, getContact, updateContact } from '@/api/system/contact'
import { reactive } from 'vue'

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
const contactTotal = ref(0)
const contactFormRef = ref()
const contactDialogVisible = ref(false)
const contactSubmitting = ref(false)
const contactDialogTitle = ref("")

const data = reactive({
    contactForm: {},
    contactQueryParams: {
        pageNum: 1,
        pageSize: 5,
        customerId: null
    },
    contactRules: {
        customerId: [
            { required: true, message: "所属客户不能为空", trigger: "change" }
        ],
        contactName: [
            { required: true, message: "联系人姓名不能为空", trigger: "blur" }
        ]
    }
})

const { contactForm, contactQueryParams, contactRules } = toRefs(data)

/** 联系人表单重置 */
function contactFormReset () {
    contactForm.value = {
        customerId: null,
        contactId: null,
        contactName: "",
        phone: "",
        remark: ""
    }
}

/** 打开客户档案详情抽屉 */
const open = async (customerId) => {
    visible.value = true
    loading.value = true
    contactListByCustomerId.value = []
    contactQueryParams.value.customerId = customerId
    contactQueryParams.value.pageNum = 1
    try {
        const res = await getCustomer(customerId)
        Object.assign(info, res.data || {})
        const contactRes = await listContact(contactQueryParams.value)
        contactListByCustomerId.value = contactRes.rows || []
        contactTotal.value = contactRes.total || 0
    } catch (error) {
        console.error('获取客户档案信息失败:', error)
    } finally {
        loading.value = false
    }
}

/** 关闭客户档案详情抽屉 */
function handleClose () {
    visible.value = false
    contactLoading.value = false
    contactListByCustomerId.value = []
    contactQueryParams.value.pageNum = 1
    contactQueryParams.value.pageSize = 5
    contactQueryParams.value.customerId = null
    contactTotal.value = 0
    Object.keys(info).forEach((key) => delete info[key])
}

/** 根据客户ID获取联系人列表 */
function fetchContactListByCustomerId () {
    contactLoading.value = true
    contactQueryParams.value.customerId = info.customerId
    listContact(contactQueryParams.value)
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

/** 新增联系人 */
function handleAddContact () {
    contactFormReset();
    contactForm.value.customerId = info.customerId;

    contactDialogVisible.value = true;
    contactDialogTitle.value = "新增联系人";

    nextTick(() => {
        contactFormRef.value?.clearValidate();
    });
}

/* 提交联系人表单 */
async function submitContact () {
    const valid = await contactFormRef.value
        .validate()
        .catch(() => false);

    if (!valid) {
        return;
    }

    contactSubmitting.value = true;

    try {
        if (contactForm.value.contactId) {
            await updateContact(contactForm.value);
            proxy.$modal.msgSuccess("修改成功");
            contactDialogVisible.value = false;

            fetchContactListByCustomerId();
        } else {
            await addContact(contactForm.value);
            proxy.$modal.msgSuccess("新增成功");
            contactDialogVisible.value = false;

            contactQueryParams.value.pageNum = 1;
            fetchContactListByCustomerId();
        }
    } finally {
        contactSubmitting.value = false;
    }
}

/** 修改联系人 */
async function handleContactUpdate (row) {
    contactFormReset();
    const response = await getContact(row.contactId)
    const contact = response.data

    if (!contact) {
        proxy.$modal.msgError("联系人不存在或已删除")
        fetchContactListByCustomerId()
        return
    }

    contactForm.value = {
        customerId: contact.customerId,
        contactId: contact.contactId,
        contactName: contact.contactName,
        phone: contact.phone,
        remark: contact.remark
    }

    contactDialogTitle.value = "修改联系人"
    contactDialogVisible.value = true

    nextTick(() => {
        contactFormRef.value?.clearValidate()
    })
}

defineExpose({ open })
</script>
