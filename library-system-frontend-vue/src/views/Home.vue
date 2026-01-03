<template>
  <div class="card">
    <h2 style="margin:0 0 8px;">欢迎</h2>
    <p class="small" style="margin:0;">
      这是一个“前后端分离”的演示前端（Vue3 + Vite）。后端请先启动 Spring Boot（默认端口 8080）。
      前端通过 Vite 代理调用 <code>/api/**</code>，无需额外配置 CORS。
    </p>
    <div class="row" style="margin-top:12px;">
      <router-link class="btn primary" to="/books">开始使用：图书查询</router-link>
      <router-link class="btn" to="/login">登录</router-link>
      <router-link class="btn" to="/register">注册</router-link>
    </div>
  </div>
</template>
