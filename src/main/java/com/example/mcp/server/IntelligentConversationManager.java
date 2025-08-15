package com.example.mcp.server;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * Dialog Manager for Intelligent Marketing Campaign Conversations
 *
 */
@Service
public class IntelligentConversationManager {
    
    @Autowired
    private ChatClient chatClient;
    
    @Autowired
    private AIRecommendationGenerator recommendationGenerator;
    
    // 存储活跃的对话会话
    private final Map<String, ConversationContext> activeSessions = new ConcurrentHashMap<>();
    
    /**
     * 处理用户消息，返回AI响应和下一步操作
     */
    public ConversationResponse processMessage(String userId, String message) {
        // 获取或创建对话上下文
        ConversationContext context = getOrCreateContext(userId);
        context.incrementTurnCount();
        
        try {
            // 分析用户意图
            UserIntentAnalysis intentAnalysis = analyzeUserIntent(message, context);
            context.setIntent(intentAnalysis.getIntent());
            
            // 根据意图和当前状态处理消息
            ConversationResponse response = handleMessageByIntent(message, context, intentAnalysis);
            
            // 更新对话状态
            updateConversationState(context, response);
            
            return response;
            
        } catch (Exception e) {
            context.setState(ConversationContext.ConversationState.ERROR);
            return ConversationResponse.error("抱歉，处理您的消息时出现了错误: " + e.getMessage());
        }
    }
    
    /**
     * 分析用户意图
     */
    private UserIntentAnalysis analyzeUserIntent(String message, ConversationContext context) {
        String prompt = String.format("""
            分析用户的营销相关意图。用户消息: %s
            当前对话轮次: %d
            已收集的参数: %s
            
            请识别用户意图并提取关键信息。可能的意图包括:
            - CREATE_CAMPAIGN: 创建营销活动
            - MODIFY_CAMPAIGN: 修改活动
            - GET_ADVICE: 获取建议
            - ANALYZE_PERFORMANCE: 分析性能
            - OPTIMIZE_BUDGET: 优化预算
            
            返回JSON格式:
            {
                "intent": "意图类型",
                "confidence": 0.95,
                "extractedParams": {"参数名": "参数值"},
                "requiresMoreInfo": true/false,
                "nextQuestion": "下一个问题"
            }
            """, message, context.getTurnCount(), context.getParameters());
        
        String aiResponse = chatClient.prompt().user(prompt).call().content();
        return parseIntentAnalysis(aiResponse);
    }
    
    /**
     * 根据意图处理消息
     */
    private ConversationResponse handleMessageByIntent(String message, ConversationContext context, UserIntentAnalysis intentAnalysis) {
        switch (intentAnalysis.getIntent()) {
            case CREATE_CAMPAIGN:
                return handleCreateCampaign(message, context, intentAnalysis);
            case MODIFY_CAMPAIGN:
                return handleModifyCampaign(message, context, intentAnalysis);
            case GET_ADVICE:
                return handleGetAdvice(message, context, intentAnalysis);
            case ANALYZE_PERFORMANCE:
                return handleAnalyzePerformance(message, context, intentAnalysis);
            case OPTIMIZE_BUDGET:
                return handleOptimizeBudget(message, context, intentAnalysis);
            default:
                return handleUnknownIntent(message, context);
        }
    }
    
    /**
     * 处理创建营销活动的意图
     */
    private ConversationResponse handleCreateCampaign(String message, ConversationContext context, UserIntentAnalysis intentAnalysis) {
        // 提取用户提供的参数
        if (intentAnalysis.getExtractedParams() != null) {
            intentAnalysis.getExtractedParams().forEach(context::addParameter);
        }
        
        // 检查是否还需要更多信息
        if (intentAnalysis.isRequiresMoreInfo()) {
            context.setState(ConversationContext.ConversationState.GATHERING_INFO);
            return ConversationResponse.gatheringInfo(
                intentAnalysis.getNextQuestion(),
                getMissingParameters(context),
                context.getParameters()
            );
        }
        
        // 如果信息足够，进入节点建议阶段
        if (hasRequiredParameters(context)) {
            context.setState(ConversationContext.ConversationState.CONFIRMING_PARAMS);
            return generateNodeRecommendations(context);
        }
        
        // 继续收集信息
        String nextQuestion = generateNextQuestion(context);
        context.setCurrentQuestion(nextQuestion);
        return ConversationResponse.gatheringInfo(nextQuestion, getMissingParameters(context), context.getParameters());
    }
    
