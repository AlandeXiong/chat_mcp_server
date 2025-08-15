package com.example.mcp.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Marketing Campaign Creation Service
 * Creates complete marketing campaigns based on user confirmed parameters and AI recommendations
 */
@Service
public class CampaignCreationService {
    
    @Autowired
    private AIRecommendationGenerator recommendationGenerator;
    
    /**
     * Create complete marketing campaign
     */
    public Map<String, Object> createCampaign(String userId, Map<String, Object> confirmedParams, Map<String, Object> aiRecommendations) {
        try {
            // Generate campaign ID
            String campaignId = UUID.randomUUID().toString();
            
            // Create campaign basic information
            Map<String, Object> campaign = new HashMap<>();
            campaign.put("id", campaignId);
            campaign.put("userId", userId);
            campaign.put("name", confirmedParams.get("campaignName"));
            campaign.put("type", confirmedParams.get("campaignType"));
            campaign.put("targetAudience", confirmedParams.get("targetAudience"));
            campaign.put("budget", confirmedParams.get("budget"));
            campaign.put("duration", confirmedParams.get("duration"));
            campaign.put("status", "DRAFT");
            campaign.put("createdAt", LocalDateTime.now());
            campaign.put("updatedAt", LocalDateTime.now());
            
            // Create campaign nodes
            List<CampaignNode> nodes = createCampaignNodes(campaignId, confirmedParams, aiRecommendations);
            campaign.put("nodes", nodes);
            
            // Create campaign flow
            List<Map<String, Object>> connections = createCampaignConnections(nodes);
            campaign.put("connections", connections);
            
            // Set campaign status
            campaign.put("status", "READY");
            campaign.put("updatedAt", LocalDateTime.now());
            
            return campaign;
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to create marketing campaign: " + e.getMessage());
            error.put("timestamp", LocalDateTime.now());
            return error;
        }
    }
    
    /**
     * Create campaign nodes
     */
    private List<CampaignNode> createCampaignNodes(String campaignId, Map<String, Object> confirmedParams, Map<String, Object> aiRecommendations) {
        List<CampaignNode> nodes = new ArrayList<>();
        
        // Create start node
        CampaignNode startNode = new CampaignNode(
            campaignId + "_start", 
            CampaignNode.NodeType.START, 
            "Start"
        );
        startNode.setStatus(CampaignNode.NodeStatus.READY);
        nodes.add(startNode);
        
        // Create target segment node
        if (aiRecommendations.containsKey("segment")) {
            CampaignNode segmentNode = createSegmentNode(campaignId, aiRecommendations.get("segment"));
            nodes.add(segmentNode);
        }
        
        // Create delivery strategy node
        if (aiRecommendations.containsKey("strategy")) {
            CampaignNode strategyNode = createStrategyNode(campaignId, aiRecommendations.get("strategy"));
            nodes.add(strategyNode);
        }
        
        // Create email template node
        if (aiRecommendations.containsKey("emailTemplate")) {
            CampaignNode emailNode = createEmailTemplateNode(campaignId, aiRecommendations.get("emailTemplate"));
            nodes.add(emailNode);
        }
        
        // Create condition judgment node
        if (aiRecommendations.containsKey("condition")) {
            CampaignNode conditionNode = createConditionNode(campaignId, aiRecommendations.get("condition"));
            nodes.add(conditionNode);
        }
        
        // Create customer journey node
        if (aiRecommendations.containsKey("customerJourney")) {
            CampaignNode journeyNode = createCustomerJourneyNode(campaignId, aiRecommendations.get("customerJourney"));
            nodes.add(journeyNode);
        }
        
        // Create end node
        CampaignNode endNode = new CampaignNode(
            campaignId + "_end", 
            CampaignNode.NodeType.END, 
            "End"
        );
        endNode.setStatus(CampaignNode.NodeStatus.READY);
        nodes.add(endNode);
        
        return nodes;
    }
    
    /**
     * Create target segment node
     */
    private CampaignNode createSegmentNode(String campaignId, Object segmentRecommendations) {
        CampaignNode node = new CampaignNode(
            campaignId + "_segment",
            CampaignNode.NodeType.SEGMENT,
            "Target Segment"
        );
        
        Map<String, Object> data = new HashMap<>();
        if (segmentRecommendations instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> recommendations = (Map<String, Object>) segmentRecommendations;
            data.putAll(recommendations);
        }
        
        node.setData(data);
        node.setStatus(CampaignNode.NodeStatus.READY);
        return node;
    }
    
