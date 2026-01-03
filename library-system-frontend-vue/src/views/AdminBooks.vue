<template>
  <div class="card">
    <div class="row" style="justify-content: space-between;">
      <div class="row">
        <h2 style="margin:0;">管理员：图书管理</h2>
        <span class="badge">ROLE_ADMIN</span>
      </div>
      <div class="row">
        <button class="btn" @click="load">刷新</button>
      </div>
    </div>

    <p v-if="msg" :class="ok ? 'success' : 'error'" style="margin:10px 0 0;">{{ msg }}</p>

    <div class="card" style="margin-top:12px;">
      <h3 style="margin:0 0 10px;">新增 / 修改</h3>

      <div class="row" style="margin-bottom:10px;">
        <input class="input" v-model="form.id" placeholder="ID（留空=新增）" />
        <input class="input" v-model="form.isbn" placeholder="ISBN" />
        <input class="input" v-model="form.title" placeholder="书名" />
        <input class="input" v-model="form.author" placeholder="作者" />
        <input class="input" v-model="form.publisher" placeholder="出版社" />
        <input class="input" v-model="form.location" placeholder="馆藏位置" />
        <input class="input" v-model.number="form.totalStock" placeholder="总库存" />
      </div>

      <div class="row">
        <button class="btn primary" :disabled="loadingSave" @click="save">保存</button>
        <button class="btn" @click="reset">清空</button>
      </div>
      <p class="small" style="margin:10px 0 0;">提示：修改时会尽量保持“已借出数量”不变，只调整可借库存。</p>
    </div>

    <h3 style="margin:14px 0 8px;">列表</h3>
    <table class="table">
      <thead>
        <tr>
          <th>ID</th><th>ISBN</th><th>书名</th><th>作者</th><th>出版社</th><th>位置</th><th>总</th><th>可借</th><th>操作</th>
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
          <td class="row">
            <button class="btn" @click="fill(b)">编辑</button>
            <button class="btn danger" :disabled="loadingDel" @click="del(b.id)">删除</button>
          </td>
        </tr>
      </tbody>
    </table>

  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { api, unwrap } from '../lib/api'

const books = ref([])
const msg = ref('')
const ok = ref(false)

const loadingSave = ref(false)
const loadingDel = ref(false)

const form = ref({ id:'', isbn:'', title:'', author:'', publisher:'', location:'', totalStock: 1 })

async function load() {
  msg.value = ''
  ok.value = false
  try {
    const data = unwrap(await api.get('/books', { params: { page: 0, size: 50 } }))
    books.value = data.content || []
  } catch (e) {
    msg.value = e?.message || '加载失败（请确认管理员已登录）'
  }
}

function reset() { form.value = { id:'', isbn:'', title:'', author:'', publisher:'', location:'', totalStock: 1 } }

function fill(b) {
  form.value = { id: String(b.id), isbn: b.isbn, title: b.title, author: b.author, publisher: b.publisher, location: b.location, totalStock: b.totalStock }
}

async function save() {
  msg.value = ''
  ok.value = false
  loadingSave.value = true
  try {
    const payload = { isbn: form.value.isbn, title: form.value.title, author: form.value.author, publisher: form.value.publisher, location: form.value.location, totalStock: Number(form.value.totalStock || 0) }
    if (form.value.id) {
      unwrap(await api.put(`/books/${form.value.id}`, payload))
      msg.value = '修改成功'
    } else {
      unwrap(await api.post('/books', payload))
      msg.value = '新增成功'
    }
    ok.value = true
    reset()
    await load()
  } catch (e) {
    msg.value = e?.response?.data?.message || e?.message || '保存失败'
  } finally {
    loadingSave.value = false
  }
}

async function del(id) {
  if (!confirm(`确认删除图书ID=${id} ?`)) return
  msg.value = ''
  ok.value = false
  loadingDel.value = true
  try {
    unwrap(await api.delete(`/books/${id}`))
    ok.value = true
    msg.value = '删除成功'
    await load()
  } catch (e) {
    msg.value = e?.response?.data?.message || e?.message || '删除失败'
  } finally {
    loadingDel.value = false
  }
}

onMounted(load)
</script>
