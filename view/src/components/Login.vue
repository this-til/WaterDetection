<template>
  <div class="loogin-container">
    <div class="login-title">登录</div>
    <br>
    <el-form
        class="form-wrap"
        ref="ruleFormRef"
        autocomplete="off"
    >
      <el-form-item prop="staff_account">
        <el-input
            class="login-input"
            style="width: 360px"
            v-model="username"
            placeholder="请输入账号"
            :prefix-icon="User"
        ></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
            class="login-input"
            style="width: 360px"
            v-model="password"
            placeholder="请输入密码"
            show-password
            :prefix-icon="Lock"
        ></el-input>
      </el-form-item>
    </el-form>
    <br>
    <el-button
        class="login-btn"
        type="primary"
        style="width: 360px"
        @click="loginHandle"
    >登录
    </el-button>
  </div>
</template>

<script setup lang="ts">
import {reactive, ref} from 'vue'
import {User, Lock, Sunny, Moon} from '@element-plus/icons-vue'
import type {FormInstance, FormRules} from 'element-plus'
import {LoginApi, addToken} from "@/api";
import {useRouter} from "vue-router";

const router = useRouter();



const username = ref<string>('')
const password = ref<string>('')



const ruleFormRef = ref<FormInstance>()
const loginLoading = ref(false)

const loginHandle = (formEl: FormInstance | undefined) => {
  loginLoading.value = true
  LoginApi.login(username.value, password.value).then(response => {
    if (response.data.resultType != "SUCCESSFUL") {
      return
    }
    addToken(response.data.data)
    router.replace({path: "/content"});
  })
}
</script>
<style scoped lang="css">
.loogin-container {
  position: absolute;
  top: 40%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.login-title {
  font-size: 24px;
}
</style>