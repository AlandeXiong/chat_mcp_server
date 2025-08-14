package com.example.mcp;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.modelcontextprotocol.server.McpServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MCPWebSocketHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final McpServer mcpServer;

    @Autowired
    private MarketingCampaignService marketingService;

    @Autowired
    private MCPClientService mcpClientService;

    public MCPWebSocketHandler(McpServer mcpServer) {
        this.mcpServer = mcpServer;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Send welcome message when connection is established
        Map<String, String> welcomeMessage = Map.of(
            "type", "connection",
            "message", "Connected to Campaign Journey MCP Server with Spring AI",
            "status", "ready",
            "capabilities", "Create campaigns, segments, strategies, templates, analyze performance, optimize budget",
            "mcp_server", "enabled",
            "mcp_client", "enabled"
        );
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(welcomeMessage)));
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload().trim();
        Object response;

        try {
            // Check if this is an MCP-specific command
            if (payload.startsWith("/mcp")) {
                response = handleMCPCommand(payload);
            } else {
                // Process marketing request through service layer
                response = processMarketingRequest(payload);
            }

        } catch (Exception e) {
            // Handle errors gracefully
            response = Map.of(
                "error", "Failed to process request: " + e.getMessage(),
                "status", "error",
                "timestamp", System.currentTimeMillis()
            );
        }

        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
    }

    // Handle MCP-specific commands
    private Map<String, Object> handleMCPCommand(String command) throws Exception {
        if (command.equals("/mcp/status")) {
            return Map.of(
                "type", "mcp_status",
                "server", Map.of(
                    "name", "lcms",
                    "version", "0.1",
                    "status", "running"
                ),
                "clients", Map.of(
                    "sync_clients", mcpClientService.getSyncClients().size(),
                    "async_clients", mcpClientService.getAsyncClients().size()
                ),
                "timestamp", System.currentTimeMillis()
            );
        } else if (command.equals("/mcp/tools")) {
            return Map.of(
                "type", "mcp_tools",
                "tools", mcpClientService.getToolCallbacks(),
                "timestamp", System.currentTimeMillis()
            );
        } else if (command.equals("/mcp/health")) {
            CompletableFuture<Map<String, Object>> healthFuture = mcpClientService.checkHealth();
            return healthFuture.get();
        } else {
            return Map.of(
                "type", "mcp_error",
                "error", "Unknown MCP command: " + command,
                "available_commands", List.of("/mcp/status", "/mcp/tools", "/mcp/health"),
                "timestamp", System.currentTimeMillis()
            );
        }
    }

    // Process marketing request through service layer
    private Map<String, Object> processMarketingRequest(String request) throws Exception {
        String lowerRequest = request.toLowerCase();

        // Route to appropriate service method based on request content
        if (lowerRequest.contains("campaign") || lowerRequest.contains("create") ||
            lowerRequest.contains("generate") || lowerRequest.contains("make")) {

            CompletableFuture<Map<String, Object>> future = marketingService.createCampaign(request);
            return future.get();

        } else if (lowerRequest.contains("segment") || lowerRequest.contains("audience") ||
                   lowerRequest.contains("people") || lowerRequest.contains("target")) {

            CompletableFuture<Map<String, Object>> future = marketingService.defineAudienceSegments(request);
            return future.get();

        } else if (lowerRequest.contains("channel") || lowerRequest.contains("strategy") ||
                   lowerRequest.contains("plan") || lowerRequest.contains("approach")) {

            CompletableFuture<Map<String, Object>> future = marketingService.developChannelStrategy(request);
            return future.get();

        } else if (lowerRequest.contains("template") || lowerRequest.contains("email") ||
                   lowerRequest.contains("content") || lowerRequest.contains("message")) {

            CompletableFuture<Map<String, Object>> future = marketingService.generateEmailTemplate(request);
            return future.get();

        } else if (lowerRequest.contains("performance") || lowerRequest.contains("analyze") ||
                   lowerRequest.contains("metrics") || lowerRequest.contains("kpi")) {

            CompletableFuture<Map<String, Object>> future = marketingService.analyzeCampaignPerformance(request);
            return future.get();

        } else if (lowerRequest.contains("budget") || lowerRequest.contains("optimize") ||
                   lowerRequest.contains("roi") || lowerRequest.contains("allocation")) {

            CompletableFuture<Map<String, Object>> future = marketingService.optimizeBudgetAllocation(request);
            return future.get();

        } else {
            // General help response
            return Map.of(
                "type", "help",
                "message", "I can help you with marketing campaigns. Try asking about:",
                "capabilities", List.of(
                    "Creating marketing campaigns",
                    "Defining target audiences",
                    "Developing channel strategies",
                    "Generating email templates",
                    "Analyzing performance",
                    "Optimizing budget allocation"
                ),
                "examples", List.of(
                    "\"Create a campaign for life insurance products\"",
                    "\"Define target audience for our services\"",
                    "\"Develop a multi-channel marketing strategy\"",
                    "\"Generate an email template for promotion\"",
                    "\"Analyze campaign performance metrics\"",
                    "\"Optimize budget allocation across channels\""
                ),
                "mcp_commands", List.of(
                    "/mcp/status - Check MCP server status",
                    "/mcp/tools - Get available MCP tools",
                    "/mcp/health - Check MCP health"
                ),
                "status", "info",
                "timestamp", System.currentTimeMillis()
            );
        }
    }
}
