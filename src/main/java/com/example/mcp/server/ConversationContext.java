package com.example.mcp.server;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * ConversationContext
 *
 */
public class ConversationContext {
    
    private String sessionId;
    private String userId;
    private LocalDateTime startTime;
    private LocalDateTime lastUpdateTime;
    private ConversationState state;
    private UserIntent intent;
    private Map<String, Object> parameters;
    private Map<String, Object> confirmedParameters;
    private String currentQuestion;
    private int turnCount;
    
    public enum ConversationState {
        INITIAL,           // 初始状态
        GATHERING_INFO,    // 收集信息
        CONFIRMING_PARAMS, // 确认参数
        CREATING_CAMPAIGN, // 创建活动
        COMPLETED,         // 完成
        ERROR             // 错误
    }
    
    public enum UserIntent {
        CREATE_CAMPAIGN,      // 创建营销活动
        MODIFY_CAMPAIGN,      // 修改活动
        GET_ADVICE,           // 获取建议
        ANALYZE_PERFORMANCE,  // 分析性能
        OPTIMIZE_BUDGET,      // 优化预算
        UNKNOWN               // 未知意图
    }
    
    public ConversationContext(String userId) {
        this.sessionId = UUID.randomUUID().toString();
        this.userId = userId;
        this.startTime = LocalDateTime.now();
        this.lastUpdateTime = LocalDateTime.now();
        this.state = ConversationState.INITIAL;
        this.intent = UserIntent.UNKNOWN;
        this.parameters = new HashMap<>();
        this.confirmedParameters = new HashMap<>();
        this.turnCount = 0;
    }
    
    // Getters and Setters
    public String getSessionId() { return sessionId; }
    public String getUserId() { return userId; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getLastUpdateTime() { return lastUpdateTime; }
    public ConversationState getState() { return state; }
    public UserIntent getIntent() { return intent; }
    public Map<String, Object> getParameters() { return parameters; }
    public Map<String, Object> getConfirmedParameters() { return confirmedParameters; }
    public String getCurrentQuestion() { return currentQuestion; }
    public int getTurnCount() { return turnCount; }
    
    public void setState(ConversationState state) { 
        this.state = state; 
        this.lastUpdateTime = LocalDateTime.now();
    }
    
    public void setIntent(UserIntent intent) { 
        this.intent = intent; 
        this.lastUpdateTime = LocalDateTime.now();
    }
    
    public void setCurrentQuestion(String question) { 
        this.currentQuestion = question; 
        this.lastUpdateTime = LocalDateTime.now();
    }
    
    public void incrementTurnCount() { 
        this.turnCount++; 
        this.lastUpdateTime = LocalDateTime.now();
    }
    
    public void addParameter(String key, Object value) {
        this.parameters.put(key, value);
        this.lastUpdateTime = LocalDateTime.now();
    }
    
    public void addConfirmedParameter(String key, Object value) {
        this.confirmedParameters.put(key, value);
        this.lastUpdateTime = LocalDateTime.now();
    }
    
    public boolean hasParameter(String key) {
        return parameters.containsKey(key) || confirmedParameters.containsKey(key);
    }
    
    public Object getParameter(String key) {
        return parameters.getOrDefault(key, confirmedParameters.get(key));
    }
    
    public boolean isComplete() {
        return state == ConversationState.COMPLETED;
    }
    
    public boolean needsMoreInfo() {
        return state == ConversationState.GATHERING_INFO;
    }
    
    public boolean isConfirming() {
        return state == ConversationState.CONFIRMING_PARAMS;
    }
    
    @Override
    public String toString() {
        return String.format("ConversationContext{sessionId='%s', userId='%s', state=%s, intent=%s, turnCount=%d}", 
            sessionId, userId, state, intent, turnCount);
    }
}
