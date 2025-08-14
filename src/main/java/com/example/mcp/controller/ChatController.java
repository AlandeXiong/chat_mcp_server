package com.example.mcp.controller;

import com.example.mcp.server.MarketingCampaignService;
import com.example.mcp.server.IntelligentConversationManager;
import com.example.mcp.server.ConversationContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@Tag(name = "AI Chat API", description = "AI-powered chat interface for marketing campaigns")
@CrossOrigin(origins = "*") // Allow ReactFlow frontend to connect
public class ChatController {

    @Autowired
    private MarketingCampaignService marketingCampaignService;
    
    @Autowired
    private IntelligentConversationManager conversationManager;

    @PostMapping("/send")
    @Operation(summary = "Send Chat Message", description = "Send a message to AI and get response")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Message sent successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid message"),
        @ApiResponse(responseCode = "500", description = "AI service error")
    })
    public ResponseEntity<Map<String, Object>> sendMessage(
        @Parameter(description = "Chat message", required = true) @RequestBody ChatRequest request
    ) {
        try {
            String aiResponse = marketingCampaignService.processChatMessage(request.getMessage());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", request.getMessage());
            response.put("aiResponse", aiResponse);
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @PostMapping("/campaign-advice")
    @Operation(summary = "Get Campaign Advice", description = "Get AI-powered marketing campaign advice")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Advice generated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "500", description = "AI service error")
    })
    public ResponseEntity<Map<String, Object>> getCampaignAdvice(
        @Parameter(description = "Campaign request", required = true) @RequestBody CampaignAdviceRequest request
    ) {
        try {
            String advice = marketingCampaignService.generateCampaignAdvice(
                request.getCampaignType(), 
                request.getTargetAudience(), 
                request.getBudget()
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("campaignType", request.getCampaignType());
            response.put("targetAudience", request.getTargetAudience());
            response.put("budget", request.getBudget());
            response.put("advice", advice);
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @GetMapping("/status")
    @Operation(summary = "Chat Service Status", description = "Check if the AI chat service is available")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Service is available")
    })
    public ResponseEntity<Map<String, Object>> getChatStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "available");
        response.put("service", "AI Chat Service");
        response.put("aiModels", new String[]{"Azure OpenAI"});
        response.put("features", new String[]{"Chat", "Campaign Advice", "Marketing Strategy", "Intelligent Conversation"});
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }

    // Request DTOs
    public static class ChatRequest {
        private String message;
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    public static class CampaignAdviceRequest {
        private String campaignType;
        private String targetAudience;
        private Double budget;
        
        public String getCampaignType() { return campaignType; }
        public void setCampaignType(String campaignType) { this.campaignType = campaignType; }
        
        public String getTargetAudience() { return targetAudience; }
        public void setTargetAudience(String targetAudience) { this.targetAudience = targetAudience; }
        
        public Double getBudget() { return budget; }
        public void setBudget(Double budget) { this.budget = budget; }
    }
    
    // Intelligent Conversation DTOs
    public static class IntelligentChatRequest {
        private String userId;
        private String message;
        
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
    
    public static class ConfirmParametersRequest {
        private String userId;
        private Map<String, Object> parameters;
        
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        
        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
    }
    
    @PostMapping("/intelligent-chat")
    @Operation(summary = "Intelligent Chat", description = "AI-powered intelligent conversation for campaign creation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Message processed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "500", description = "AI service error")
    })
    public ResponseEntity<Map<String, Object>> intelligentChat(
        @Parameter(description = "Intelligent chat request", required = true) @RequestBody IntelligentChatRequest request
    ) {
        try {
            var response = conversationManager.processMessage(request.getUserId(), request.getMessage());
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("response", response);
            result.put("sessionId", conversationManager.getSessionStatus(request.getUserId()).getSessionId());
            result.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    
    @PostMapping("/confirm-parameters")
    @Operation(summary = "Confirm Parameters", description = "Confirm campaign parameters collected during conversation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Parameters confirmed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "500", description = "Service error")
    })
    public ResponseEntity<Map<String, Object>> confirmParameters(
        @Parameter(description = "Parameter confirmation request", required = true) @RequestBody ConfirmParametersRequest request
    ) {
        try {
            var response = conversationManager.confirmParameters(request.getUserId(), request.getParameters());
            
            Map<String, Object> result = new HashMap<>();
            result.put("result", response);
            result.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    
    @GetMapping("/session-status/{userId}")
    @Operation(summary = "Get Session Status", description = "Get current conversation session status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Session status retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Session not found")
    })
    public ResponseEntity<Map<String, Object>> getSessionStatus(
        @Parameter(description = "User ID", required = true) @PathVariable String userId
    ) {
        ConversationContext context = conversationManager.getSessionStatus(userId);
        if (context == null) {
            return ResponseEntity.notFound().build();
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("sessionId", context.getSessionId());
        result.put("state", context.getState());
        result.put("intent", context.getIntent());
        result.put("turnCount", context.getTurnCount());
        result.put("parameters", context.getParameters());
        result.put("confirmedParameters", context.getConfirmedParameters());
        result.put("currentQuestion", context.getCurrentQuestion());
        result.put("startTime", context.getStartTime());
        result.put("lastUpdateTime", context.getLastUpdateTime());
        
        return ResponseEntity.ok(result);
    }
    
    @DeleteMapping("/end-session/{userId}")
    @Operation(summary = "End Session", description = "End conversation session for user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Session ended successfully")
    })
    public ResponseEntity<Map<String, Object>> endSession(
        @Parameter(description = "User ID", required = true) @PathVariable String userId
    ) {
        conversationManager.endSession(userId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Session ended successfully");
        result.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(result);
    }
}
