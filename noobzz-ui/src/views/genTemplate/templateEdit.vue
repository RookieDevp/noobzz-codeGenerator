<template>
  <div class="app-container">
    <el-form ref="form" :model="form" :rules="rules" label-width="80px">
      <el-row :gutter="20">
        <el-col :span="5" :xs="24">
          <el-form-item label="模板名称" prop="templateName">
            <el-input v-model="form.templateName" placeholder="请输入模板名称"></el-input>
          </el-form-item>
          <el-form-item label="上级目录" prop="parentId">
            <el-select v-model="form.parentId" placeholder="请选择">
              <el-option
                v-for="item in parentIdOptions"
                :key="item.templateId"
                :label="item.templateName"
                :value="item.templateId"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="模板类型" prop="templateType">
            <el-select v-model="form.templateType" placeholder="请选择">
              <el-option
                v-for="item in typeOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item v-if="form.templateType === 'F'" label="文件类型" prop="fileType">
            <el-select v-model="form.fileType" placeholder="请选择">
              <el-option
                v-for="item in fileOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="模板状态" prop="status">
            <el-select v-model="form.status" placeholder="请选择">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="备注" prop="remark">
            <el-input
              type="textarea"
              :rows="2"
              placeholder="请输入备注"
              v-model="form.remark"
            >
            </el-input>
          </el-form-item>
          <el-button @click="submitFrom('form')" plain style="width: 100%">提交</el-button>
        </el-col>

        <el-col :span="19" :xs="24">
          <el-button
            v-clipboard:copy="form.content"
            type="text"
            icon="el-icon-document-copy"
            class="copyBtn"
          >复制代码
          </el-button>
          <el-button
            icon="el-icon-download"
            type="text"
          >下载当前文件
          </el-button>
          <el-button
            icon="el-icon-bottom-left"
            type="text"
            @click="insertOption"
          >插入当前
            <el-select  size="mini" :filterable=true v-model="velocityOption" placeholder="请选择">
            <el-option
              v-for="item in velocityOptions"
              :key="item.id"
              :label="item.expression"
              :value="item.expression">
              <span style="float: left;margin-right: 30px">{{ item.expression }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{ item.text }}</span>
            </el-option>
          </el-select>
          </el-button>
          <el-link style="float: right" type="primary" :underline="false"
                   href="https://www.cnblogs.com/codingsilence/archive/2011/03/29/2146580.html" target="_blank"
          >Velocity语法
          </el-link>
          <codemirror
            ref="myCm"
            v-model="form.content"
            :options="cmOptions"
          />
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script>
import { codemirror } from 'vue-codemirror'
import 'codemirror/mode/velocity/velocity'
import 'codemirror/theme/neat.css'
import 'codemirror/lib/codemirror.css'
import { addTemplateList, editTemplate, getTemplateList, javaConfig } from '@/api/template'

export default {
  components: { codemirror },
  data() {
    return {
      form: {
        parentId: '',
        content: '',
        status: '0'
      },
      cmOptions: {
        value: '',
        mode: 'text/velocity',
        theme: 'neat',
        lineNumbers: true,
        tabSize: 4,
        line: true
      },
      velocityOption: '',
      velocityOptions: [],
      parentIdOptions: [],
      options: [{
        value: '0',
        label: '启用'
      }, {
        value: '1',
        label: '停用'
      }],
      fileOptions: [{
        value: 'java',
        label: 'java'
      }, {
        value: 'js',
        label: 'js'
      },{
        value: 'sql',
        label: 'sql'
      }, {
        value: 'vue',
        label: 'vue'
      }, {
        value: 'xml',
        label: 'xml'
      }],
      typeOptions: [{
        value: 'M',
        label: '目录'
      }, {
        value: 'F',
        label: '文件'
      }],
      rules: {
        templateName: [
          { required: true, pattern: '^\\w+$', message: '请输入正确的格式数字(数字、大小写字母、_)', trigger: 'blur' }
        ],
        parentId: [
          { required: true, trigger: 'blur' }
        ],
        templateType: [
          { required: true, trigger: 'blur' }
        ],
        status: [
          { required: true, trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    insertOption(){
      console.log(this.velocityOption)
      this.$refs.myCm.codemirror.replaceSelection(this.velocityOption)
    },
    submitFrom(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          console.log(this.form)
          if (this.form.templateId != null){
            editTemplate(this.form).then(response => {
              if (response.code === 200) {
                this.$message.success(response.msg)
              }
            })
          } else {
            addTemplateList(this.form).then(response => {
              if (response.code === 200) {
                this.$message.success(response.msg)
                this.$router.push(`/templateList`)
              }
            })
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    getList() {
      javaConfig().then(response => {
        this.velocityOptions = response.data
      })
      getTemplateList({ templateType: 'M' }).then(response => {
        this.parentIdOptions = response.data.list
      })
      if (this.$route.query.parentId !== undefined) {
        this.form.parentId = this.$route.query.parentId
      }
      if (this.$route.query.templateId !== undefined) {
        getTemplateList({ templateType: 'F', templateId: this.$route.query.templateId }).then(response => {
          this.form = response.data.list[0]
        })
      }
    }
  }
}
</script>
