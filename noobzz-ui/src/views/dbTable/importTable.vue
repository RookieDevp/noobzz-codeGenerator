<template>
  <!-- 导入表 -->
  <el-dialog title="导入表" :visible.sync="visible" width="800px" top="5vh" append-to-body>
    <el-form size="small">
      <el-form-item label="数据源" prop="tableName">
        <el-select @change="changeDatasource" style="width: 90%" :clearable=true size="small" v-model="datasource" placeholder="请选择">
          <el-option
            v-for="item in datasourceOptions"
            :key="item.connectionName"
            :label="item.connectionName"
            :value="item.connectionName">
          </el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <el-form ref="queryForm" :model="queryParams" size="small" :inline="true">
      <el-form-item label="表名称" prop="tableName">
        <el-input
          v-model="queryParams.tableName"
          placeholder="请输入表名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="表描述" prop="tableComment">
        <el-input
          v-model="queryParams.tableComment"
          placeholder="请输入表描述"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row>
      <el-table ref="table" :data="dbTableList" height="260px" @row-click="clickRow" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="tableName" label="表名称" :show-overflow-tooltip="true" />
        <el-table-column prop="tableComment" label="表描述" :show-overflow-tooltip="true" />
        <el-table-column prop="createTime" label="创建时间" />
        <el-table-column prop="updateTime" label="更新时间" />
      </el-table>
      <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </el-row>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="handleImportTable">确 定</el-button>
      <el-button @click="visible = false">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { listDbTable, importTable } from '@/api/gen'
import { listDatasource } from '@/api/datasource'
export default {
  data() {
    return {
      datasourceOptions: [],
      datasource: undefined,
      // 遮罩层
      visible: false,
      // 选中数组值
      tables: [],
      // 总条数
      total: 0,
      // 表数据
      dbTableList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tableName: undefined,
        tableComment: undefined,
        datasource: undefined
      }
    }
  },
  methods: {
    // 显示弹框
    show() {
      this.getList()
      this.visible = true
    },
    clickRow(row) {
      this.$refs.table.toggleRowSelection(row)
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.tables = selection.map(item => item.tableName)
    },
    // 查询表数据
    getList() {
      if (this.datasource === undefined || this.datasource === 'master') {
        listDatasource({ pageNum: 1, pageSize: 1000 }).then(response => {
          this.datasourceOptions = response.data.list
          this.datasourceOptions.unshift({ connectionName: 'master' })
        })
      }
      if (this.queryParams.datasource !== undefined){
        listDbTable(this.queryParams).then(res => {
          if (res.code === 200) {
            this.dbTableList = res.data.list
            this.total = res.data.total
          }
        })
      }
    },
    changeDatasource(datasource){
      this.queryParams.pageNum = 1
      this.queryParams.datasource = datasource
      listDbTable(this.queryParams).then(res => {
        if (res.code === 200) {
          this.dbTableList = res.data.list
          this.total = res.data.total
        }
      })
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
    /** 导入按钮操作 */
    handleImportTable() {
      const tableNames = this.tables.join(',')
      if (tableNames === '') {
        this.$modal.msgError('请选择要导入的表')
        return
      }
      importTable({ tables: tableNames, datasource: this.datasource }).then(res => {
        this.$modal.msgSuccess(res.msg)
        if (res.code === 200) {
          this.visible = false
          this.$emit('ok')
        }
      })
    }
  }
}
</script>
