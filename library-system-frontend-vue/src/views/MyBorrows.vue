<template>
  <div class="card">
    <div class="row" style="justify-content: space-between;">
      <div class="row">
        <h2 style="margin:0;">我的借阅</h2>
        <span class="badge">需要登录</span>
      </div>
      <div class="row">
        <button class="btn" @click="load">刷新</button>
      </div>
    </div>

    <p v-if="msg" :class="ok ? 'success' : 'error'" style="margin:10px 0 0;">{{ msg }}</p>

    <table class="table">
      <thead>
        <tr>
          <th>记录ID</th>
          <th>图书ID</th>
          <th>借阅日期</th>
          <th>应还日期</th>
          <th>归还日期</th>
          <th>状态</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="r in records" :key="r.id">
          <td>{{ r.id }}</td>
          <td>{{ r.bookId }}</td>
          <td>{{ r.borrowDate }}</td>
          <td>{{ r.dueDate }}</td>
          <td>{{ r.returnDate || '-' }}</td>
          <td><span class="badge">{{ r.status }}</span></td>
          <td>
            <button class="btn primary" :disabled="!!r.returnDate || loadingReturn" @click="giveBack(r.id)">
              还书
            </button>
          </td>
        </tr>
      </tbody>
    </table>

    <div class="row" style="margin-top:12px; justify-content: space-between;">
      <div class="small">共 {{ totalElements }} 条（第 {{ page+1 }} / {{ totalPages }} 页）</div>
      <div class="row">
        <button class="btn" :disabled="page<=0" @click="prev">上一页</button>
        <button class="btn" :disabled="page>=totalPages-1" @click="next">下一页</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { api, unwrap } from '../lib/api'

const records = ref([])
const page = ref(0)
const size = ref(10)
const totalPages = ref(1)
const totalElements = ref(0)
const loadingReturn = ref(false)

const msg = ref('')
const ok = ref(false)

async function load() {
  msg.value = ''
  ok.value = false
  try {
    const data = unwrap(await api.get('/borrows/my', { params: { page: page.value, size: size.value } }))
    records.value = data.content || []
    totalPages.value = data.totalPages ?? 1
    totalElements.value = data.totalElements ?? records.value.length
  } catch (e) {
    msg.value = e?.message || '加载失败（请确认已登录）'
  }
}

async function giveBack(recordId) {
  msg.value = ''
  ok.value = false
  loadingReturn.value = true
  try {
    unwrap(await api.post('/return', { recordId }))
    ok.value = true
    msg.value = '还书成功'
    await load()
  } catch (e) {
    msg.value = e?.response?.data?.message || e?.message || '还书失败'
  } finally {
    loadingReturn.value = false
  }
}

function prev() { page.value = Math.max(0, page.value - 1); load() }
function next() { page.value = page.value + 1; load() }

onMounted(load)
</script>
