import axios from 'axios'
import { getAuth, clearAuth } from './auth'

export const api = axios.create({ baseURL: '/api', timeout: 15000 })

api.interceptors.request.use((config) => {
  const auth = getAuth()
  if (auth?.token) {
    config.headers = config.headers || {}
    config.headers['Authorization'] = `Bearer ${auth.token}`
  }
  return config
})

api.interceptors.response.use(
  (resp) => resp,
  (err) => {
    if (err?.response?.status === 401) clearAuth()
    return Promise.reject(err)
  }
)

export function unwrap(resp) {
  const body = resp?.data
  if (!body?.success) throw new Error(body?.message || '请求失败')
  return body.data
}
