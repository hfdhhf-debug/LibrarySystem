<template>
  <div class="card">
    <div class="row" style="justify-content: space-between;">
      <div class="row">
        <h2 style="margin:0;">图书查询</h2>
        <span class="badge">公开接口</span>
      </div>

      <div class="row">
        <input class="input" v-model="keyword" placeholder="关键字：ISBN/书名/作者/出版社/位置" @keyup.enter="load" />
        <button class="btn" @click="load">搜索</button>
      </div>
    </div>

    <p v-if="msg" :class="ok ? 'success' : 'error'" style="margin:10px 0 0;">{{ msg }}</p>

    <table class="table">
      <thead>
        <tr>
          <th>ID</th>
          <th>ISBN</th>
          <th>书名</th>
          <th>作者</th>
          <th>出版社</th>
          <th>馆藏位置</th>
          <th>库存</th>
          <th>可借</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="b in books" :key="b.id">
          <td>{{ b.id }}</td>
          <td>{{ b.isbn }}</td>
          <td>{{ b.title }}</td>
          <td>{{ b.author }}</td>
          <td>{{ b.publisher }}</td>
          <td>{{ b.location }}</td>
          <td>{{ b.totalStock }}</td>
          <td>{{ b.availableStock }}</td>
          <td>
            <button class="btn primary" :disabled="!authed || b.availableStock<=0 || loadingBorrow" @click="borrow(b.id)">
              借书
            </button>
          </td>
        </tr>
      </tbody>
    </table>

    <div class="row" style="margin-top:12px; justify-content: space-between;">
      <div class="small">共 {{ totalElements }} 本（第 {{ page+1 }} / {{ totalPages }} 页）</div>
      <div class="row">
        <button class="btn" :disabled="page<=0" @click="prev">上一页</button>
        <button class="btn" :disabled="page>=totalPages-1" @click="next">下一页</button>
      </div>
    </div>

    <p class="small" style="margin-top:10px;">
      提示：借书需要先 <router-link to="/login">登录</router-link>。
    </p>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { api, unwrap } from '../lib/api'
import { getAuth } from '../lib/auth'

const keyword = ref('')
const page = ref(0)
const size = ref(10)

const books = ref([])
const totalPages = ref(1)
const totalElements = ref(0)

const loadingBorrow = ref(false)
const msg = ref('')
const ok = ref(false)

const authed = computed(() => !!getAuth()?.token)

async function load() {
  msg.value = ''
  ok.value = false
  try {
    const data = unwrap(await api.get('/books', { params: { keyword: keyword.value, page: page.value, size: size.value } }))
    books.value = data.content || []
    totalPages.value = data.totalPages ?? 1
    totalElements.value = data.totalElements ?? books.value.length
  } catch (e) {
    msg.value = e?.message || '加载失败'
  }
}

async function borrow(bookId) {
  msg.value = ''
  ok.value = false
  loadingBorrow.value = true
  try {
    unwrap(await api.post('/borrow', { bookId }))
    ok.value = true
    msg.value = '借书成功（请到“我的借阅”查看并归还）'
    await load()
  } catch (e) {
    msg.value = e?.response?.data?.message || e?.message || '借书失败'
  } finally {
    loadingBorrow.value = false
  }
}

function prev() { page.value = Math.max(0, page.value - 1); load() }
function next() { page.value = page.value + 1; load() }

onMounted(load)
</script>
