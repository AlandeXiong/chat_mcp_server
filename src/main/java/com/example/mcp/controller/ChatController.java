package com.example.mcp.controller;

import com.example.mcp.server.MarketingCampaignService;
import com.example.mcp.server.IntelligentConversationManager;
import com.example.mcp.server.ConversationContext;
import com.example.mcp.server.CampaignCreationService;
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
    
    @Autowired
    private CampaignCreationService campaignCreationService;
    
    @Autowired
    private McpClientService mcpClientService;

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
    
    @PostMapping("/create-campaign")
    @Operation(summary = "Create Campaign", description = "Create marketing campaign based on confirmed parameters and AI recommendations")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Campaign created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "500", description = "Creation error")
    })
    public ResponseEntity<Map<String, Object>> createCampaign(
        @Parameter(description = "Campaign creation request", required = true) @RequestBody CreateCampaignRequest request
    ) {
        try {
            var response = campaignCreationService.createCampaign(
                request.getUserId(), 
                request.getConfirmedParams(), 
                request.getAiRecommendations()
            );
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("campaign", response);
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
    
    @PostMapping("/activate-campaign/{campaignId}")
    @Operation(summary = "Activate Campaign", description = "Activate a marketing campaign")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Campaign activated successfully"),
        @ApiResponse(responseCode = "500", description = "Activation error")
    })
    public ResponseEntity<Map<String, Object>> activateCampaign(
        @Parameter(description = "Campaign ID", required = true) @PathVariable String campaignId
    ) {
        try {
            var response = campaignCreationService.activateCampaign(campaignId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
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
    
    @GetMapping("/campaign-status/{campaignId}")
    @Operation(summary = "Get Campaign Status", description = "Get marketing campaign status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Campaign not found")
    })
    public ResponseEntity<Map<String, Object>> getCampaignStatus(
        @Parameter(description = "Campaign ID", required = true) @PathVariable String campaignId
    ) {
        try {
            var response = campaignCreationService.getCampaignStatus(campaignId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
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
    
    // Campaign Creation DTO
    public static class CreateCampaignRequest {
        private String userId;
        private Map<String, Object> confirmedParams;
        private Map<String, Object> aiRecommendations;
        
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        
        public Map<String, Object> getConfirmedParams() { return confirmedParams; }
        public void setConfirmedParams(Map<String, Object> confirmedParams) { this.confirmedParams = confirmedParams; }
        
        public Map<String, Object> getAiRecommendations() { return aiRecommendations; }
        public void setAiRecommendations(Map<String, Object> aiRecommendations) { this.aiRecommendations = aiRecommendations; }
    }
    
    @PostMapping("/mcp-client/connect")
    @Operation(summary = "Connect MCP Client", description = "Initialize MCP client connection to marketing service")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "MCP client connected successfully"),
        @ApiResponse(responseCode = "500", description = "Connection failed")
    })
    public ResponseEntity<Map<String, Object>> connectMcpClient() {
        try {
            mcpClientService.initializeMcpClient();
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "MCP client connected successfully");
            result.put("connected", mcpClientService.isConnected());
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
    
    @GetMapping("/mcp-client/status")
    @Operation(summary = "Get MCP Client Status", description = "Get MCP client connection status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status retrieved successfully")
    })
    public ResponseEntity<Map<String, Object>> getMcpClientStatus() {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("connected", mcpClientService.isConnected());
        result.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/mcp-client/marketing-info")
    @Operation(summary = "Get Marketing Service Info", description = "Get marketing service information through MCP client")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Information retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Failed to get information")
    })
    public ResponseEntity<Map<String, Object>> getMarketingServiceInfo() {
        try {
            // Get marketing service info through AI recommendation generator
            Map<String, Object> marketingInfo = new HashMap<>();
            marketingInfo.put("success", true);
            marketingInfo.put("data", "Marketing service information will be available after MCP client integration");
            marketingInfo.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(marketingInfo);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    
    @PostMapping("/mcp-client/call-tool")
    @Operation(summary = "Call Marketing Service Tool", description = "Call a tool on marketing service through MCP client")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tool called successfully"),
        @ApiResponse(responseCode = "500", description = "Tool call failed")
    })
    public ResponseEntity<Map<String, Object>> callMarketingServiceTool(
        @Parameter(description = "Tool call request", required = true) @RequestBody ToolCallRequest request
    ) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "Tool call functionality will be available after MCP client integration");
            result.put("toolName", request.getToolName());
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
    
    // Tool Call DTO
    public static class ToolCallRequest {
        private String toolName;
        private Map<String, Object> arguments;
        
        public String getToolName() { return toolName; }
        public void setToolName(String toolName) { this.toolName = toolName; }
        
        public Map<String, Object> getArguments() { return arguments; }
        public void setArguments(Map<String, Object> arguments) { this.arguments = arguments; }
    }
}
