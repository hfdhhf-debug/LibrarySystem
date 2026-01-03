<template>
  <div class="card" style="max-width:520px;margin:0 auto;">
    <h2 style="margin:0 0 10px;">登录</h2>

    <div class="row" style="margin-bottom:10px;">
      <input class="input" v-model="username" placeholder="用户名" />
      <input class="input" v-model="password" placeholder="密码" type="password" />
    </div>

    <div class="row">
      <button class="btn primary" :disabled="loading" @click="doLogin">登录</button>
      <router-link class="btn" to="/register">没有账号？去注册</router-link>
    </div>

    <p v-if="msg" :class="ok ? 'success' : 'error'" style="margin:10px 0 0;">{{ msg }}</p>

    <p class="small" style="margin:12px 0 0;">
      默认管理员：<b>admin / admin123</b>
    </p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { api, unwrap } from '../lib/api'
import { setAuth } from '../lib/auth'
import { useRouter } from 'vue-router'

const router = useRouter()
const username = ref('admin')
const password = ref('admin123')
const loading = ref(false)
const msg = ref('')
const ok = ref(false)

async function doLogin() {
  msg.value = ''
  ok.value = false
  loading.value = true
  try {
    const data = unwrap(await api.post('/auth/login', { username: username.value, password: password.value }))
    setAuth({ token: data.token, role: data.role, username: username.value })
    ok.value = true
    msg.value = '登录成功'
    window.dispatchEvent(new StorageEvent('storage'))
    router.push('/books')
  } catch (e) {
    msg.value = e?.message || '登录失败'
  } finally {
    loading.value = false
  }
}
</script>