    /**
     * 生成节点建议
     */
    private ConversationResponse generateNodeRecommendations(ConversationContext context) {
        try {
            String campaignType = (String) context.getParameter("campaignType");
            String targetAudience = (String) context.getParameter("targetAudience");
            Double budget = (Double) context.getParameter("budget");
            String duration = (String) context.getParameter("duration");
            
            // 生成人群选择建议
            Map<String, Object> segmentRecommendations = recommendationGenerator.generateSegmentRecommendations(
                campaignType, targetAudience, budget);
            
            // 生成投放策略建议
            Map<String, Object> strategyRecommendations = recommendationGenerator.generateStrategyRecommendations(
                campaignType, targetAudience, budget, segmentRecommendations);
            
            // 生成邮件模板建议
            Map<String, Object> emailTemplateRecommendations = recommendationGenerator.generateEmailTemplateRecommendations(
                campaignType, targetAudience, segmentRecommendations, strategyRecommendations);
            
            // 生成条件判断建议
            Map<String, Object> conditionRecommendations = recommendationGenerator.generateConditionRecommendations(
                campaignType, segmentRecommendations, strategyRecommendations);
            
            // 生成客户旅程建议
            Map<String, Object> journeyRecommendations = recommendationGenerator.generateCustomerJourneyRecommendations(
                campaignType, targetAudience, segmentRecommendations);
            
            // 构建建议响应
            Map<String, Object> allRecommendations = new HashMap<>();
            allRecommendations.put("segment", segmentRecommendations);
            allRecommendations.put("strategy", strategyRecommendations);
            allRecommendations.put("emailTemplate", emailTemplateRecommendations);
            allRecommendations.put("condition", conditionRecommendations);
            allRecommendations.put("customerJourney", journeyRecommendations);
            
            context.addParameter("aiRecommendations", allRecommendations);
            
            return ConversationResponse.nodeRecommendations(
                "AI已为您的营销活动生成了详细的节点配置建议，请逐一确认：",
                allRecommendations,
                generateCampaignSummary(context)
            );
            
        } catch (Exception e) {
            return ConversationResponse.error("生成节点建议时出现错误: " + e.getMessage());
        }
    }
    
    /**
     * 处理其他意图的方法...
     */
    private ConversationResponse handleModifyCampaign(String message, ConversationContext context, UserIntentAnalysis intentAnalysis) {
        return ConversationResponse.info("修改活动功能正在开发中...");
    }
    
    private ConversationResponse handleGetAdvice(String message, ConversationContext context, UserIntentAnalysis intentAnalysis) {
        return ConversationResponse.info("获取建议功能正在开发中...");
    }
    
    private ConversationResponse handleAnalyzePerformance(String message, ConversationContext context, UserIntentAnalysis intentAnalysis) {
        return ConversationResponse.info("性能分析功能正在开发中...");
    }
    
    private ConversationResponse handleOptimizeBudget(String message, ConversationContext context, UserIntentAnalysis intentAnalysis) {
        return ConversationResponse.info("预算优化功能正在开发中...");
    }
    
    private ConversationResponse handleUnknownIntent(String message, ConversationContext context) {
        return ConversationResponse.info("抱歉，我没有理解您的意图。请重新描述您的需求。");
    }
    
    /**
     * 生成下一个问题
     */
    private String generateNextQuestion(ConversationContext context) {
        String prompt = String.format("""
            基于当前已收集的营销活动参数，生成下一个问题来收集缺失的信息。
            
            已收集的参数: %s
            缺失的关键参数: %s
            
            请生成一个自然、友好的问题来询问缺失的信息。
            """, context.getParameters(), getMissingParameters(context));
        
        return chatClient.prompt().user(prompt).call().content();
    }
    
