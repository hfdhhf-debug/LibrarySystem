# Library Management System (Spring Boot)

本项目实现了“图书入库/查询、用户注册登录（区分管理员/普通用户）、借书/还书（含逾期判定）、借阅统计排行榜”等核心功能，符合课程作业要求。

> 默认提供一个管理员账号：`admin / admin123`（启动时自动创建）

## 1. 技术栈
- Java 17
- Spring Boot 3 + Spring Security（BCrypt加密存储密码）
- Spring Data JPA
- MySQL 8（可选：本地默认用 H2 方便快速运行）
- Postman API 集合：`postman/LibrarySystem.postman_collection.json`

## 2. 快速运行（推荐：H2 内存数据库）
1) 安装 JDK 17 与 Maven  
2) 运行：
```bash
mvn spring-boot:run
```
3) 访问：
- 接口基地址：`http://localhost:8080`
- H2 控制台（dev profile）：`http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:librarydb`

## 3. 使用 MySQL 8 运行（满足作业“数据库：MySQL 8.0”）
1) 启动 MySQL（docker-compose）：
```bash
docker compose up -d
```
2) 切换 profile：
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```
3) 如需改账号密码：编辑 `src/main/resources/application-mysql.yml`

## 4. 鉴权方式（JWT）
- 登录成功后返回 `token`
- 调用需要登录的接口时添加 Header：
```
Authorization: Bearer <token>
```

## 5. 主要接口
- POST `/api/auth/register` 注册
- POST `/api/auth/login` 登录
- GET  `/api/books` 图书查询（支持 keyword+分页）
- POST `/api/books` 新增图书（管理员）
- POST `/api/borrow` 借书
- POST `/api/return` 还书
- GET  `/api/borrows/my` 我的借阅记录
- GET  `/api/stats/top-books?from=2026-01-01&to=2026-12-31`（管理员）
- GET  `/api/stats/top-users?from=2026-01-01&to=2026-12-31`（管理员）

## 6. 测试
```bash
mvn test
```

## 7. 目录结构
- `entity/` 实体（User/Book/BorrowRecord）
- `repo/` JPA仓库
- `service/` 业务逻辑（借书时扣减库存、还书时归还库存、逾期判定）
- `controller/` REST API
- `security/` JWT鉴权与Spring Security配置

## 8. 作业提交建议
- 把本项目推到 GitHub / Gitee
- README 保留“运行说明、依赖清单”
- 在项目报告中贴：关键接口截图、测试截图、以及 AI 辅助过程记录（注意：作业要求“不包括AI产生的代码”）
