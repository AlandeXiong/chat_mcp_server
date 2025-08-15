package com.example.mcp.client;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * MCP Client Controller
 * REST endpoints for testing MCP client functionality independently
 */
@RestController
@RequestMapping("/api/mcp-client")
@Tag(name = "MCP Client API", description = "MCP Client testing and management endpoints")
@CrossOrigin(origins = "*")
public class McpClientController {

    @Autowired
    private MCPClientService mcpClientService;

    /**
     * Initialize MCP client connection
     */
    @PostMapping("/initialize")
    @Operation(summary = "Initialize Client", description = "Initialize MCP client connection")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Client initialized successfully"),
        @ApiResponse(responseCode = "500", description = "Initialization failed")
    })
    public ResponseEntity<Map<String, Object>> initializeClient() {
        try {
            mcpClientService.initializeMcpClient();
            
            Map<String, Object> response = Map.of(
                "success", true,
                "message", "MCP client initialized successfully",
                "enabled", mcpClientService.isEnabled(),
                "service_path", mcpClientService.getMarketingServicePath(),
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
     * Get client status
     */
    @GetMapping("/status")
    @Operation(summary = "Client Status", description = "Get MCP client connection status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status retrieved successfully")
    })
    public ResponseEntity<Map<String, Object>> getClientStatus() {
        Map<String, Object> status = Map.of(
            "success", true,
            "enabled", mcpClientService.isEnabled(),
            "service_path", mcpClientService.getMarketingServicePath(),
            "status", mcpClientService.isEnabled() ? "enabled" : "disabled",
            "timestamp", System.currentTimeMillis()
        );
        
        return ResponseEntity.ok(status);
    }

    /**
     * Get available marketing tools from external service
     */
    @GetMapping("/tools")
    @Operation(summary = "Get Marketing Tools", description = "Get available tools from external marketing service")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tools retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Failed to get tools")
    })
    public ResponseEntity<Map<String, Object>> getMarketingTools() {
        try {
            var tools = mcpClientService.getMarketingTools();
            
            Map<String, Object> response = Map.of(
                "success", true,
                "tools", tools,
                "count", tools.size(),
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
     * Call marketing service tool
     */
    @PostMapping("/call-tool")
    @Operation(summary = "Call Marketing Tool", description = "Call a tool on external marketing service")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tool called successfully"),
        @ApiResponse(responseCode = "500", description = "Tool call failed")
    })
    public ResponseEntity<Map<String, Object>> callMarketingTool(
        @Parameter(description = "Tool call request", required = true) @RequestBody Map<String, Object> request
    ) {
        try {
            String toolName = (String) request.get("toolName");
            Map<String, Object> arguments = (Map<String, Object>) request.get("arguments");
            
            var result = mcpClientService.callMarketingTool(toolName, arguments);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "tool_name", toolName,
                "arguments", arguments,
                "result", result,
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
     * Get marketing service resources
     */
    @GetMapping("/resources/{resourceType}")
    @Operation(summary = "Get Resources", description = "Get resources from external marketing service")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resources retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Failed to get resources")
    })
    public ResponseEntity<Map<String, Object>> getMarketingResources(
        @Parameter(description = "Resource type", required = true) @PathVariable String resourceType
    ) {
        try {
            var resources = mcpClientService.getMarketingResources(resourceType);
            
            Map<String, Object> response = Map.of(
                "success", true,
                "resource_type", resourceType,
                "resources", resources,
                "count", resources.size(),
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
     * Disconnect MCP client
     */
    @PostMapping("/disconnect")
    @Operation(summary = "Disconnect Client", description = "Disconnect MCP client from external service")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Client disconnected successfully")
    })
    public ResponseEntity<Map<String, Object>> disconnectClient() {
        mcpClientService.disconnect();
        
        Map<String, Object> response = Map.of(
            "success", true,
            "message", "MCP client disconnected",
            "timestamp", System.currentTimeMillis()
        );
        
        return ResponseEntity.ok(response);
    }
}
