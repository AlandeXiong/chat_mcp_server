# 营销活动智能创建系统 - 完整架构文档

## 系统概述

本系统是一个基于AI的智能营销活动创建平台，通过多轮对话理解用户需求，基于模型上下文协议(MCP)读取接口参数，为投放渠道、投放策略、人群选择等节点提供AI建议，并支持用户确认后创建完整的营销活动。

## 核心特性

### 1. 智能多轮对话
- 基于Azure OpenAI的智能对话系统
- 自动识别用户意图和参数需求
- 引导式参数收集和确认流程

### 2. AI驱动的节点建议
- 人群选择节点：年龄、地域、职业、兴趣等细分建议
- 投放策略节点：渠道选择、频率、预算分配、时机优化
- 邮件模板节点：主题、内容、CTA、个性化字段建议
- 条件判断节点：流程路径、逻辑设计、目标连接建议
- 客户旅程节点：阶段设计、接触点、转化路径优化

### 3. 完整的营销活动创建流程
- 从对话到活动的端到端流程
- 基于AI建议的节点配置
- 活动状态管理和生命周期控制

## 系统架构

### 后端架构

```
┌─────────────────────────────────────────────────────────────┐
│                    Spring Boot Application                  │
├─────────────────────────────────────────────────────────────┤
│  Controllers Layer                                         │
│  ├── ChatController (智能对话API)                          │
│  └── ApiController (基础API)                               │
├─────────────────────────────────────────────────────────────┤
│  Services Layer                                            │
│  ├── IntelligentConversationManager (智能对话管理)         │
│  ├── AIRecommendationGenerator (AI建议生成)                │
│  ├── CampaignCreationService (活动创建服务)                │
│  └── MarketingCampaignService (营销活动服务)               │
├─────────────────────────────────────────────────────────────┤
│  Models Layer                                              │
│  ├── CampaignNode (活动节点定义)                           │
│  ├── CampaignNodeConfig (节点配置类)                       │
│  ├── ConversationContext (对话上下文)                      │
│  ├── ConversationResponse (对话响应)                       │
│  └── UserIntentAnalysis (用户意图分析)                     │
├─────────────────────────────────────────────────────────────┤
│  Configuration Layer                                       │
│  ├── SecurityConfig (安全配置)                             │
│  ├── SwaggerConfig (API文档配置)                           │
│  ├── CorsConfig (跨域配置)                                 │
│  └── McpServerConfig (MCP服务器配置)                       │
└─────────────────────────────────────────────────────────────┘
```

### 前端架构

```
┌─────────────────────────────────────────────────────────────┐
│                    React + ReactFlow                       │
├─────────────────────────────────────────────────────────────┤
│  Components                                                │
│  ├── ChatWidget (智能对话界面)                             │
│  ├── Campaign Nodes (营销活动节点)                         │
│  │   ├── SegmentNode (人群选择)                            │
│  │   ├── StrategyNode (投放策略)                           │
│  │   ├── EmailTemplateNode (邮件模板)                      │
│  │   ├── ConditionNode (条件判断)                          │
│  │   └── CustomerJourneyNode (客户旅程)                    │
│  └── Panels (节点配置面板)                                 │
├─────────────────────────────────────────────────────────────┤
│  Services                                                  │
│  └── chatService.js (后端通信服务)                         │
└─────────────────────────────────────────────────────────────┘
```

## 核心组件详解

### 1. 智能对话管理器 (IntelligentConversationManager)

**功能职责：**
- 管理多轮对话会话状态
- 分析用户意图和参数需求
- 协调AI建议生成和参数确认流程

**核心方法：**
```java
// 处理用户消息
public ConversationResponse processMessage(String userId, String message)

// 生成节点建议
private ConversationResponse generateNodeRecommendations(ConversationContext context)

// 确认参数
public ConversationResponse confirmParameters(String userId, Map<String, Object> parameters)
```

### 2. AI建议生成器 (AIRecommendationGenerator)

**功能职责：**
- 基于Azure OpenAI生成营销活动建议
- 为不同类型的节点提供专业配置建议
- 确保建议的一致性和可行性

**支持的建议类型：**
- 人群选择建议：年龄组、地理位置、职业兴趣等
- 投放策略建议：渠道选择、频率、预算分配
- 内容创意建议：邮件主题、正文、CTA等
- 流程设计建议：条件逻辑、路径连接等

