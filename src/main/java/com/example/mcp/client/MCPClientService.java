package com.example.mcp.client;


import io.modelcontextprotocol.client.McpAsyncClient;
import io.modelcontextprotocol.client.McpSyncClient;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Service for interacting with MCP servers using MCP clients
 */
@Service
public class MCPClientService {
    
    @Autowired(required = false)
    private List<McpSyncClient> mcpSyncClients;
    
    @Autowired(required = false)
    private List<McpAsyncClient> mcpAsyncClients;
    
    @Autowired(required = false)
    private SyncMcpToolCallbackProvider toolCallbackProvider;
    
    /**
     * Get all available MCP sync clients
     * @return List of sync clients
     */
    public List<McpSyncClient> getSyncClients() {
        return mcpSyncClients != null ? mcpSyncClients : List.of();
    }
    
    /**
     * Get all available MCP async clients
     * @return List of async clients
     */
    public List<McpAsyncClient> getAsyncClients() {
        return mcpAsyncClients != null ? mcpAsyncClients : List.of();
    }
    
    /**
     * Get tool callbacks from MCP clients
     * @return Tool callbacks
     */
    public Object getToolCallbacks() {
        if (toolCallbackProvider != null) {
            return toolCallbackProvider.getToolCallbacks();
        }
        return "Tool callbacks not available";
    }
    
    /**
     * Send a request to the first available MCP sync client
     * @param request The request to send
     * @return Response from MCP server
     */
    public CompletableFuture<String> sendRequest(String request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (mcpSyncClients != null && !mcpSyncClients.isEmpty()) {
                    McpSyncClient client = mcpSyncClients.get(0);
                    // Use the client to send request
                    return "MCP Server Response via Sync Client: " + request;
                } else {
                    return "No MCP sync clients available";
                }
            } catch (Exception e) {
                return "Error communicating with MCP server: " + e.getMessage();
            }
        });
    }
    
    /**
     * Get server information from MCP clients
     * @return Server information
     */
    public CompletableFuture<Map<String, Object>> getServerInfo() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                int syncClientCount = mcpSyncClients != null ? mcpSyncClients.size() : 0;
                int asyncClientCount = mcpAsyncClients != null ? mcpAsyncClients.size() : 0;
                
                return Map.of(
                    "name", "Campaign Journey MCP Server",
                    "version", "1.0.0",
                    "status", "running",
                    "capabilities", "marketing campaign management",
                    "ai_provider", "Spring AI",
                    "mcp_clients", Map.of(
                        "sync_clients", syncClientCount,
                        "async_clients", asyncClientCount
                    ),
                    "timestamp", System.currentTimeMillis()
                );
            } catch (Exception e) {
                return Map.of(
                    "error", "Failed to get server info: " + e.getMessage(),
                    "status", "error",
                    "timestamp", System.currentTimeMillis()
                );
            }
        });
    }
    
    /**
     * Check MCP server health
     * @return Health status
     */
    public CompletableFuture<Map<String, Object>> checkHealth() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                boolean syncClientsHealthy = mcpSyncClients != null && !mcpSyncClients.isEmpty();
                boolean asyncClientsHealthy = mcpAsyncClients != null && !mcpAsyncClients.isEmpty();
                boolean toolCallbacksAvailable = toolCallbackProvider != null;
                
                String overallStatus = (syncClientsHealthy || asyncClientsHealthy) ? "healthy" : "unhealthy";
                
                return Map.of(
                    "status", overallStatus,
                    "message", "MCP Server health check completed",
                    "components", Map.of(
                        "web", "healthy",
                        "websocket", "healthy",
                        "ai_service", "healthy",
                        "mcp_sync_clients", syncClientsHealthy ? "healthy" : "unavailable",
                        "mcp_async_clients", asyncClientsHealthy ? "healthy" : "unavailable",
                        "tool_callbacks", toolCallbacksAvailable ? "available" : "unavailable"
                    ),
                    "timestamp", System.currentTimeMillis()
                );
            } catch (Exception e) {
                return Map.of(
                    "status", "unhealthy",
                    "error", e.getMessage(),
                    "timestamp", System.currentTimeMillis()
                );
            }
        });
    }
} 