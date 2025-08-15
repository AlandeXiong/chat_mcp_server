package com.example.mcp.server;

import java.util.Map;

/**
 * Dialogue Response Class
 *
 */
public class ConversationResponse {
    
    private ResponseType type;
    private String message;
    private Map<String, Object> parameters;
    private Map<String, String> missingParameters;
    private String nextQuestion;
    private String summary;
    private String action;
    private boolean requiresUserAction;
    
    public enum ResponseType {
        INFO,               // 信息性响应
        GATHERING_INFO,     // 收集信息
        CONFIRMING_PARAMS,  // 确认参数
        NODE_RECOMMENDATIONS, // 节点建议
        COMPLETED,          // 完成
        ERROR               // 错误
    }
    
    // 私有构造函数
    private ConversationResponse(ResponseType type, String message) {
        this.type = type;
        this.message = message;
        this.requiresUserAction = false;
    }
    
    // 静态工厂方法
    public static ConversationResponse info(String message) {
        return new ConversationResponse(ResponseType.INFO, message);
    }
    
    public static ConversationResponse gatheringInfo(String nextQuestion, Map<String, String> missingParams, Map<String, Object> currentParams) {
        ConversationResponse response = new ConversationResponse(ResponseType.GATHERING_INFO, "需要收集更多信息");
        response.nextQuestion = nextQuestion;
        response.missingParameters = missingParams;
        response.parameters = currentParams;
        response.requiresUserAction = true;
        return response;
    }
    
    public static ConversationResponse confirmingParameters(String message, Map<String, Object> params, String summary) {
        ConversationResponse response = new ConversationResponse(ResponseType.CONFIRMING_PARAMS, message);
        response.parameters = params;
        response.summary = summary;
        response.requiresUserAction = true;
        return response;
    }
    
    public static ConversationResponse completed(String message, Map<String, Object> finalParams, String summary) {
        ConversationResponse response = new ConversationResponse(ResponseType.COMPLETED, message);
        response.parameters = finalParams;
        response.summary = summary;
        response.requiresUserAction = false;
        return response;
    }
    
    public static ConversationResponse nodeRecommendations(String message, Map<String, Object> recommendations, String summary) {
        ConversationResponse response = new ConversationResponse(ResponseType.NODE_RECOMMENDATIONS, message);
        response.parameters = recommendations;
        response.summary = summary;
        response.requiresUserAction = true;
        return response;
    }
    
    public static ConversationResponse error(String message) {
        ConversationResponse response = new ConversationResponse(ResponseType.ERROR, message);
        response.requiresUserAction = false;
        return response;
    }
    
    // Getters
    public ResponseType getType() { return type; }
    public String getMessage() { return message; }
    public Map<String, Object> getParameters() { return parameters; }
    public Map<String, String> getMissingParameters() { return missingParameters; }
    public String getNextQuestion() { return nextQuestion; }
    public String getSummary() { return summary; }
    public String getAction() { return action; }
    public boolean isRequiresUserAction() { return requiresUserAction; }
    
    // 设置额外信息的方法
    public ConversationResponse withAction(String action) {
        this.action = action;
        return this;
    }
    
    public ConversationResponse withParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
        return this;
    }
    
    @Override
    public String toString() {
        return String.format("ConversationResponse{type=%s, message='%s', requiresUserAction=%s}", 
            type, message, requiresUserAction);
    }
}