### 3. 营销活动创建服务 (CampaignCreationService)

**功能职责：**
- 基于确认的参数和AI建议创建完整活动
- 管理活动节点和连接关系
- 控制活动生命周期状态

**创建流程：**
1. 验证用户参数和AI建议
2. 生成活动唯一标识
3. 创建各类营销节点
4. 建立节点间连接关系
5. 设置活动状态为就绪

### 4. 活动节点系统 (CampaignNode)

**节点类型：**
- **START**: 活动开始节点
- **SEGMENT**: 人群选择节点
- **STRATEGY**: 投放策略节点
- **EMAIL_TEMPLATE**: 邮件模板节点
- **CONDITION**: 条件判断节点
- **CUSTOMER_JOURNEY**: 客户旅程节点
- **END**: 活动结束节点

**节点状态：**
- **DRAFT**: 草稿状态
- **CONFIGURING**: 配置中
- **READY**: 就绪状态
- **ACTIVE**: 激活状态
- **PAUSED**: 暂停状态
- **COMPLETED**: 完成状态

## API接口设计

### 智能对话接口

#### 1. 发送消息
```
POST /api/chat/send
{
  "userId": "user123",
  "message": "我想创建一个保险产品的营销活动"
}
```

#### 2. 智能对话
```
POST /api/chat/intelligent-chat
{
  "userId": "user123",
  "message": "目标受众是25-35岁的年轻白领"
}
```

#### 3. 确认参数
```
POST /api/chat/confirm-parameters
{
  "userId": "user123",
  "parameters": {
    "campaignType": "保险产品推广",
    "targetAudience": "25-35岁年轻白领",
    "budget": 10000.0,
    "duration": "3个月"
  }
}
```

### 活动创建接口

#### 1. 创建营销活动
```
POST /api/chat/create-campaign
{
  "userId": "user123",
  "confirmedParams": {
    "campaignName": "保险产品推广活动",
    "campaignType": "保险产品推广",
    "targetAudience": "25-35岁年轻白领",
    "budget": 10000.0,
    "duration": "3个月"
  },
  "aiRecommendations": {
    "segment": {...},
    "strategy": {...},
    "emailTemplate": {...},
    "condition": {...},
    "customerJourney": {...}
  }
}
```

#### 2. 激活活动
```
POST /api/chat/activate-campaign/{campaignId}
```

#### 3. 获取活动状态
```
GET /api/chat/campaign-status/{campaignId}
```

## 数据流程

### 1. 用户对话流程
```
用户输入 → 意图识别 → 参数提取 → 信息收集 → AI建议生成 → 用户确认 → 活动创建
```

### 2. AI建议生成流程
```
用户参数 → 人群分析 → 策略建议 → 内容建议 → 流程设计 → 综合建议
```

### 3. 活动创建流程
```
确认参数 + AI建议 → 节点创建 → 连接建立 → 状态设置 → 活动就绪
```

## 配置要求

### 环境变量
```bash
# Azure OpenAI配置
AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
AZURE_OPENAI_API_KEY=your-api-key
AZURE_OPENAI_DEPLOYMENT_NAME=your-deployment-name

# 服务器配置
SERVER_PORT=8088
```

### 应用配置 (application.yml)
```yaml
spring:
  ai:
    azure:
      openai:
        endpoint: ${AZURE_OPENAI_ENDPOINT}
        api-key: ${AZURE_OPENAI_API_KEY}
        deployment-name: ${AZURE_OPENAI_DEPLOYMENT_NAME}
    mcp:
      server:
        enabled: true
        transport: stdio
        stdio:
          enabled: true
      client:
        enabled: false
    model:
      tool:
        enabled: false

server:
  port: ${SERVER_PORT:8088}

# 禁用安全配置
spring:
  security:
    enabled: false
  security:
    basic:
      enabled: false

# 禁用Actuator安全
management:
  security:
    enabled: false
  endpoints:
    web:
      base-path: /actuator

# Swagger配置
springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html
```

## 部署和运行

### 1. 编译项目
```bash
cd campaign-journey-backend
mvn clean compile -DskipTests
```

### 2. 启动服务器
```bash
# Linux/Mac
./start-mcp-server.sh

# Windows
start-mcp-server.bat
```

### 3. 启动前端
```bash
cd reactflow-campaign-demo
npm run dev
```

