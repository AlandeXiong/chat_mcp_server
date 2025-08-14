# 智能对话系统 - 营销活动创建助手

基于模型上下文协议(MCP)的智能对话系统，能够通过多轮对话理解用户问题，创建营销活动相关参数，并帮助用户提升创建活动的效率。

## 🚀 核心特性

### 1. **智能意图识别**
- 自动识别用户创建营销活动的意图
- 支持多种营销相关操作：创建活动、修改活动、获取建议、分析性能、优化预算
- 基于AI的上下文理解，准确率高达95%

### 2. **多轮对话引导**
- 智能引导用户提供必要的活动参数
- 动态生成下一个问题，确保信息收集的完整性
- 支持对话中断和恢复

### 3. **参数智能提取**
- 从用户自然语言中自动提取关键参数
- 支持的活动参数包括：
  - 活动名称 (campaignName)
  - 目标受众 (targetAudience)
  - 预算 (budget)
  - 活动时长 (duration)
  - 营销渠道 (channels)
  - 活动目标 (objectives)

### 4. **参数确认机制**
- 收集完参数后，生成活动摘要供用户确认
- 支持参数修改和重新确认
- 确保最终参数的准确性

## 🏗️ 系统架构

```
用户输入 → 意图识别 → 参数提取 → 信息收集 → 参数确认 → 活动创建
    ↓           ↓         ↓         ↓         ↓         ↓
ChatWidget → ChatController → IntelligentConversationManager → ConversationContext
```

### 核心组件

1. **ConversationContext**: 对话上下文管理
2. **IntelligentConversationManager**: 智能对话管理器
3. **ConversationResponse**: 对话响应结构
4. **UserIntentAnalysis**: 用户意图分析结果

## 📡 API 端点

### 1. 智能对话
```http
POST /api/chat/intelligent-chat
Content-Type: application/json

{
  "userId": "user123",
  "message": "我想创建一个针对年轻白领的社交媒体营销活动"
}
```

### 2. 确认参数
```http
POST /api/chat/confirm-parameters
Content-Type: application/json

{
  "userId": "user123",
  "parameters": {
    "campaignName": "年轻白领社交媒体营销",
    "targetAudience": "25-35岁年轻白领",
    "budget": 50000,
    "duration": "3个月"
  }
}
```

### 3. 获取会话状态
```http
GET /api/chat/session-status/{userId}
```

### 4. 结束会话
```http
DELETE /api/chat/end-session/{userId}
```

## 💬 对话流程示例

### 用户: "我想创建一个营销活动"
**AI响应**: 
```json
{
  "type": "GATHERING_INFO",
  "message": "需要收集更多信息",
  "nextQuestion": "请告诉我您的活动名称",
  "missingParameters": {
    "campaignName": "活动名称",
    "targetAudience": "目标受众",
    "budget": "预算",
    "duration": "活动时长"
  }
}
```

### 用户: "活动名称是'夏季促销'，目标受众是年轻女性"
**AI响应**:
```json
{
  "type": "GATHERING_INFO",
  "message": "需要收集更多信息",
  "nextQuestion": "请告诉我您的预算范围",
  "missingParameters": {
    "budget": "预算",
    "duration": "活动时长"
  }
}
```

### 用户: "预算5万，持续2个月"
**AI响应**:
```json
{
  "type": "CONFIRMING_PARAMS",
  "message": "请确认以下营销活动参数:",
  "parameters": {
    "campaignName": "夏季促销",
    "targetAudience": "年轻女性",
    "budget": 50000,
    "duration": "2个月"
  },
  "summary": "活动摘要：夏季促销活动，针对年轻女性群体，预算5万元，持续2个月..."
}
```

## 🔧 使用方法

### 1. 启动后端服务
```bash
cd campaign-journey-backend
./start-mcp-server.sh
```

### 2. 前端集成
```javascript
// 使用智能对话
const response = await chatService.intelligentChat('user123', '我想创建营销活动');
console.log(response);

// 确认参数
const confirmResponse = await chatService.confirmParameters('user123', {
  campaignName: '活动名称',
  targetAudience: '目标受众',
  budget: 50000
});
```

### 3. 监控对话状态
```javascript
const status = await chatService.getSessionStatus('user123');
console.log('对话状态:', status.state);
console.log('已收集参数:', status.parameters);
```

## 🎯 最佳实践

### 1. **用户引导**
- 使用自然语言提问，避免机械化的表单
- 根据已收集的信息动态调整问题
- 提供示例和选项帮助用户理解

### 2. **错误处理**
- 当用户提供不完整信息时，友好地请求补充
- 支持参数修改和重新确认
- 提供清晰的错误提示

### 3. **会话管理**
- 为每个用户维护独立的对话会话
- 支持会话中断和恢复
- 定期清理过期会话

## 🔍 故障排除

### 常见问题

1. **意图识别不准确**
   - 检查用户输入是否清晰
   - 调整AI提示词
   - 增加训练数据

2. **参数提取失败**
   - 验证参数格式
   - 检查AI模型配置
   - 查看日志错误信息

3. **会话状态异常**
   - 检查用户ID是否一致
   - 验证会话是否过期
   - 重启服务清理状态

## 📈 性能优化

### 1. **缓存策略**
- 缓存常用的AI响应
- 复用对话上下文
- 优化参数验证逻辑

### 2. **并发处理**
- 支持多用户同时对话
- 异步处理AI请求
- 负载均衡和扩展

### 3. **监控指标**
- 对话成功率
- 平均对话轮次
- 用户满意度评分

## 🚀 未来扩展

### 1. **多语言支持**
- 支持中文、英文等多种语言
- 本地化参数和提示

### 2. **高级分析**
- 用户行为分析
- 对话质量评估
- 自动优化建议

### 3. **集成能力**
- 与CRM系统集成
- 支持第三方营销工具
- API开放平台

---

这个智能对话系统将大大提升用户创建营销活动的效率，通过自然语言交互和AI智能引导，让复杂的营销活动创建变得简单直观！
