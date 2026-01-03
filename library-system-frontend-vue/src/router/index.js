import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Books from '../views/Books.vue'
import MyBorrows from '../views/MyBorrows.vue'
import AdminBooks from '../views/AdminBooks.vue'
import AdminStats from '../views/AdminStats.vue'
import { getAuth, isAdmin } from '../lib/auth'

const routes = [
  { path: '/', component: Home },
  { path: '/login', component: Login },
  { path: '/register', component: Register },
  { path: '/books', component: Books },
  { path: '/my', component: MyBorrows, meta: { requiresAuth: true } },
  { path: '/admin/books', component: AdminBooks, meta: { requiresAdmin: true } },
  { path: '/admin/stats', component: AdminStats, meta: { requiresAdmin: true } }
]

const router = createRouter({ history: createWebHistory(), routes })

router.beforeEach((to) => {
  const auth = getAuth()
  if (to.meta?.requiresAuth && !auth?.token) return '/login'
  if (to.meta?.requiresAdmin && !isAdmin()) return '/books'
})

export default router
