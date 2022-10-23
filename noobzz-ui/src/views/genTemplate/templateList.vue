<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :model="queryParams" size="small" :inline="true" label-width="68px">
      <el-form-item label="模板名称" prop="templateName">
        <el-input
          v-model="queryParams.templateName"
          placeholder="请输入模板名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="模板组" prop="group">
        <el-input
          v-model="queryParams.group"
          placeholder="请输入模板组"
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
          @click="handleAddTemplate"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload"
          size="mini"
          @click="ImportTemplate"
        >导入
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleEditTable"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >删除
        </el-button>
      </el-col>
      <!--      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />-->
    </el-row>

    <el-table
      v-if="refreshTable"
      v-loading="loading"
      :data="templateList"
      row-key="templateId"
      :default-expand-all="isExpandAll"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column prop="templateName" label="模板名称" width="200"></el-table-column>
      <el-table-column prop="fileName" label="文件名称" width="200"></el-table-column>
      <el-table-column prop="fileType" label="文件类型" width="200"></el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.status === '0' ? '正常' : '停用'}}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="200"/>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="200"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改
          </el-button>
          <el-button
            v-if="scope.row.templateType != 'F' "
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAddTemplateFrom(scope.row)"
          >新增
          </el-button>
          <el-button
            v-if="scope.row.parentId != 0 || scope.row.templateId !== '3'"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除
          </el-button>
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
    <!-- 预览界面 -->


    <el-dialog
      :title="preview.title"
      :visible.sync="preview.open"
      width="80%"
      top="5vh"
      append-to-body
      class="scrollbar"
    >
      <el-link
        v-clipboard:copy="preview.data"
        v-clipboard:success="clipboardSuccess"
        :underline="false"
        icon="el-icon-document-copy"
        style="float:right"
      >复制
      </el-link>
      <codemirror
        v-if="preview.data !== null"
        v-model="preview.data"
        :options="cmOptions"
      />
    </el-dialog>

    <!--   上传模板-->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="26%"
      append-to-body
    >
      <el-row>
        <el-col>
          <el-upload
            class="upload-demo"
            drag
            :before-upload="handleBeforeUpload"
            :action="uploadUrl"
            :file-list="fileList"
            :on-success="handleUploadSuccess"
            :show-file-list="false"
            multiple
          >
            <i class="el-icon-upload"/>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <div slot="tip" class="el-upload__tip">只能上传vm文件，且不超过500kb</div>
          </el-upload>
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
        <el-button @click="open = false">取 消</el-button>
        <el-button type="primary" @click="clickSubmit">确 定</el-button>
      </span>
    </el-dialog>


  </div>
</template>

<script>
import { codemirror } from 'vue-codemirror'
import 'codemirror/mode/velocity/velocity'
import 'codemirror/theme/neat.css'
import 'codemirror/lib/codemirror.css'
import { deleteTemplate, getTemplateList, importTemplate } from '@/api/template'
import { handleTree } from '@/utils'

