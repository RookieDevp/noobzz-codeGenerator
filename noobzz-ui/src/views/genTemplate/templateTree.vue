<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--部门数据-->
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input
            v-model="templateName"
            placeholder="请输入模板名称"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container">
          <el-tree
            ref="tree"
            :data="templateOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            default-expand-all
            highlight-current
            @node-click="handleNodeClick"
          />
        </div>
      </el-col>

      <el-col :span="20" :xs="24">
        <el-button
          v-clipboard:copy="fileInfo.content"
          type="text"
          icon="el-icon-document-copy"
          class="copyBtn"
        >复制代码</el-button>
        <el-button
          icon="el-icon-download"
          type="text"
          @click="downloadText(fileInfo.fileName, fileInfo.content)"
        >下载当前文件</el-button>
        <el-link style="float: right" type="primary" :underline="false" href="https://www.cnblogs.com/codingsilence/archive/2011/03/29/2146580.html" target="_blank">Velocity语法</el-link>
        <codemirror
          ref="code"
          v-if="fileInfo.content !== null"
          v-model="fileInfo.content"
          :config="cmOptions"
          :options="cmOptions"
        />
        <el-empty v-else description="请选择模板" />
      </el-col>
    </el-row>

    <!-- 添加或修改用户配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="分组名" prop="nickName">
              <el-input v-model="form.groupName" placeholder="请输入分组名" maxlength="30" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模板名" prop="deptId">
              <el-input v-model="form.templateName" placeholder="请输入模板名" maxlength="30" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { createTemplate, deleteTemplate} from '@/api/gen'
import { getTemplate, getTemplateList } from '@/api/template'
import { codemirror } from 'vue-codemirror'
import 'codemirror/mode/velocity/velocity'
import 'codemirror/theme/neat.css'
import 'codemirror/lib/codemirror.css'

import 'codemirror/addon/fold/foldgutter.css'
import 'codemirror/addon/fold/foldcode'
import 'codemirror/addon/fold/foldgutter'
import { handleTree } from '@/utils'

export default {
  components: { codemirror },
  data() {
    return {
      form: {},
      rules: {},
      title: '',
      open: false,
      templateName: '',
      editButtonOpen: false,
      templateOptions: null,
      defaultProps: {
        children: 'children',
        label: 'templateName',
        value: 'templateId'
      },
      fileInfo: {
        content: null,
        fileName: null
      },
      currentPath: '',
      currentTemplateName: '',
      currentGroupName: '',
      cmOptions: {
        lineWrapping: true,
        value: '',
        mode: 'text/velocity',
        theme: 'neat',
        lineNumbers: true,
        tabSize: 4,
        line: true,
        gutters: ['CodeMirror-linenumbers', 'CodeMirror-foldgutter'],  // 折叠栏位置
        foldGutter: true,    // 开启折叠栏
        extraKeys: {
          'Ctrl-Q': function (cm) {  // 配置折叠快捷键
            cm.foldCode(cm.getCursor())
          }
        }
      }
    }
  },
  created() {
    this.getList()
  },
  mounted() {
    this.cmOptions.foldGutter = true
  },
  methods: {
    getList() {
      getTemplateList({}).then(response => {
        this.templateOptions = handleTree(response.data.list,'templateId');
      }
      )
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        templateName: '',
        groupName: '',
        content: '',
        path: ''
      }
      this.resetForm('form')
    },
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },
    // 节点单击事件
    handleNodeClick(data) {
      console.log(data.templateId)
      getTemplate(data.templateId).then(response => {
        this.fileInfo.content = response.data.content
      })
    },
    clickNewFile() {
      console.log(this.currentTemplateName)
      this.currentTemplateName = ''
      this.fileInfo.content = ''
    },
    clickSaveFile() {
      this.open = true
      if (this.currentTemplateName !== '') {
        this.reset()
        this.form.templateName = this.currentTemplateName
        this.form.groupName = this.currentGroupName.substring(0, this.currentGroupName.length - 1)
        this.form.path = this.currentPath
      }
    },
    submitForm() {
      if (this.currentTemplateName === '') {
        const postBody = {
          content: this.fileInfo.content,
          dir: this.form.groupName,
          path: this.form.groupName + '/' + this.form.templateName,
          name: this.form.templateName
        }
        console.log(postBody)
        console.log('save')
        createTemplate(postBody).then(response => {
          this.$modal.msgSuccess(response.msg)
          this.getList()
        })
      } else {
        this.form.content = this.fileInfo.content
        console.log('update')
        console.log(this.form)
      }
      this.open = false
    },
    clickDeleteTemplate() {
      const path = this.currentPath
      this.$modal.confirm('是否确认删除模板名为"' + this.currentTemplateName + '"的数据项？').then(function() {
        return deleteTemplate(path)
      }).then(() => {
        this.getList()
        this.fileInfo.content = ''
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    }
  }
}
</script>
<style>
.CodeMirror{
  border: 1px solid #eee;
  height: auto;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
  font-size: 14px;
}
</style>

