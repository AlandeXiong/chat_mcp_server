package com.example.mcp.server;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Service interface for marketing campaign operations
 */
public interface MarketingCampaignService {
    
    /**
     * Create a marketing campaign based on user request
     * @param request User's natural language request
     * @return Campaign information
     */
    CompletableFuture<Map<String, Object>> createCampaign(String request);
    
    /**
     * Define target audience segments
     * @param request User's audience definition request
     * @return Audience segment information
     */
    CompletableFuture<Map<String, Object>> defineAudienceSegments(String request);
    
    /**
     * Develop marketing channel strategy
     * @param request User's strategy request
     * @return Channel strategy information
     */
    CompletableFuture<Map<String, Object>> developChannelStrategy(String request);
    
    /**
     * Generate email templates
     * @param request User's template request
     * @return Email template information
     */
    CompletableFuture<Map<String, Object>> generateEmailTemplate(String request);
    
    /**
     * Analyze campaign performance
     * @param request User's analysis request
     * @return Performance analysis
     */
    CompletableFuture<Map<String, Object>> analyzeCampaignPerformance(String request);
    
    /**
     * Optimize campaign budget allocation
     * @param request User's optimization request
     * @return Budget optimization recommendations
     */
    CompletableFuture<Map<String, Object>> optimizeBudgetAllocation(String request);
    
    /**
     * Process chat message and get AI response
     * @param message User's chat message
     * @return AI response
     */
    String processChatMessage(String message);
    
    /**
     * Generate AI-powered campaign advice
     * @param campaignType Type of campaign
     * @param targetAudience Target audience description
     * @param budget Campaign budget
     * @return AI-generated advice
     */
    String generateCampaignAdvice(String campaignType, String targetAudience, Double budget);
} 