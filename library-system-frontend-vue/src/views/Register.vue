<template>
  <div class="card" style="max-width:520px;margin:0 auto;">
    <h2 style="margin:0 0 10px;">注册</h2>

    <div class="row" style="margin-bottom:10px;">
      <input class="input" v-model="username" placeholder="用户名（例如 user1）" />
      <input class="input" v-model="password" placeholder="密码（例如 123456）" type="password" />
    </div>

    <div class="row">
      <button class="btn primary" :disabled="loading" @click="doRegister">注册</button>
      <router-link class="btn" to="/login">已有账号？去登录</router-link>
    </div>

    <p v-if="msg" :class="ok ? 'success' : 'error'" style="margin:10px 0 0;">{{ msg }}</p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { api } from '../lib/api'

const username = ref('')
const password = ref('')
const loading = ref(false)
const msg = ref('')
const ok = ref(false)

async function doRegister() {
  msg.value = ''
  ok.value = false
  loading.value = true
  try {
    const resp = await api.post('/auth/register', { username: username.value, password: password.value })
    if (!resp.data?.success) throw new Error(resp.data?.message || '注册失败')
    ok.value = true
    msg.value = '注册成功，请去登录'
  } catch (e) {
    msg.value = e?.message || '注册失败'
  } finally {
    loading.value = false
  }
}
</script>
