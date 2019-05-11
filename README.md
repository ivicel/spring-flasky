# spring-flasky

Flasky 是 [Flask Web Development](https://book.douban.com/subject/26274202/) 里的一个实例项目, 原项目是使用 Python 开发了, 一个类似微博的小型项目, 我用 Spring Boot 重写了一个
实现了里面提到了主要功能:

- [x] Post 分页, 及在登录后查看关注的人发的 Post
- [x] 帐号注册, 登录, 发 Post, 对他人 Post 的评论
- [x] 个人信息页
- [x] 权限系统, 帐号角色分配
- [x] 邮件发送系统, 通过邮件激活帐号, 重置密码
- [x] 关注, 取消关注功能
- [ ] Rest API

### 2. 主要依赖

- Spring Boot
- Spring Security 用于权限控制, 登录
- Spring Data JPA 数据库查询 ORM
- Thymeleaf 模版引擎
- MySQL 数据库
- OhMyEmail 最简单邮箱发送系统

### 3. 一些笔记

- [Spring Flasky 开发 (一)](https://ivicel.info/2019/05/spring-flasky-kai-fa-yi.html)
- [Spring Flasky 开发 (二)](https://ivicel.info/2019/05/spring-flasky-kai-fa-er.html)
- [Spring Flasky 开发 (三)](https://ivicel.info/2019/05/spring-flasky-kai-fa-san.html)