    /**
     * 检查是否有所需的参数
     */
    private boolean hasRequiredParameters(ConversationContext context) {
        String[] requiredParams = {"campaignName", "targetAudience", "budget", "duration"};
        for (String param : requiredParams) {
            if (!context.hasParameter(param)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 获取缺失的参数列表
     */
    private Map<String, String> getMissingParameters(ConversationContext context) {
        Map<String, String> missing = new HashMap<>();
        Map<String, String> requiredParams = Map.of(
            "campaignName", "活动名称",
            "targetAudience", "目标受众",
            "budget", "预算",
            "duration", "活动时长",
            "channels", "营销渠道",
            "objectives", "活动目标"
        );
        
        for (Map.Entry<String, String> entry : requiredParams.entrySet()) {
            if (!context.hasParameter(entry.getKey())) {
                missing.put(entry.getKey(), entry.getValue());
            }
        }
        
        return missing;
    }
    
    /**
     * 生成活动摘要
     */
    private String generateCampaignSummary(ConversationContext context) {
        String prompt = String.format("""
            基于以下参数生成营销活动摘要:
            %s
            
            请生成一个简洁、专业的活动摘要，突出关键信息。
            """, context.getParameters());
        
        return chatClient.prompt().user(prompt).call().content();
    }
    
    /**
     * 更新对话状态
     */
    private void updateConversationState(ConversationContext context, ConversationResponse response) {
        if (response.getType() == ConversationResponse.ResponseType.CONFIRMING_PARAMS) {
            context.setState(ConversationContext.ConversationState.CONFIRMING_PARAMS);
        } else if (response.getType() == ConversationResponse.ResponseType.GATHERING_INFO) {
            context.setState(ConversationContext.ConversationState.GATHERING_INFO);
        } else if (response.getType() == ConversationResponse.ResponseType.COMPLETED) {
            context.setState(ConversationContext.ConversationState.COMPLETED);
        }
    }
    
    /**
     * 获取或创建对话上下文
     */
    private ConversationContext getOrCreateContext(String userId) {
        return activeSessions.computeIfAbsent(userId, ConversationContext::new);
    }
    
    /**
     * 解析AI返回的意图分析结果
     */
    private UserIntentAnalysis parseIntentAnalysis(String aiResponse) {
        // 简化实现，实际应该使用JSON解析
        try {
            // 这里应该使用JSON解析器解析AI响应
            // 暂时返回默认值
            return new UserIntentAnalysis(
                ConversationContext.UserIntent.CREATE_CAMPAIGN,
                0.9,
                new HashMap<>(),
                true,
                "请告诉我您的活动名称"
            );
        } catch (Exception e) {
            return new UserIntentAnalysis(
                ConversationContext.UserIntent.UNKNOWN,
                0.0,
                new HashMap<>(),
                true,
                "请重新描述您的需求"
            );
        }
    }
    
    /**
     * 确认参数
     */
    public ConversationResponse confirmParameters(String userId, Map<String, Object> confirmedParams) {
        ConversationContext context = activeSessions.get(userId);
        if (context == null) {
            return ConversationResponse.error("未找到对话会话");
        }
        
        // 将确认的参数添加到已确认参数中
        confirmedParams.forEach(context::addConfirmedParameter);
        
        // 创建最终的营销活动
        context.setState(ConversationContext.ConversationState.CREATING_CAMPAIGN);
        
        return ConversationResponse.completed(
            "营销活动参数已确认！正在创建活动...",
            context.getConfirmedParameters(),
            "活动创建成功"
        );
    }
    
    /**
     * 获取会话状态
     */
    public ConversationContext getSessionStatus(String userId) {
        return activeSessions.get(userId);
    }
    
    /**
     * 结束会话
     */
    public void endSession(String userId) {
        activeSessions.remove(userId);
    }
}
