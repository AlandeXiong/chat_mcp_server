package com.example.mcp.controller;

import com.example.mcp.client.MCPClientService;
import com.example.mcp.server.MarketingCampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.List;

/**
 * REST Controller for marketing campaign operations and MCP integration
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class MarketingCampaignController {
    
    @Autowired
    private MarketingCampaignService marketingService;
    
    @Autowired
    private MCPClientService mcpClientService;
    
    // Marketing Campaign Endpoints
    
    /**
     * Create a marketing campaign
     */
    @PostMapping("/marketing/campaign")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> createCampaign(
            @RequestBody Map<String, String> request) {
        
        String userRequest = request.get("request");
        if (userRequest == null || userRequest.trim().isEmpty()) {
            return CompletableFuture.completedFuture(
                ResponseEntity.badRequest().body(Map.of("error", "Request text is required"))
            );
        }
        
        return marketingService.createCampaign(userRequest)
            .thenApply(ResponseEntity::ok);
    }
    
    /**
     * Define audience segments
     */
    @PostMapping("/marketing/audience")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> defineAudience(
            @RequestBody Map<String, String> request) {
        
        String userRequest = request.get("request");
        if (userRequest == null || userRequest.trim().isEmpty()) {
            return CompletableFuture.completedFuture(
                ResponseEntity.badRequest().body(Map.of("error", "Request text is required"))
            );
        }
        
        return marketingService.defineAudienceSegments(userRequest)
            .thenApply(ResponseEntity::ok);
    }
    
    /**
     * Develop channel strategy
     */
    @PostMapping("/marketing/strategy")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> developStrategy(
            @RequestBody Map<String, String> request) {
        
        String userRequest = request.get("request");
        if (userRequest == null || userRequest.trim().isEmpty()) {
            return CompletableFuture.completedFuture(
                ResponseEntity.badRequest().body(Map.of("error", "Request text is required"))
            );
        }
        
        return marketingService.developChannelStrategy(userRequest)
            .thenApply(ResponseEntity::ok);
    }
    
    /**
     * Generate email template
     */
    @PostMapping("/marketing/template")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> generateTemplate(
            @RequestBody Map<String, String> request) {
        
        String userRequest = request.get("request");
        if (userRequest == null || userRequest.trim().isEmpty()) {
            return CompletableFuture.completedFuture(
                ResponseEntity.badRequest().body(Map.of("error", "Request text is required"))
            );
        }
        
        return marketingService.generateEmailTemplate(userRequest)
            .thenApply(ResponseEntity::ok);
    }
    
    /**
     * Analyze campaign performance
     */
    @PostMapping("/marketing/performance")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> analyzePerformance(
            @RequestBody Map<String, String> request) {
        
        String userRequest = request.get("request");
        if (userRequest == null || userRequest.trim().isEmpty()) {
            return CompletableFuture.completedFuture(
                ResponseEntity.badRequest().body(Map.of("error", "Request text is required"))
            );
        }
        
        return marketingService.analyzeCampaignPerformance(userRequest)
            .thenApply(ResponseEntity::ok);
    }
    
    /**
     * Optimize budget allocation
     */
    @PostMapping("/marketing/optimization")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> optimizeBudget(
            @RequestBody Map<String, String> request) {
        
        String userRequest = request.get("request");
        if (userRequest == null || userRequest.trim().isEmpty()) {
            return CompletableFuture.completedFuture(
                ResponseEntity.badRequest().body(Map.of("error", "Request text is required"))
            );
        }
        
        return marketingService.optimizeBudgetAllocation(userRequest)
            .thenApply(ResponseEntity::ok);
    }
    
    // MCP Integration Endpoints
    
    /**
     * Get MCP server information
     */
    @GetMapping("/mcp/server/info")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getMCPServerInfo() {
        return mcpClientService.getServerInfo()
            .thenApply(ResponseEntity::ok);
    }
    
    /**
     * Check MCP server health
     */
    @GetMapping("/mcp/server/health")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> checkMCPHealth() {
        return mcpClientService.checkHealth()
            .thenApply(ResponseEntity::ok);
    }
    
    /**
     * Get MCP client information
     */
    @GetMapping("/mcp/clients")
    public ResponseEntity<Map<String, Object>> getMCPClients() {
        return ResponseEntity.ok(Map.of(
            "sync_clients", mcpClientService.getSyncClients().size(),
            "async_clients", mcpClientService.getAsyncClients().size(),
            "tool_callbacks", mcpClientService.getToolCallbacks(),
            "timestamp", System.currentTimeMillis()
        ));
    }
    
    /**
     * Send request via MCP client
     */
    @PostMapping("/mcp/request")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> sendMCPRequest(
            @RequestBody Map<String, String> request) {
        
        String userRequest = request.get("request");
        if (userRequest == null || userRequest.trim().isEmpty()) {
            return CompletableFuture.completedFuture(
                ResponseEntity.badRequest().body(Map.of("error", "Request text is required"))
            );
        }
        
        return mcpClientService.sendRequest(userRequest)
            .thenApply(response -> ResponseEntity.ok(Map.of(
                "response", response,
                "timestamp", System.currentTimeMillis()
            )));
    }
    
    // General Endpoints
    
    /**
     * Get available capabilities
     */
    @GetMapping("/capabilities")
    public ResponseEntity<Map<String, Object>> getCapabilities() {
        return ResponseEntity.ok(Map.of(
            "marketing", Map.of(
                "campaign", "Create marketing campaigns",
                "audience", "Define target audiences",
                "strategy", "Develop channel strategies",
                "template", "Generate email templates",
                "performance", "Analyze campaign performance",
                "optimization", "Optimize budget allocation"
            ),
            "mcp", Map.of(
                "server", "MCP Server integration",
                "client", "MCP Client functionality",
                "tools", "MCP Tool callbacks"
            ),
            "endpoints", Map.of(
                "POST /api/marketing/*", "Marketing operations",
                "GET /api/mcp/*", "MCP operations",
                "GET /api/capabilities", "Get capabilities",
                "WebSocket", "ws://localhost:8080/chat"
            ),
            "websocket", "ws://localhost:8080/chat",
            "mcp_commands", List.of(
                "/mcp/status - Check MCP server status",
                "/mcp/tools - Get available MCP tools",
                "/mcp/health - Check MCP health"
            ),
            "status", "ready"
        ));
    }
} 