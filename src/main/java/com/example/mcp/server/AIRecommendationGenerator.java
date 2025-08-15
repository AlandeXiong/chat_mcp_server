package com.example.mcp.server;


import org.springframework.ai.azure.openai.AzureOpenAiChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.mcp.client.McpClientService;

/**
 * AI Recommendation Generator
 * Based on Model Context Protocol, generates creation recommendations for marketing campaign nodes
 */
@Service
public class AIRecommendationGenerator {
    
    @Autowired
    private AzureOpenAiChatModel chatModel;
    
    @Autowired
    private McpClientService mcpClientService;


    @Autowired
    private ChatClient chatClient;

    /**
     * Generate recommendations for target segment node
     */
    public Map<String, Object> generateSegmentRecommendations(String campaignType, String targetAudience, Double budget) {
        String prompt = String.format("""
            Based on the following marketing campaign information, generate detailed configuration recommendations for the target segment node:
            
            Campaign Type: %s
            Target Audience: %s
            Budget: $%.2f
            
            Please provide recommendations for the following aspects:
            1. Age group segmentation suggestions
            2. Geographic location targeting suggestions
            3. Occupation and interest tags
            4. Behavioral characteristic analysis
            5. Need insights
            6. Custom attribute suggestions
            
            Return recommendations in JSON format with specific configuration parameters and reasoning.
            """, campaignType, targetAudience, budget);
        
        String aiResponse = chatClient.prompt(prompt).call().content();
        return parseSegmentRecommendations(aiResponse);
    }
    
    /**
     * Generate recommendations for delivery strategy node
     */
    public Map<String, Object> generateStrategyRecommendations(String campaignType, String targetAudience, Double budget, Map<String, Object> segmentConfig) {
        String prompt = String.format("""
            Based on the following information, generate detailed configuration recommendations for the delivery strategy node:
            
            Campaign Type: %s
            Target Audience: %s
            Budget: $%.2f
            Segment Configuration: %s
            
            Please provide recommendations for the following aspects:
            1. Delivery channel selection (Email, SMS, Social Media, etc.)
            2. Delivery frequency suggestions
            3. Budget allocation strategy
            4. Delivery timing optimization
            5. Channel-specific settings
            6. Optimization goal suggestions
            
            Return recommendations in JSON format with specific configuration parameters and strategy reasoning.
            """, campaignType, targetAudience, budget, segmentConfig);

        String aiResponse = chatClient.prompt(prompt).call().content();
        return parseStrategyRecommendations(aiResponse);
    }
    
    /**
     * Generate recommendations for email template node
     */
    public Map<String, Object> generateEmailTemplateRecommendations(String campaignType, String targetAudience, Map<String, Object> segmentConfig, Map<String, Object> strategyConfig) {
        String prompt = String.format("""
            Based on the following information, generate detailed configuration recommendations for the email template node:
            
            Campaign Type: %s
            Target Audience: %s
            Segment Configuration: %s
            Delivery Strategy: %s
            
            Please provide recommendations for the following aspects:
            1. Email subject optimization suggestions
            2. Email body content suggestions
            3. Call to action (CTA) suggestions
            4. Personalization field suggestions
            5. Template type selection
            6. Sender information suggestions
            
            Return recommendations in JSON format with specific configuration parameters and content suggestions.
            """, campaignType, targetAudience, segmentConfig, strategyConfig);

        String aiResponse = chatClient.prompt(prompt).call().content();
        return parseEmailTemplateRecommendations(aiResponse);
    }
    
