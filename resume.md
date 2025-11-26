### 项目名称

- InsightVault（企业级 RAG 知识库平台）

### 项目简介

- 企业级 RAG 知识库平台。提供“上传解析 → 向量化 → 混合检索 → 流式问答”的完整链路，支持多租户与组织标签权限隔离，提供智能文档处理和检索能力。

### 技术栈（后端）

- 核心：Spring Boot 3、Spring Security + JWT、Spring Data JPA、WebFlux
- 存储与中间件：MySQL、Redis、Elasticsearch、Kafka、MinIO
- 文档解析与 AI：Apache Tika、DeepSeek API / Ollama / 豆包 Embedding
- 其他：Maven、Docker、WebSocket

### 个人职责（Java 后端）
- 聊天助手：基于 WebSocket 双向通信与 DeepSeek 流式输出；构建 Prompt 模板引擎，集成 Redis 会话上下文存储，保障多轮对话。
- 文件上传与解析：实现分片上传/断点续传、Redis BitMap 存储进度、MinIO 合并与 MD5 秒传；Kafka 异步解析，Tika 提取文本；向量化并写入 ES。
- 检索与权限：设计 Elasticsearch 混合检索（BM25 + 向量相似度），实现 topK 返回；基于组织标签的权限过滤，支持层级标签与 DEFAULT 全局访问。
- 认证与租户：JWT 登录/刷新/登出，Redis 黑名单与令牌集；组织标签模型与请求级鉴权过滤器。