export default {
  name: 'Gen',
  data() {
    return {
      components: { codemirror },
      title: '',
      // 重新渲染表格状态
      refreshTable: true,
      // 是否展开，默认全部展开
      isExpandAll: true,
      open: false,
      // 遮罩层
      loading: true,
      // 唯一标识符
      uniqueId: '',
      // 选中数组
      ids: [],
      // 选中表数组
      tableNames: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 表数据
      templateList: [],
      // 日期范围
      dateRange: '',
      preview: {
        open: false,
        title: '',
        key: '',
        data: ''
      },
      cmOptions: {
        value: '',
        mode: 'text/velocity',
        theme: 'neat',
        lineNumbers: true,
        tabSize: 4,
        line: true
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        group: null,
        templateName: null
      },
      uploadUrl: process.env.VUE_APP_BASE_API + '/template/uploadTemplate',
      // 大小限制(MB)
      fileSize: 5,
      // 文件类型, 例如['png', 'jpg', 'jpeg']
      fileType: ['vm'],
      number: 0,
      uploadList: [],
      fileList: [],
      options: [{
        value: '0',
        label: '启用'
      }, {
        value: '1',
        label: '停用'
      }]
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询表集合 */
    getList() {
      this.loading = true
      getTemplateList(this.queryParams).then(response => {
          this.templateList = handleTree(response.data.list, 'templateId')
          console.log(this.templateList)
          this.total = response.data.total
          this.loading = false
        }
      )
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    clickAddTemplate() {
    },
    handleUpdate(row){
      const id = row.templateId
      this.$router.push({ path: '/templateEdit', query: { templateId: id }})
    },
    clickSubmit() {
      this.open = false
      this.getList()
    },
    handleAddTemplate() {
      this.$router.push({ path: '/templateEdit', query: { parentId: '1' }})
    },
    handleAddTemplateFrom(row) {
      const id = row.templateId
      this.$router.push({ path: '/templateEdit', query: { parentId: id }})
    },
    handleImport(row) {
      const templateIds = row.templateId || this.ids
      this.$modal.confirm('是否确认导入编号为"' + templateIds + '"的模板？').then(function() {
        return importTemplate(templateIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('导入成功')
      }).catch(() => {
      })
    },
    // 上传成功回调
    handleUploadSuccess(res) {
      if (res.code === 200) {
        this.$modal.closeLoading()
        this.$message.success(res.data.key + '上传成功！')
      }
    },
    handleBeforeUpload(file) {
      // 校检文件类型
      if (this.fileType) {
        let fileExtension = ''
        if (file.name.lastIndexOf('.') > -1) {
          fileExtension = file.name.slice(file.name.lastIndexOf('.') + 1)
        }
        const isTypeOk = this.fileType.some((type) => {
          if (file.type.indexOf(type) > -1) return true
          if (fileExtension && fileExtension.indexOf(type) > -1) return true
          return false
        })
        if (!isTypeOk) {
          this.$modal.msgError(`文件格式不正确, 请上传${this.fileType.join('/')}格式文件!`)
          return false
        }
      }
      // 校检文件大小
      if (this.fileSize) {
        const isLt = file.size / 1024 / 1024 < this.fileSize
        if (!isLt) {
          this.$modal.msgError(`上传文件大小不能超过 ${this.fileSize} MB!`)
          return false
        }
      }
      this.$modal.loading('正在上传文件，请稍候...')
      this.number++
      return true
    },
    /** 生成代码操作 */
    handleGenTable(row) {
      const tableNames = row.tableName || this.tableNames
      if (tableNames === '') {
        this.$modal.msgError('请选择要生成的数据')
        return
      }
      if (row.genType === '1') {
        genCode(row.tableName).then(response => {
          this.$modal.msgSuccess('成功生成到自定义路径：' + row.genPath)
        })
      } else {
        this.$download.zip('/gen/batchGenCode?tables=' + tableNames, 'noobzz-codeGenerator')
      }
    },
    /** 打开导入表弹窗 */
    ImportTemplate() {
      this.title = '导入模板'
      this.open = true
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = []
      this.resetForm('queryForm')
      this.handleQuery()
    },
    /** 预览按钮 */
    handlePreview(row) {
      console.log(row)
      this.preview.data = row.content
      this.preview.key = row.path
      this.preview.title = row.templateName
      this.preview.open = true
    },
    /** 高亮显示 */
    highlightedCode(code, key) {
      const vmName = key.substring(key.lastIndexOf('/') + 1, key.indexOf('.vm'))
      var language = vmName.substring(vmName.indexOf('.') + 1, vmName.length)
      const result = hljs.highlight(language, code || '', true)
      return result.value || '&nbsp;'
    },
    /** 复制代码成功 */
    clipboardSuccess() {
      this.$modal.msgSuccess('复制成功')
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.templateId)
      this.templateNames = selection.map(item => item.templateName)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 修改按钮操作 */
    handleEditTable(row) {
      const tableId = row.tableId || this.ids[0]
      // const tableName = row.tableName || this.tableNames[0]
      const params = { pageNum: this.queryParams.pageNum }
      this.$router.push({ path: '/tool/gen-edit/index/' + tableId, query: { p: params } })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const templateIds = row.templateId || this.ids
      this.$modal.confirm('是否确认删除模板名为"' + row.templateName + '"的数据项？').then(function() {
        return deleteTemplate(templateIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {
      })
    }
  }
}
</script>
