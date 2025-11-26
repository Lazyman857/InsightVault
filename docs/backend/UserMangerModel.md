✅RAG用户管理模块设计方案

**用户管理模块**负责处理用户的**注册、登录和权限控制**功能。该模块的核心目标是：

- 确保用户身份的安全性。
- 提供灵活的权限管理机制，支持基于角色的访问控制，通过 RBAC 实现对不同角色（如普通用户和管理员）的功能权限区分，通过组织标签实现数据访问权限隔离。
- 为其他模块提供用户信息支持。

## 一、功能需求

![用户管理设计方案](https://cdn.tobebetterjavaer.com/paicoding/957c4737041d8e4826e2b4b9070c6b63.jpeg)用户管理设计方案



## 二、技术选型



![用户管理技术选项](https://cdn.tobebetterjavaer.com/paicoding/06101e387550b06d6d17a85b187c64b6.jpeg)用户管理技术选项



## 三、关键流程

### 01、用户注册流程

- 接收用户注册请求，验证用户名和密码；
- 检查用户名是否已存在；
- 使用 BCrypt 加密密码；
- 创建用户记录，设置默认角色为USER;
- 创建用户私人组织标签（PRIVATE_username）；
- 将私人组织标签设置为用户的主组织标签；返回注册成功响应。

![img](https://cdn.tobebetterjavaer.com/paicoding/eee639efcc244bc6836b78c4b3bf4154.png)

### 02、用户登录流程

- 接收用户登录请求，获取用户名和密码；
- 查询用户记录并验证密码；加载用户组织标签信息；
- 生成包含用户信息和组织标签的 JWT Token；
- 返回登录成功响应和 Token。

![img](https://cdn.tobebetterjavaer.com/paicoding/84f416b2ddd5a0138f48d490e8a04549.png)

### 03、组织标签管理流程

- 管理员创建组织标签，设置标签名称和描述；
- 可选择设置父级组织标签（支持简单层级）；
- 管理员为用户分配组织标签；
- 系统自动保留用户的私人组织标签，确保其不被移除；
- 用户查看自己的组织标签。

### 04、权限验证流程

- 解析请求头中的 JWT Token，验证有效性；
- 提取用户 ID、角色和组织标签信息；
- 对功能权限请求，根据用户角色判断是否允许访问；
- 对数据权限请求，根据用户组织标签判断是否可以访问特定资源；
- 允许或拒绝请求访问；权限验证流程具体讲解见：[✅如何基于Spring Security实现RBAC？](https://paicoding.com/column/10/28)

![img](https://cdn.tobebetterjavaer.com/paicoding/5b7875e06d038428f89f8dfcaf246f1f.png)

## 四、接口设计

### 01、用户注册接口

- URL: /api/v1/users/register
- Method: POST
- Request Body:

```json
{
  "username": "string",   // 用户名，唯一
  "password": "string"    // 密码（明文传输，后端加密存储）
}
复制代码
```

- Response:

```json
{
  "code": 200, // 成功
  "message": "User registered successfully"
}

{
  "code": 400, // 失败
  "message": "Username already exists"
}
复制代码
```

### 02、用户登录接口

- URL: /api/v1/users/login
- Method: POST
- Request Body:

```json
{
  "username": "string",
  "password": "string"
}
复制代码
```

- Response:

```json
{
  "code": 200, // 成功
  "message": "Login successful",
  "data": {
    "token": "JWT_TOKEN_STRING"
  }
}

{
  "code": 401, // 失败
  "message": "Invalid username or password"
}
复制代码
```

### 03、获取用户信息接口

- URL: /api/v1/users/me
- Method: GET
- Headers:
  - Authorization: Bearer JWT_TOKEN_STRING
- Response:

```json
{
  "code": 200,// 成功
  "message": "Success",
  "data": {
    "id": 1,
    "username": "example_user",
    "role": "USER",
    "orgTags": ["PRIVATE_example_user", "dept1", "team2"],
    "primaryOrg": "PRIVATE_example_user"
  }
}

{
  "code": 401, // 失败
  "message": "Unauthorized"
}
复制代码
```

### 04、用户列表查询接口

- URL: /api/v1/admin/users/list
- Method: GET
- Headers:
  - Authorization: Bearer JWT_TOKEN_STRING
- Query Parameters:
  - page: 页码（从1开始，默认1）
  - size: 每页条数（默认20）
  - keyword: 搜索关键词（可选）
  - orgTag: 按组织标签筛选（可选）
  - status: 用户状态（可选，0=禁用，1=启用
- Response:

```json
{
  "code": 200,
  "message": "Get users successful",
  "data": {
    "content": [
      {
        "userId": "user1",
        "username": "用户1",
        "email": "user1@example.com",
        "status": 1,
        "orgTags": ["dept1", "team2"],
        "primaryOrg": "dept1",
        "createTime": "2023-01-01T12:00:00Z",
        "lastLoginTime": "2023-06-15T08:30:00Z"
      },
      // 更多用户...
    ],
    "totalElements": 150,
    "totalPages": 8,
    "size": 20,
    "number": 0
  }
}
复制代码
```

### 05、组织标签管理接口

#### 5.1 创建组织标签（管理员）

- URL: /api/v1/admin/org-tags
- Method: POST
- Headers:
  - Authorization: Bearer JWT_TOKEN_STRING
- Request Body:

```json
{
  "tagId": "string",     // 标签ID，唯一
  "name": "string",      // 标签名称
  "description": "string", // 标签描述
  "parentTag": "string"  // 父标签ID（可选）
}
复制代码
```

- Response:

```json
{
  "code": 200, // 成功
  "message": "Organization tag created successfully"
}
复制代码
```

#### 5.2 为用户分配组织标签（admin）

- URL: /api/v1/admin/users/{userId}/org-tags
- Method: PUT
- Headers:
  - Authorization: Bearer JWT_TOKEN_STRING
- Request Body:

```json
{
  "orgTags": ["tag1", "tag2"]
}
复制代码
```

- Response:

```json
{
  "code": 200, //   成功：
  "message": "Organization tags assigned successfully"
}
复制代码
```

#### 5.3 设置用户主组织

- URL: /api/v1/users/primary-org
- Method: PUT
- Headers:
  - Authorization: Bearer JWT_TOKEN_STRING
- Request Body:

```json
{
  "primaryOrg": "tag1",
  "userId":"xxx"
}
复制代码
```

- Response:

```json
{
  "code": 200,
  "message": "Primary organization set successfully"
}
复制代码
```

#### 5.4 获取用户组织标签详情

- URL: /api/v1/users/org-tags
- Method: GET
- Headers:
  - Authorization: Bearer JWT_TOKEN_STRING
- Response:

```json
{
  "code": 200,
  "message": "Get user organization tags successful",
  "data": {
    "orgTags": ["PRIVATE_example_user", "dept1", "team2"],
    "primaryOrg": "PRIVATE_example_user",
    "orgTagDetails": [
      {
        "tagId": "PRIVATE_example_user",
        "name": "example_user的私人空间",
        "description": "用户的私人组织标签，仅用户本人可访问"
      },
      {
        "tagId": "dept1",
        "name": "部门1",
        "description": "部门1的组织标签"
      },
      {
        "tagId": "team2",
        "name": "团队2",
        "description": "团队2的组织标签"
      }
    ]
  }
}
复制代码
```

#### 5.5 组织标签树查询接口 (admin)

- URL: /api/v1/admin/org-tags/tree
- Method: GET
- Headers:
  - Authorization: Bearer JWT_TOKEN_STRING
- Response:

```josn
{
  "code": 200,
  "message": "Get organization tag tree successful",
  "data": [
    {
      "tagId": "dept1",
      "name": "部门1",
      "description": "部门1描述",
      "children": [
        {
          "tagId": "team1",
          "name": "团队1",
          "description": "团队1描述",
          "children": []
        },
        {
          "tagId": "team2",
          "name": "团队2",
          "description": "团队2描述",
          "children": []
        }
      ]
    },
    {
      "tagId": "dept2",
      "name": "部门2",
      "description": "部门2描述",
      "children": []
    }
  ]
}
```

#### 5.6 更新组织标签接口(admin)

- URL: /api/v1/admin/org-tags/{tagId}
- Method: PUT
- Headers:
  - Authorization: Bearer JWT_TOKEN_STRING
- Request Body:

```plain
{
  "name": "string",       // 新标签名称
  "description": "string", // 新标签描述
  "parentTag": "string"    // 新父标签ID（可选）
}
```

- Response:

```josn
{
  "code": 200,
  "message": "Organization tag updated successfully"
}
```

#### 5.7 删除组织标签接口

- URL: /api/v1/admin/org-tags/{tagId}
- Method: DELETE
- Headers:
  - Authorization: Bearer JWT_TOKEN_STRING
- Response:

```json
{
  "code": 200, // 成功
  "message": "Organization tag deleted successfully"
}
{
  "code": 409, // 错误（标签已被使用）：
  "message": "Cannot delete tag as it is associated with users or documents"
}
复制代码
```

## 五、库表设计

### 01、用户表 (users)

| 字段名      | 数据类型              | 是否主键 | 描述                             |
| ----------- | --------------------- | -------- | -------------------------------- |
| id          | BIGINT                | 是       | 用户唯一标识                     |
| username    | VARCHAR(255)          | 否       | 用户名，唯一                     |
| password    | VARCHAR(255)          | 否       | 加密后的密码                     |
| role        | ENUM('USER', 'ADMIN') | 否       | 用户角色                         |
| org_tags    | VARCHAR(255)          | 否       | 用户所属组织标签，多个用逗号分隔 |
| primary_org | VARCHAR(50)           | 否       | 用户主组织标签                   |
| created_at  | TIMESTAMP             | 否       | 创建时间                         |
| updated_at  | TIMESTAMP             | 否       | 更新时间                         |

建表语句：

```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户唯一标识',
    username VARCHAR(255) NOT NULL UNIQUE COMMENT '用户名，唯一',
    password VARCHAR(255) NOT NULL COMMENT '加密后的密码',
    role ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER' COMMENT '用户角色',
    org_tags VARCHAR(255) DEFAULT NULL COMMENT '用户所属组织标签，多个用逗号分隔',
    primary_org VARCHAR(50) DEFAULT NULL COMMENT '用户主组织标签',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username) COMMENT '用户名索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
复制代码
```

### 02、组织标签表

| 字段名      | 数据类型     | 是否主键 | 描述         |
| ----------- | ------------ | -------- | ------------ |
| tag_id      | VARCHAR(50)  | 是       | 标签唯一标识 |
| name        | VARCHAR(100) | 否       | 标签名称     |
| description | TEXT         | 否       | 描述         |
| parent_tag  | VARCHAR(50)  | 否       | 父标签ID     |
| created_by  | BIGINT       | 否       | 创建者ID     |
| created_at  | TIMESTAMP    | 否       | 创建时间     |
| updated_at  | TIMESTAMP    | 否       | 更新时间     |

建表语句：

```sql
CREATE TABLE organization_tags (
    tag_id VARCHAR(50) PRIMARY KEY COMMENT '标签唯一标识',
    name VARCHAR(100) NOT NULL COMMENT '标签名称',
    description TEXT COMMENT '描述',
    parent_tag VARCHAR(50) DEFAULT NULL COMMENT '父标签ID',
    created_by BIGINT NOT NULL COMMENT '创建者ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (parent_tag) REFERENCES organization_tags(tag_id) ON DELETE SET NULL,
    FOREIGN KEY (created_by) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='组织标签表';
```