package com.example.mcp.client;


import io.modelcontextprotocol.client.McpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * MCP Client Service
 * Retrieves model context services from marketing services through MCP protocol
 */
@Service
public class McpClientService {

    @Value("${spring.ai.mcp.client.enabled:false}")
    private boolean mcpClientEnabled;

    @Value("${spring.ai.mcp.client.marketing.service.path:}")
    private String marketingServicePath;

    private McpClient mcpClient;

    /**
     * Initialize MCP client connection to marketing service
     */
    public void initializeMcpClient() {
        if (!mcpClientEnabled) {
            return;
        }

        try {
            // Configure stdio transport for MCP client
            StdioTransportConfig transportConfig = StdioTransportConfig.builder()
                .command(marketingServicePath)
                .args(List.of("--mcp-server"))
                .build();

            StdioTransport transport = new StdioTransport(transportConfig);

            // Configure MCP client
            McpClientConfig clientConfig = McpClientConfig.builder()
                .transport(transport)
                .build();

            mcpClient = new McpClient(clientConfig);

            // Connect to marketing service
            mcpClient.connect();

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize MCP client: " + e.getMessage(), e);
        }
    }

    /**
     * Get available tools from marketing service
     */
    public List<Map<String, Object>> getMarketingTools() {
        if (mcpClient == null) {
            throw new IllegalStateException("MCP client not initialized");
        }

        try {
            // Request tools from marketing service
            CompletableFuture<List<Map<String, Object>>> toolsFuture = mcpClient.listTools();
            return toolsFuture.get();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get marketing tools: " + e.getMessage(), e);
        }
    }

    /**
     * Call marketing service tool
     */
    public Map<String, Object> callMarketingTool(String toolName, Map<String, Object> arguments) {
        if (mcpClient == null) {
            throw new IllegalStateException("MCP client not initialized");
        }

        try {
            // Call specific tool on marketing service
            CompletableFuture<Map<String, Object>> resultFuture = mcpClient.callTool(toolName, arguments);
            return resultFuture.get();
        } catch (Exception e) {
            throw new RuntimeException("Failed to call marketing tool: " + e.getMessage(), e);
        }
    }

    /**
     * Get marketing service resources
     */
    public List<Map<String, Object>> getMarketingResources(String resourceType) {
        if (mcpClient == null) {
            throw new IllegalStateException("MCP client not initialized");
        }

        try {
            // Request resources from marketing service
            CompletableFuture<List<Map<String, Object>>> resourcesFuture = mcpClient.listResources(resourceType);
            return resourcesFuture.get();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get marketing resources: " + e.getMessage(), e);
        }
    }

    /**
     * Read marketing service resource content
     */
    public String readMarketingResource(String resourceUri) {
        if (mcpClient == null) {
            throw new IllegalStateException("MCP client not initialized");
        }

        try {
            // Read specific resource from marketing service
            CompletableFuture<String> contentFuture = mcpClient.readResource(resourceUri);
            return contentFuture.get();
        } catch (Exception e) {
            throw new RuntimeException("Failed to read marketing resource: " + e.getMessage(), e);
        }
    }

    /**
     * Get marketing service schemas
     */
    public Map<String, Object> getMarketingSchemas() {
        if (mcpClient == null) {
            throw new IllegalStateException("MCP client not initialized");
        }

        try {
            // Request schemas from marketing service
            CompletableFuture<Map<String, Object>> schemasFuture = mcpClient.listSchemas();
            return schemasFuture.get();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get marketing schemas: " + e.getMessage(), e);
        }
    }

    /**
     * Check if MCP client is connected
     */
    public boolean isConnected() {
        return mcpClient != null && mcpClient.isConnected();
    }

    /**
     * Disconnect MCP client
     */
    public void disconnect() {
        if (mcpClient != null) {
            try {
                mcpClient.disconnect();
            } catch (Exception e) {
                // Log error but don't throw
                System.err.println("Error disconnecting MCP client: " + e.getMessage());
            }
        }
    }
}