### 4. 访问地址
- 前端应用: http://localhost:5173
- 后端API: http://localhost:8088
- Swagger文档: http://localhost:8088/swagger-ui.html
- API文档: http://localhost:8088/api-docs

## 使用示例

### 1. 创建保险产品营销活动
```
用户: "我想创建一个保险产品的营销活动"
AI: "好的！请告诉我以下信息：
     1. 活动类型（如：保险产品推广、品牌宣传等）
     2. 目标受众描述
     3. 预算范围
     4. 活动时长"

用户: "目标受众是25-35岁的年轻白领，预算1万元，活动3个月"
AI: "很好！基于您的需求，我为您生成以下建议：

     人群选择建议：
     - 年龄组：25-35岁
     - 地理位置：一线城市
     - 职业：年轻白领
     - 需求：职业发展、生活品质提升
     
     投放策略建议：
     - 渠道：Email(40%) + 社交媒体(40%) + SMS(20%)
     - 频率：每周3次
     - 时机：工作日9-11点，周末14-16点
     
     请确认这些建议，我将为您创建完整的营销活动。"
```

### 2. 活动创建结果
```json
{
  "success": true,
  "campaign": {
    "id": "campaign_12345",
    "name": "保险产品推广活动",
    "status": "READY",
    "nodes": [
      {
        "id": "campaign_12345_start",
        "type": "START",
        "name": "开始",
        "status": "READY"
      },
      {
        "id": "campaign_12345_segment",
        "type": "SEGMENT",
        "name": "人群选择",
        "status": "READY",
        "data": {
          "ageGroup": "25-35岁",
          "location": "一线城市",
          "occupation": "年轻白领"
        }
      },
      {
        "id": "campaign_12345_strategy",
        "type": "STRATEGY",
        "name": "投放策略",
        "status": "READY",
        "data": {
          "channels": ["email", "social", "sms"],
          "frequency": 3,
          "budgetAllocation": {"email": 40, "social": 40, "sms": 20}
        }
      }
    ],
    "connections": [
      {
        "id": "conn_0",
        "source": "campaign_12345_start",
        "target": "campaign_12345_segment"
      }
    ]
  }
}
```

## 扩展性设计

### 1. 新增节点类型
- 在`CampaignNode.NodeType`枚举中添加新类型
- 在`CampaignNodeConfig`中创建对应的配置类
- 在`AIRecommendationGenerator`中添加建议生成方法
- 在`CampaignCreationService`中添加节点创建逻辑

### 2. 新增AI建议类型
- 扩展`AIRecommendationGenerator`类
- 添加新的建议生成方法
- 更新对话流程以支持新建议类型

### 3. 集成外部营销平台
- 创建适配器接口
- 实现具体的平台集成
- 在活动创建服务中调用外部API

## 监控和日志

### 1. 应用监控
- 使用Spring Boot Actuator监控应用健康状态
- 监控AI建议生成的响应时间
- 跟踪活动创建的成功率

### 2. 日志记录
- 记录用户对话流程
- 记录AI建议生成过程
- 记录活动创建和状态变更

### 3. 性能指标
- 对话响应时间
- AI建议生成时间
- 活动创建处理时间

## 安全考虑

### 1. 数据保护
- 用户对话数据加密存储
- 敏感信息脱敏处理
- 访问权限控制

### 2. API安全
- 请求频率限制
- 输入验证和过滤
- 错误信息脱敏

### 3. AI模型安全
- 提示词注入防护
- 输出内容过滤
- 使用限制和监控

## 故障排除

### 1. 常见问题
- **AI建议生成失败**: 检查Azure OpenAI配置和网络连接
- **活动创建失败**: 验证参数完整性和数据格式
- **对话状态丢失**: 检查会话管理和存储配置

### 2. 调试方法
- 启用详细日志记录
- 使用Swagger UI测试API接口
- 检查应用健康状态端点

### 3. 性能优化
- 缓存AI建议结果
- 异步处理长时间任务
- 数据库查询优化

## 总结

本系统通过AI驱动的智能对话和专业的营销活动建议，大大提升了用户创建营销活动的效率。系统架构清晰，组件职责明确，具有良好的扩展性和维护性。通过模型上下文协议，系统能够智能理解用户需求，为各种营销节点提供专业建议，最终创建完整、可执行的营销活动。

整个系统体现了现代AI应用的最佳实践，将自然语言交互、智能建议生成和业务流程自动化有机结合，为用户提供了直观、高效的营销活动创建体验。
