<template>
  <div class="topbar">
    <div class="nav">
      <div class="nav-left">
        <router-link to="/" style="font-weight:700;">📚 图书馆系统</router-link>
        <router-link to="/books">图书查询</router-link>
        <router-link v-if="authed" to="/my">我的借阅</router-link>
        <router-link v-if="admin" to="/admin/books">管理员-图书</router-link>
        <router-link v-if="admin" to="/admin/stats">管理员-统计</router-link>
      </div>
      <div class="nav-right">
        <span class="small" v-if="authed">当前用户：<b>{{ auth.username }}</b> <span class="badge">{{ auth.role }}</span></span>
        <router-link v-if="!authed" class="btn" to="/login">登录</router-link>
        <router-link v-if="!authed" class="btn" to="/register">注册</router-link>
        <button v-if="authed" class="btn" @click="logout">退出</button>
      </div>
    </div>
  </div>

  <div class="container">
    <router-view />
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import { getAuth, clearAuth, isAdmin } from './lib/auth'
import { useRouter } from 'vue-router'

const router = useRouter()
const auth = ref(getAuth())
const authed = computed(() => !!auth.value?.token)
const admin = computed(() => isAdmin())

window.addEventListener('storage', () => { auth.value = getAuth() })

function logout() {
  clearAuth()
  auth.value = null
  router.push('/login')
}
</script>