    /**
     * Generate recommendations for condition judgment node
     */
    public Map<String, Object> generateConditionRecommendations(String campaignType, Map<String, Object> segmentConfig, Map<String, Object> strategyConfig) {
        String prompt = String.format("""
            Based on the following information, generate detailed configuration recommendations for the condition judgment node:
            
            Campaign Type: %s
            Segment Configuration: %s
            Delivery Strategy: %s
            
            Please provide recommendations for the following aspects:
            1. Condition type selection suggestions
            2. Flow path design suggestions
            3. Condition logic suggestions
            4. Target node connection suggestions
            5. Condition name and description suggestions
            
            Return recommendations in JSON format with specific configuration parameters and logic design.
            """, campaignType, segmentConfig, strategyConfig);

        String aiResponse = chatClient.prompt(prompt).call().content();
        return parseConditionRecommendations(aiResponse);
    }
    
    /**
     * Generate recommendations for customer journey node
     */
    public Map<String, Object> generateCustomerJourneyRecommendations(String campaignType, String targetAudience, Map<String, Object> segmentConfig) {
        String prompt = String.format("""
            Based on the following information, generate detailed configuration recommendations for the customer journey node:
            
            Campaign Type: %s
            Target Audience: %s
            Segment Configuration: %s
            
            Please provide recommendations for the following aspects:
            1. Customer journey stage design
            2. Touchpoint selection suggestions
            3. Journey duration suggestions
            4. Journey goal setting
            5. Journey map design
            6. Conversion path optimization
            
            Return recommendations in JSON format with specific configuration parameters and journey design.
            """, campaignType, targetAudience, segmentConfig);

        String aiResponse = chatClient.prompt(prompt).call().content();
        return parseCustomerJourneyRecommendations(aiResponse);
    }
    
    /**
     * Generate complete marketing campaign recommendations
     */
    public Map<String, Object> generateCompleteCampaignRecommendations(String campaignType, String targetAudience, Double budget, String duration) {
        String prompt = String.format("""
            Based on the following information, generate complete marketing campaign recommendations:
            
            Campaign Type: %s
            Target Audience: %s
            Budget: $%.2f
            Campaign Duration: %s
            
            Please provide comprehensive recommendations for the entire marketing campaign, including:
            1. Overall campaign strategy suggestions
            2. Target segment selection suggestions
            3. Delivery strategy suggestions
            4. Content creative suggestions
            5. Execution plan suggestions
            6. Expected results assessment
            7. Risk control suggestions
            
            Return complete recommendations in JSON format with configuration parameters and strategy descriptions for all nodes.
            """, campaignType, targetAudience, budget, duration);

        String aiResponse = chatClient.prompt(prompt).call().content();
        return parseCompleteCampaignRecommendations(aiResponse);
    }
    
    /**
     * Get marketing service information through MCP client
     */
    public Map<String, Object> getMarketingServiceInfo() {
        try {
            Map<String, Object> serviceInfo = new HashMap<>();
            
            // Get available tools from marketing service
            if (mcpClientService.isConnected()) {
                List<Map<String, Object>> tools = mcpClientService.getMarketingTools();
                serviceInfo.put("availableTools", tools);
                
                // Get marketing service schemas
                Map<String, Object> schemas = mcpClientService.getMarketingSchemas();
                serviceInfo.put("schemas", schemas);
                
                // Get marketing resources
                List<Map<String, Object>> resources = mcpClientService.getMarketingResources("campaign");
                serviceInfo.put("campaignResources", resources);
                
                serviceInfo.put("status", "connected");
                serviceInfo.put("message", "Successfully retrieved marketing service information");
            } else {
                serviceInfo.put("status", "disconnected");
                serviceInfo.put("message", "MCP client not connected to marketing service");
            }
            
            return serviceInfo;
            
        } catch (Exception e) {
            Map<String, Object> errorInfo = new HashMap<>();
            errorInfo.put("status", "error");
            errorInfo.put("error", e.getMessage());
            errorInfo.put("message", "Failed to get marketing service information");
            return errorInfo;
        }
    }
    
