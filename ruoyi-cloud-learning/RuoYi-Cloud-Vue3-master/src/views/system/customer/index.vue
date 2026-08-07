<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="客户编号" prop="customerNo">
        <el-input
          v-model="queryParams.customerNo"
          placeholder="请输入客户编号"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="客户名称" prop="customerName">
        <el-input
          v-model="queryParams.customerName"
          placeholder="请输入客户名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="客户类型 0 个人客户 1 企业客户" prop="customerType">
        <el-select v-model="queryParams.customerType" placeholder="请选择客户类型 0 个人客户 1 企业客户" clearable>
          <el-option
            v-for="dict in crm_customer_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="联系电话" prop="phone">
        <el-input
          v-model="queryParams.phone"
          placeholder="请输入联系电话"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="客户状态 0 潜在 1 跟进中 2 已成交 3 已流失" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择客户状态 0 潜在 1 跟进中 2 已成交 3 已流失" clearable>
          <el-option
            v-for="dict in crm_customer_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="负责人ID" prop="ownerId">
        <el-input
          v-model="queryParams.ownerId"
          placeholder="请输入负责人ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间" style="width: 308px">
        <el-date-picker
          v-model="daterangeCreateTime"
          value-format="YYYY-MM-DD"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['system:customer:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:customer:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:customer:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['system:customer:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="customerList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="客户ID" align="center" prop="customerId" />
      <el-table-column label="客户编号" align="center" prop="customerNo" />
      <el-table-column label="客户名称" align="center" prop="customerName" />
      <el-table-column label="客户类型 0 个人客户 1 企业客户" align="center" prop="customerType">
        <template #default="scope">
          <dict-tag :options="crm_customer_type" :value="scope.row.customerType"/>
        </template>
      </el-table-column>
      <el-table-column label="联系电话" align="center" prop="phone" />
      <el-table-column label="客户等级 ABC" align="center" prop="level">
        <template #default="scope">
          <dict-tag :options="crm_customer_level" :value="scope.row.level"/>
        </template>
      </el-table-column>
      <el-table-column label="客户状态 0 潜在 1 跟进中 2 已成交 3 已流失" align="center" prop="status">
        <template #default="scope">
          <dict-tag :options="crm_customer_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="负责人ID" align="center" prop="ownerId" />
      <el-table-column label="最后跟进时间" align="center" prop="lastFollowTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.lastFollowTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="View" @click="handleViewData(scope.row)" v-hasPermi="['system:customer:query']">详情</el-button>
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:customer:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['system:customer:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 客户档案详情抽屉 -->
    <customer-view-drawer ref="customerViewRef" />
    <!-- 添加或修改客户档案对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="customerRef" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="客户编号" prop="customerNo">
              <el-input v-model="form.customerNo" placeholder="请输入客户编号" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="客户名称" prop="customerName">
              <el-input v-model="form.customerName" placeholder="请输入客户名称" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="客户类型 0 个人客户 1 企业客户" prop="customerType">
              <el-select v-model="form.customerType" placeholder="请选择客户类型 0 个人客户 1 企业客户">
                <el-option
                  v-for="dict in crm_customer_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="电子邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入电子邮箱" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="联系地址" prop="address">
              <el-input v-model="form.address" placeholder="请输入联系地址" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="客户来源 0 主动开发1 客户推荐2 网络推广3 其他" prop="source">
              <el-select v-model="form.source" placeholder="请选择客户来源 0 主动开发1 客户推荐2 网络推广3 其他">
                <el-option
                  v-for="dict in crm_customer_source"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="客户等级 ABC" prop="level">
              <el-select v-model="form.level" placeholder="请选择客户等级 ABC">
                <el-option
                  v-for="dict in crm_customer_level"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="客户状态 0 潜在 1 跟进中 2 已成交 3 已流失" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in crm_customer_status"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="负责人ID" prop="ownerId">
              <el-input v-model="form.ownerId" placeholder="请输入负责人ID" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="部门ID" prop="deptId">
              <el-input v-model="form.deptId" placeholder="请输入部门ID" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Customer">
import { listCustomer, getCustomer, delCustomer, addCustomer, updateCustomer } from "@/api/system/customer"
import CustomerViewDrawer from "./view"

const { proxy } = getCurrentInstance()
const { crm_customer_source, crm_customer_level, crm_customer_type, crm_customer_status } = useDict('crm_customer_source', 'crm_customer_level', 'crm_customer_type', 'crm_customer_status')

const customerList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const daterangeCreateTime = ref([])

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    customerNo: undefined,
    customerName: undefined,
    customerType: undefined,
    phone: undefined,
    status: undefined,
    ownerId: undefined,
    createTime: undefined,
  },
  rules: {
    customerNo: [
      { required: true, message: "客户编号不能为空", trigger: "blur" }
    ],
    customerName: [
      { required: true, message: "客户名称不能为空", trigger: "blur" }
    ],
    customerType: [
      { required: true, message: "客户类型 0 个人客户 1 企业客户不能为空", trigger: "change" }
    ],
    phone: [
      { required: true, message: "联系电话不能为空", trigger: "blur" }
    ],
    source: [
      { required: true, message: "客户来源 0 主动开发1 客户推荐2 网络推广3 其他不能为空", trigger: "change" }
    ],
    level: [
      { required: true, message: "客户等级 ABC不能为空", trigger: "change" }
    ],
    status: [
      { required: true, message: "客户状态 0 潜在 1 跟进中 2 已成交 3 已流失不能为空", trigger: "change" }
    ],
    ownerId: [
      { required: true, message: "负责人ID不能为空", trigger: "blur" }
    ],
    deptId: [
      { required: true, message: "部门ID不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询客户档案列表 */
function getList() {
  loading.value = true
  queryParams.value.params = {}
  if (null != daterangeCreateTime.value && '' != daterangeCreateTime.value) {
    queryParams.value.params["beginCreateTime"] = daterangeCreateTime.value[0]
    queryParams.value.params["endCreateTime"] = daterangeCreateTime.value[1]
  }
  listCustomer(queryParams.value).then(response => {
    customerList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

/** 取消按钮 */
function cancel() {
  open.value = false
  reset()
}

/** 表单重置 */
function reset() {
  form.value = {
    customerId: null,
    customerNo: null,
    customerName: null,
    customerType: null,
    phone: null,
    email: null,
    address: null,
    source: null,
    level: null,
    status: null,
    ownerId: null,
    deptId: null,
    lastFollowTime: null,
    delFlag: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
    remark: null
  }
  proxy.resetForm("customerRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  daterangeCreateTime.value = []
  proxy.resetForm("queryRef")
  handleQuery()
}

/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.customerId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加客户档案"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _customerId = row.customerId || ids.value
  getCustomer(_customerId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改客户档案"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["customerRef"].validate(valid => {
    if (valid) {
      if (form.value.customerId != null) {
        updateCustomer(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addCustomer(form.value).then(() => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _customerIds = row.customerId || ids.value
  proxy.$modal.confirm('是否确认删除客户档案编号为"' + _customerIds + '"的数据项？').then(function() {
    return delCustomer(_customerIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 详情按钮操作 */
function handleViewData(row) {
  proxy.$refs["customerViewRef"].open(row.customerId)
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('system/customer/export', {
    ...queryParams.value
  }, `customer_${new Date().getTime()}.xlsx`)
}

getList()
</script>
