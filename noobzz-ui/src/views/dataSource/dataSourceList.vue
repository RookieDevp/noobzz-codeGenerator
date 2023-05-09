<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="68px">
      <el-form-item label="连接名" prop="connectionName">
        <el-input
          v-model="queryParams.connectionName"
          placeholder="请输入连接名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="datasourceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="数据源ID" align="center" prop="datasourceId" />
      <el-table-column label="连接名" align="center" prop="connectionName" />
      <el-table-column label="连接url" align="center" prop="url" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ scope.row.url }}</span>
          <el-link
            v-clipboard:copy="scope.row.url"
            v-clipboard:success="clipboardSuccess"
            :underline="false"
            icon="el-icon-document-copy"
            style="float:right"
          >
          </el-link>
        </template>
      </el-table-column>
      <el-table-column label="用户名" align="center" prop="username" />
      <el-table-column label="密码" align="center" prop="password" />
      <el-table-column label="数据库类型" align="center" prop="dataBaseType" />
      <el-table-column label="数据状态" align="center" prop="status">
        <template slot-scope="scope">
          <span>{{ scope.row.status === '0' ? '正常' : '停用'}}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200px">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleConnection(scope.row)"
          >测试连接</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改数据源对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="连接名" prop="connectionName">
          <el-input v-model="form.connectionName" placeholder="请输入连接名" />
        </el-form-item>
        <el-form-item label="连接url" prop="url">
          <el-input v-model="form.url" placeholder="请输入连接url" />
        </el-form-item>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="数据库类型" prop="dataBaseType">
          <el-select v-model="form.dataBaseType" placeholder="请选择">
            <el-option
              v-for="item in dataBaseTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button
          type="success"
          @click="handleConnection(form)"
        >测试连接</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

import {
  addDatasource,
  delDatasource,
  getDatasource,
  listDatasource,
  testDatasource,
  updateDatasource
} from '@/api/datasource'

export default {
  name: 'Datasource',
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 数据源表格数据
      datasourceList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      dataBaseTypeOptions: [
        {
          label: 'Mysql',
          value: 'Mysql'
        }
      ],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        createTime: [
          { required: true, message: '创建时间不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '数据状态不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询数据源列表 */
    getList() {
      this.loading = true
      listDatasource(this.queryParams).then(response => {
        this.datasourceList = response.data.list
        this.total = response.data.total
        this.loading = false
      })
    },
    /** 复制代码成功 */
    clipboardSuccess() {
      this.$modal.msgSuccess('复制成功')
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        datasourceId: null,
        connectionName: null,
        url: null,
        username: null,
        password: null,
        dataBaseType: null,
        createTime: null,
        updateTime: null,
        status: '0'
      }
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.datasourceId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.form.dataBaseType = 'Mysql'
      this.title = '添加数据源'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const datasourceId = row.datasourceId || this.ids
      getDatasource(datasourceId).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改数据源'
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.datasourceId != null) {
            updateDatasource(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addDatasource(this.form).then(response => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const datasourceIds = row.datasourceId || this.ids
      this.$modal.confirm('是否确认删除数据源编号为"' + datasourceIds + '"的数据项？').then(function() {
        return delDatasource(datasourceIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('blog/datasource/export', {
        ...this.queryParams
      }, `datasource_${new Date().getTime()}.xlsx`)
    },
    handleConnection(row) {
      testDatasource(row).then(response => {
        this.$modal.msgSuccess(response.msg)
      })
    }
  }
}
</script>
