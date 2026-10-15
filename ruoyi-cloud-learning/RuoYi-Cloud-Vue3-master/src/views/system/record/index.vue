<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="所属客户 ID，逻辑关联客户表" prop="customerId">
        <el-input
          v-model="queryParams.customerId"
          placeholder="请输入所属客户 ID，逻辑关联客户表"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="客户联系人 ID；不指定联系人时为空" prop="contactId">
        <el-input
          v-model="queryParams.contactId"
          placeholder="请输入客户联系人 ID；不指定联系人时为空"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="实际跟进时间，精确到秒" prop="followTime">
        <el-date-picker clearable
          v-model="queryParams.followTime"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择实际跟进时间，精确到秒">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="本次约定的下次跟进时间" prop="nextFollowTime">
        <el-date-picker clearable
          v-model="queryParams.nextFollowTime"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择本次约定的下次跟进时间">
        </el-date-picker>
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
          v-hasPermi="['system:record:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:record:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:record:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['system:record:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="recordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键，跟进记录 ID" align="center" prop="followId" />
      <el-table-column label="所属客户 ID，逻辑关联客户表" align="center" prop="customerId" />
      <el-table-column label="客户联系人 ID；不指定联系人时为空" align="center" prop="contactId" />
      <el-table-column label="跟进方式：0 电话、1 微信、2 拜访、3 其他" align="center" prop="followType" />
      <el-table-column label="跟进内容，业务上禁止空字符串和纯空格" align="center" prop="content" />
      <el-table-column label="实际跟进时间，精确到秒" align="center" prop="followTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.followTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="本次约定的下次跟进时间" align="center" prop="nextFollowTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.nextFollowTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:record:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['system:record:remove']">删除</el-button>
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

    <!-- 添加或修改CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="recordRef" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="所属客户 ID，逻辑关联客户表" prop="customerId">
              <el-input v-model="form.customerId" placeholder="请输入所属客户 ID，逻辑关联客户表" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="客户联系人 ID；不指定联系人时为空" prop="contactId">
              <el-input v-model="form.contactId" placeholder="请输入客户联系人 ID；不指定联系人时为空" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="跟进内容，业务上禁止空字符串和纯空格">
              <editor v-model="form.content" :min-height="192"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="实际跟进时间，精确到秒" prop="followTime">
              <el-date-picker clearable
                v-model="form.followTime"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="请选择实际跟进时间，精确到秒">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="本次约定的下次跟进时间" prop="nextFollowTime">
              <el-date-picker clearable
                v-model="form.nextFollowTime"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="请选择本次约定的下次跟进时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="0 正常、2 删除" prop="delFlag">
              <el-input v-model="form.delFlag" placeholder="请输入0 正常、2 删除" />
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

<script setup name="Record">
import { listRecord, getRecord, delRecord, addRecord, updateRecord } from "@/api/system/record"

const { proxy } = getCurrentInstance()

const recordList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    customerId: undefined,
    contactId: undefined,
    followType: undefined,
    content: undefined,
    followTime: undefined,
    nextFollowTime: undefined,
  },
  rules: {
    customerId: [
      { required: true, message: "所属客户 ID，逻辑关联客户表不能为空", trigger: "blur" }
    ],
    followType: [
      { required: true, message: "跟进方式：0 电话、1 微信、2 拜访、3 其他不能为空", trigger: "change" }
    ],
    content: [
      { required: true, message: "跟进内容，业务上禁止空字符串和纯空格不能为空", trigger: "blur" }
    ],
    followTime: [
      { required: true, message: "实际跟进时间，精确到秒不能为空", trigger: "blur" }
    ],
    delFlag: [
      { required: true, message: "0 正常、2 删除不能为空", trigger: "blur" }
    ],
    createBy: [
      { required: true, message: "创建者，后端取当前登录用户名不能为空", trigger: "blur" }
    ],
    createTime: [
      { required: true, message: "创建时间，后端生成不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。列表 */
function getList() {
  loading.value = true
  listRecord(queryParams.value).then(response => {
    recordList.value = response.rows
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
    followId: null,
    customerId: null,
    contactId: null,
    followType: null,
    content: null,
    followTime: null,
    nextFollowTime: null,
    delFlag: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null
  }
  proxy.resetForm("recordRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.followId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _followId = row.followId || ids.value
  getRecord(_followId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["recordRef"].validate(valid => {
    if (valid) {
      if (form.value.followId != null) {
        updateRecord(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addRecord(form.value).then(() => {
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
  const _followIds = row.followId || ids.value
  proxy.$modal.confirm('是否确认删除CRM 客户跟进记录，记录客户的跟进情况，支持多种跟进方式。编号为"' + _followIds + '"的数据项？').then(function() {
    return delRecord(_followIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('system/record/export', {
    ...queryParams.value
  }, `record_${new Date().getTime()}.xlsx`)
}

getList()
</script>
