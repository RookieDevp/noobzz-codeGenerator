<template>
  <el-container>
    <el-main><div>
      <el-form ref="form" :model="form" label-width="150px">
        <el-form-item label="作者">
          <el-input v-model="form.author"></el-input>
        </el-form-item>
        <el-form-item label="默认生成包路径">
          <el-input v-model="form.packageName"></el-input>
        </el-form-item>
        <el-form-item label="自动去除表前缀">
          <el-switch v-model="form.autoRemovePre"></el-switch>
        </el-form-item>
        <el-form-item label="忽略表前缀">
          <el-input v-model="form.tablePrefix"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">保存</el-button>
          <el-button @click="resetConfig">重置</el-button>
        </el-form-item>
      </el-form>
    </div></el-main>
  </el-container>

</template>

<script>
import {editConfig, getConfig, getInitConfig} from "@/api/sysConfig";

export default {
  data() {
    return {
      form: {
        author: '',
        packageName: '',
        autoRemovePre: false,
        tablePrefix: ''
      }
    }
  },
  created() {
    getConfig().then((res) =>{
      this.form  = res.data
    });
  },
  methods: {
    resetConfig(){
      getInitConfig().then((res) =>{
        if (res.code == 200) {
          this.form = res.data
          this.$message.success(res.msg)
        }else {
          this.$message.error("重置失败")
        }
      });
    },
    onSubmit() {
      editConfig(this.form).then((res) =>{
        if (res.code == 200) {
          this.$message.success(res.msg)
        }else {
          this.$message.error("保存失败")
        }
      });
    }
  }
}
</script>

<style>

</style>