    /**
     * Call marketing service tool through MCP client
     */
    public Map<String, Object> callMarketingServiceTool(String toolName, Map<String, Object> arguments) {
        try {
            if (!mcpClientService.isConnected()) {
                throw new IllegalStateException("MCP client not connected to marketing service");
            }
            
            return mcpClientService.callMarketingTool(toolName, arguments);
            
        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("error", e.getMessage());
            errorResult.put("toolName", toolName);
            return errorResult;
        }
    }
    
    // Helper methods for parsing AI responses
    private Map<String, Object> parseSegmentRecommendations(String aiResponse) {
        // Simplified implementation, should use JSON parser in production
        Map<String, Object> recommendations = new HashMap<>();
        recommendations.put("ageGroup", "25-35 years");
        recommendations.put("location", "Tier 1 cities");
        recommendations.put("occupation", "Young professionals");
        recommendations.put("needs", "Career development, quality of life improvement");
        recommendations.put("interests", "Technology, fashion, travel");
        recommendations.put("behavior", "Online shopping, social media active");
        recommendations.put("reasoning", aiResponse);
        return recommendations;
    }
    
    private Map<String, Object> parseStrategyRecommendations(String aiResponse) {
        Map<String, Object> recommendations = new HashMap<>();
        recommendations.put("channels", List.of("email", "social", "sms"));
        recommendations.put("frequency", 3);
        recommendations.put("budgetAllocation", Map.of("email", 40, "social", 40, "sms", 20));
        recommendations.put("timing", "Weekdays 9-11 AM, Weekends 2-4 PM");
        recommendations.put("optimizationGoal", "Conversion rate optimization");
        recommendations.put("reasoning", aiResponse);
        return recommendations;
    }
    
    private Map<String, Object> parseEmailTemplateRecommendations(String aiResponse) {
        Map<String, Object> recommendations = new HashMap<>();
        recommendations.put("subject", "Personalized subject suggestions");
        recommendations.put("body", "Content suggestions based on audience characteristics");
        recommendations.put("cta", "Clear call to action");
        recommendations.put("personalization", List.of("Name", "Company", "Position"));
        recommendations.put("reasoning", aiResponse);
        return recommendations;
    }
    
    private Map<String, Object> parseConditionRecommendations(String aiResponse) {
        Map<String, Object> recommendations = new HashMap<>();
        recommendations.put("conditionType", "user_segment");
        recommendations.put("flowPaths", List.of(
            Map.of("name", "Yes", "condition", "true", "targetType", "strategy"),
            Map.of("name", "No", "condition", "false", "targetType", "emailTemplate")
        ));
        recommendations.put("reasoning", aiResponse);
        return recommendations;
    }
    
    private Map<String, Object> parseCustomerJourneyRecommendations(String aiResponse) {
        Map<String, Object> recommendations = new HashMap<>();
        recommendations.put("journeyStages", List.of("Awareness", "Consideration", "Decision", "Purchase", "Loyalty"));
        recommendations.put("touchpoints", List.of("Social Media", "Email", "Website", "Customer Service"));
        recommendations.put("duration", "3-6 months");
        recommendations.put("goal", "Improve brand awareness and conversion rate");
        recommendations.put("reasoning", aiResponse);
        return recommendations;
    }
    
    private Map<String, Object> parseCompleteCampaignRecommendations(String aiResponse) {
        Map<String, Object> recommendations = new HashMap<>();
        recommendations.put("overallStrategy", "Comprehensive marketing strategy suggestions");
        recommendations.put("segmentRecommendations", parseSegmentRecommendations(aiResponse));
        recommendations.put("strategyRecommendations", parseStrategyRecommendations(aiResponse));
        recommendations.put("contentRecommendations", parseEmailTemplateRecommendations(aiResponse));
        recommendations.put("executionPlan", "Detailed execution plan");
        recommendations.put("expectedResults", "Expected results assessment");
        recommendations.put("riskControl", "Risk control suggestions");
        recommendations.put("reasoning", aiResponse);
        return recommendations;
    }
}