    /**
     * Create delivery strategy node
     */
    private CampaignNode createStrategyNode(String campaignId, Object strategyRecommendations) {
        CampaignNode node = new CampaignNode(
            campaignId + "_strategy",
            CampaignNode.NodeType.STRATEGY,
            "Delivery Strategy"
        );
        
        Map<String, Object> data = new HashMap<>();
        if (strategyRecommendations instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> recommendations = (Map<String, Object>) strategyRecommendations;
            data.putAll(recommendations);
        }
        
        node.setData(data);
        node.setStatus(CampaignNode.NodeStatus.READY);
        return node;
    }
    
    /**
     * Create email template node
     */
    private CampaignNode createEmailTemplateNode(String campaignId, Object emailRecommendations) {
        CampaignNode node = new CampaignNode(
            campaignId + "_email",
            CampaignNode.NodeType.EMAIL_TEMPLATE,
            "Email Template"
        );
        
        Map<String, Object> data = new HashMap<>();
        if (emailRecommendations instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> recommendations = (Map<String, Object>) emailRecommendations;
            data.putAll(recommendations);
        }
        
        node.setData(data);
        node.setStatus(CampaignNode.NodeStatus.READY);
        return node;
    }
    
    /**
     * Create condition judgment node
     */
    private CampaignNode createConditionNode(String campaignId, Object conditionRecommendations) {
        CampaignNode node = new CampaignNode(
            campaignId + "_condition",
            CampaignNode.NodeType.CONDITION,
            "Condition Judgment"
        );
        
        Map<String, Object> data = new HashMap<>();
        if (conditionRecommendations instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> recommendations = (Map<String, Object>) conditionRecommendations;
            data.putAll(recommendations);
        }
        
        node.setData(data);
        node.setStatus(CampaignNode.NodeStatus.READY);
        return node;
    }
    
    /**
     * Create customer journey node
     */
    private CampaignNode createCustomerJourneyNode(String campaignId, Object journeyRecommendations) {
        CampaignNode node = new CampaignNode(
            campaignId + "_journey",
            CampaignNode.NodeType.CUSTOMER_JOURNEY,
            "Customer Journey"
        );
        
        Map<String, Object> data = new HashMap<>();
        if (journeyRecommendations instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> recommendations = (Map<String, Object>) journeyRecommendations;
            data.putAll(recommendations);
        }
        
        node.setData(data);
        node.setStatus(CampaignNode.NodeStatus.READY);
        return node;
    }
    
    /**
     * Create campaign connections
     */
    private List<Map<String, Object>> createCampaignConnections(List<CampaignNode> nodes) {
        List<Map<String, Object>> connections = new ArrayList<>();
        
        // Simplified connection logic, should be based on AI-suggested flow paths
        for (int i = 0; i < nodes.size() - 1; i++) {
            Map<String, Object> connection = new HashMap<>();
            connection.put("id", "conn_" + i);
            connection.put("source", nodes.get(i).getId());
            connection.put("target", nodes.get(i + 1).getId());
            connection.put("type", "default");
            connections.add(connection);
        }
        
        return connections;
    }
    
    /**
     * Activate marketing campaign
     */
    public Map<String, Object> activateCampaign(String campaignId) {
        Map<String, Object> result = new HashMap<>();
        result.put("campaignId", campaignId);
        result.put("status", "ACTIVE");
        result.put("activatedAt", LocalDateTime.now());
        result.put("message", "Marketing campaign activated successfully");
        return result;
    }
    
    /**
     * Pause marketing campaign
     */
    public Map<String, Object> pauseCampaign(String campaignId) {
        Map<String, Object> result = new HashMap<>();
        result.put("campaignId", campaignId);
        result.put("status", "PAUSED");
        result.put("pausedAt", LocalDateTime.now());
        result.put("message", "Marketing campaign paused");
        return result;
    }
    
    /**
     * Get campaign status
     */
    public Map<String, Object> getCampaignStatus(String campaignId) {
        Map<String, Object> result = new HashMap<>();
        result.put("campaignId", campaignId);
        result.put("status", "READY");
        result.put("lastUpdated", LocalDateTime.now());
        result.put("message", "Campaign status is normal");
        return result;
    }
}
