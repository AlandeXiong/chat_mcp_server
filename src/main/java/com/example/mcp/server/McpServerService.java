package com.example.mcp.server;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * MCP Server Service
 * Core service for handling MCP server operations and marketing campaign assistance
 */
@Service
public class McpServerService {

    private final ChatClient chatClient;

    public McpServerService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    /**
     * Process user chat message and provide AI response
     * 
     * @param userMessage User's input message
     * @return AI response with marketing advice
     */
    public String processChatMessage(String userMessage) {
        try {
            String response = this.chatClient.prompt()
                .user(userMessage)
                .call()
                .content();
            return response;
        } catch (Exception e) {
            return "Error processing message: " + e.getMessage();
        }
    }

    /**
     * Generate marketing campaign advice based on parameters
     * 
     * @param industry Target industry
     * @param targetAudience Target audience description
     * @param budget Campaign budget
     * @return Structured marketing campaign advice
     */
    public String generateCampaignAdvice(String industry, String targetAudience, double budget) {
        try {
            String prompt = String.format("""
                Create a marketing campaign for:
                Industry: %s
                Target Audience: %s
                Budget: $%.2f
                
                Please provide:
                1. Campaign strategy overview
                2. Target audience analysis
                3. Channel recommendations
                4. Content suggestions
                5. Timeline and milestones
                6. Success metrics
                """, industry, targetAudience, budget);

            String response = this.chatClient.prompt()
                .user(prompt)
                .call()
                .content();
            return response;
        } catch (Exception e) {
            return "Error generating campaign advice: " + e.getMessage();
        }
    }

    /**
     * Get available marketing tools and capabilities
     * 
     * @return Map of available tools and their descriptions
     */
    public Map<String, String> getAvailableTools() {
        Map<String, String> tools = new HashMap<>();
        tools.put("campaign_creation", "Create comprehensive marketing campaigns");
        tools.put("audience_segmentation", "Define and analyze target audience segments");
        tools.put("channel_strategy", "Develop multi-channel marketing strategies");
        tools.put("content_generation", "Generate marketing content and templates");
        tools.put("budget_planning", "Plan and allocate campaign budgets");
        tools.put("performance_metrics", "Define KPIs and success metrics");
        return tools;
    }

    /**
     * Validate MCP server health and functionality
     * 
     * @return Server health status
     */
    public Map<String, Object> getServerHealth() {
        Map<String, Object> health = new HashMap<>();
        try {
            // Test basic AI functionality
            String testResponse = this.chatClient.prompt()
                .user("Hello, are you working?")
                .call()
                .content();
            health.put("status", "healthy");
            health.put("ai_functionality", "working");
            health.put("test_response", testResponse);
        } catch (Exception e) {
            health.put("status", "unhealthy");
            health.put("error", e.getMessage());
        }
        return health;
    }
}
