<template>
  <div class="card">
    <div class="row" style="justify-content: space-between;">
      <div class="row">
        <h2 style="margin:0;">管理员：借阅统计</h2>
        <span class="badge">ROLE_ADMIN</span>
      </div>
      <div class="row">
        <input class="input" v-model="from" placeholder="from (YYYY-MM-DD)" />
        <input class="input" v-model="to" placeholder="to (YYYY-MM-DD)" />
        <input class="input" v-model.number="limit" placeholder="limit" style="min-width:120px;" />
        <button class="btn primary" @click="load">查询</button>
      </div>
    </div>

    <p v-if="msg" :class="ok ? 'success' : 'error'" style="margin:10px 0 0;">{{ msg }}</p>

    <div class="row" style="margin-top:12px; align-items: stretch;">
      <div class="card" style="flex:1;">
        <h3 style="margin:0 0 10px;">Top Books</h3>
        <table class="table">
          <thead><tr><th>bookId</th><th>count</th><th>title</th><th>author</th></tr></thead>
          <tbody>
            <tr v-for="x in topBooks" :key="x.bookId">
              <td>{{ x.bookId }}</td>
              <td>{{ x.count }}</td>
              <td>{{ x.title || '-' }}</td>
              <td>{{ x.author || '-' }}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="card" style="flex:1;">
        <h3 style="margin:0 0 10px;">Top Users</h3>
        <table class="table">
          <thead><tr><th>userId</th><th>count</th><th>username</th><th>role</th></tr></thead>
          <tbody>
            <tr v-for="x in topUsers" :key="x.userId">
              <td>{{ x.userId }}</td>
              <td>{{ x.count }}</td>
              <td>{{ x.username || '-' }}</td>
              <td>{{ x.role || '-' }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <p class="small" style="margin-top:10px;">
      说明：统计接口需要先有借阅记录（先用普通用户借几本书），再用管理员查询。
    </p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { api, unwrap } from '../lib/api'

const today = new Date()
const yyyy = today.getFullYear()
const from = ref(`${yyyy}-01-01`)
const to = ref(`${yyyy}-12-31`)
const limit = ref(10)

const topBooks = ref([])
const topUsers = ref([])

const msg = ref('')
const ok = ref(false)

async function load() {
  msg.value = ''
  ok.value = false
  try {
    topBooks.value = unwrap(await api.get('/stats/top-books', { params: { from: from.value, to: to.value, limit: limit.value } })) || []
    topUsers.value = unwrap(await api.get('/stats/top-users', { params: { from: from.value, to: to.value, limit: limit.value } })) || []
    ok.value = true
    msg.value = '查询成功'
  } catch (e) {
    msg.value = e?.response?.data?.message || e?.message || '查询失败（请确认管理员已登录）'
  }
}
</script>
