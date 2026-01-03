# Library System Frontend (Vue3 + Vite)

这是配套的“前后端分离”前端工程。

## 1. 前置条件
- Node.js 18+（推荐 18 或 20）
- 后端 Spring Boot 已启动：`http://localhost:8080`

## 2. 运行
```bash
npm install
npm run dev
```

浏览器打开：`http://localhost:5173`

> Vite 已配置代理：前端请求 `/api/**` 会自动转发到 `http://localhost:8080/api/**`，无需配置 CORS。

## 3. 功能页面
- `/books` 图书查询（无需登录；登录后可借书）
- `/login` 登录（默认管理员：admin/admin123）
- `/register` 注册普通用户
- `/my` 我的借阅（登录后）
- `/admin/books` 管理员图书增删改（管理员登录后）
- `/admin/stats` 管理员统计排行榜（管理员登录后）
