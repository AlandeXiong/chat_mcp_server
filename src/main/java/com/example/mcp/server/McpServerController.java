package com.example.mcp.server;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * MCP Server Controller
 * REST endpoints for testing MCP server functionality independently
 */
@RestController
@RequestMapping("/api/mcp-server")
@Tag(name = "MCP Server API", description = "MCP Server testing and management endpoints")
@CrossOrigin(origins = "*")
public class McpServerController {

    @Autowired
    private McpServerService mcpServerService;

    /**
     * Test basic chat functionality
     */
    @PostMapping("/chat")
    @Operation(summary = "Test Chat", description = "Test basic AI chat functionality")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Chat response generated successfully"),
        @ApiResponse(responseCode = "500", description = "Chat processing error")
    })
    public ResponseEntity<Map<String, Object>> testChat(
        @Parameter(description = "User message", required = true) @RequestBody Map<String, String> request
    ) {
        try {
            String userMessage = request.get("message");
            String aiResponse = mcpServerService.processChatMessage(userMessage);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "user_message", userMessage,
                "ai_response", aiResponse,
                "timestamp", System.currentTimeMillis()
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "error", e.getMessage(),
                "timestamp", System.currentTimeMillis()
            );
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Test campaign advice generation
     */
    @PostMapping("/campaign-advice")
    @Operation(summary = "Generate Campaign Advice", description = "Test AI campaign advice generation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Campaign advice generated successfully"),
        @ApiResponse(responseCode = "500", description = "Advice generation error")
    })
    public ResponseEntity<Map<String, Object>> generateCampaignAdvice(
        @Parameter(description = "Campaign parameters", required = true) @RequestBody Map<String, Object> request
    ) {
        try {
            String industry = (String) request.get("industry");
            String targetAudience = (String) request.get("targetAudience");
            Double budget = Double.valueOf(request.get("budget").toString());
            
            String advice = mcpServerService.generateCampaignAdvice(industry, targetAudience, budget);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "industry", industry,
                "targetAudience", targetAudience,
                "budget", budget,
                "advice", advice,
                "timestamp", System.currentTimeMillis()
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "error", e.getMessage(),
                "timestamp", System.currentTimeMillis()
            );
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Get available marketing tools
     */
    @GetMapping("/tools")
    @Operation(summary = "Get Available Tools", description = "Get list of available marketing tools")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tools retrieved successfully")
    })
    public ResponseEntity<Map<String, Object>> getAvailableTools() {
        Map<String, String> tools = mcpServerService.getAvailableTools();
        
        Map<String, Object> response = Map.of(
            "success", true,
            "tools", tools,
            "count", tools.size(),
            "timestamp", System.currentTimeMillis()
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * Check server health
     */
    @GetMapping("/health")
    @Operation(summary = "Server Health Check", description = "Check MCP server health and functionality")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Health status retrieved successfully")
    })
    public ResponseEntity<Map<String, Object>> getServerHealth() {
        Map<String, Object> health = mcpServerService.getServerHealth();
        health.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(health);
    }

    /**
     * Test server configuration
     */
    @GetMapping("/config")
    @Operation(summary = "Server Configuration", description = "Get current server configuration")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Configuration retrieved successfully")
    })
    public ResponseEntity<Map<String, Object>> getServerConfig() {
        Map<String, Object> config = Map.of(
            "server_type", "MCP Server",
            "ai_provider", "Azure OpenAI",
            "capabilities", List.of("chat", "campaign_advice", "marketing_tools"),
            "status", "active",
            "timestamp", System.currentTimeMillis()
        );
        
        return ResponseEntity.ok(config);
    }
}
