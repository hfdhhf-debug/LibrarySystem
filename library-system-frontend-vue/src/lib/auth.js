const KEY = 'LIBSYS_AUTH'

export function getAuth() {
  try { return JSON.parse(localStorage.getItem(KEY) || 'null') } catch { return null }
}
export function setAuth(auth) { localStorage.setItem(KEY, JSON.stringify(auth)) }
export function clearAuth() { localStorage.removeItem(KEY) }
export function isAdmin() {
  const a = getAuth()
  return a?.role?.includes('ROLE_ADMIN')
}
