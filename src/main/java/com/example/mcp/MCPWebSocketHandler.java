package com.example.mcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.Map;

public class MCPWebSocketHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Object mcpServer;

    public MCPWebSocketHandler(Object mcpServer) {
        this.mcpServer = mcpServer;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Send welcome message when connection is established
        Map<String, String> welcomeMessage = Map.of(
            "type", "connection",
            "message", "Connected to Campaign Journey MCP Server",
            "status", "ready"
        );
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(welcomeMessage)));
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload().trim();
        Object response;

        try {
            // Use MCP Server to handle natural language requests
            if (payload.toLowerCase().contains("create") ||
                payload.toLowerCase().contains("generate") ||
                payload.toLowerCase().contains("make")) {

                // Process request through MCP Server
                String mcpResponse = processWithMcpServer(payload);

                // Generate marketing campaign related information based on user request
                if (payload.toLowerCase().contains("campaign")) {
                    response = Map.of(
                        "type", "campaign",
                        "content", mcpResponse,
                        "name", extractCampaignName(mcpResponse),
                        "period", "To be determined",
                        "budget", "To be determined",
                        "desc", mcpResponse,
                        "status", "success"
                    );
                } else if (payload.toLowerCase().contains("segment") || payload.toLowerCase().contains("people")) {
                    response = Map.of(
                        "type", "segment",
                        "content", mcpResponse,
                        "segment", extractSegmentInfo(mcpResponse),
                        "location", "To be determined",
                        "needs", "To be determined",
                        "status", "success"
                    );
                } else if (payload.toLowerCase().contains("channel")) {
                    response = Map.of(
                        "type", "channel",
                        "content", mcpResponse,
                        "channels", List.of("Email", "SMS", "Social Media", "Call Center"),
                        "status", "success"
                    );
                } else if (payload.toLowerCase().contains("strategy")) {
                    response = Map.of(
                        "type", "strategy",
                        "content", mcpResponse,
                        "frequency", "To be determined",
                        "budgetAllocation", "To be determined",
                        "status", "success"
                    );
                } else {
                    // General response handling
                    response = Map.of(
                        "type", "general",
                        "content", mcpResponse,
                        "campaign", Map.of(
                            "name", extractCampaignName(mcpResponse),
                            "period", "2024-07-01 ~ 2024-07-31",
                            "budget", 200000,
                            "desc", mcpResponse
                        ),
                        "segment", Map.of(
                            "segment", extractSegmentInfo(mcpResponse),
                            "location", "Nationwide",
                            "needs", "Family protection, wealth management"
                        ),
                        "channel", Map.of(
                            "channels", List.of("Email", "SMS", "What's App", "Social Media", "Call Center")
                        ),
                        "strategy", Map.of(
                            "channels", "Email, SMS, Social Media",
                            "frequency", "Once per week",
                            "budgetAllocation", "Email 30%, SMS 20%, Social 30%, Call Center 20%"
                        ),
                        "status", "success"
                    );
                }
            } else if (payload.toLowerCase().contains("campaign")) {
                response = Map.of(
                    "name", "Summer Insurance Promotion",
                    "period", "2024-07-01 ~ 2024-07-31",
                    "budget", 200000,
                    "desc", "Multi-channel campaign to promote new life insurance products.",
                    "status", "success"
                );
            } else if (payload.toLowerCase().contains("segment") || payload.toLowerCase().contains("people")) {
                response = Map.of(
                    "segment", "30-55 years",
                    "location", "Nationwide",
                    "needs", "Family protection, wealth management",
                    "status", "success"
                );
            } else if (payload.toLowerCase().contains("channel")) {
                response = Map.of(
                    "channels", List.of("Email", "SMS", "What's App", "Social Media", "Call Center"),
                    "status", "success"
                );
            } else if (payload.toLowerCase().contains("strategy")) {
                response = Map.of(
                    "channels", "Email, SMS, Social Media",
                    "frequency", "Once per week",
                    "budgetAllocation", "Email 30%, SMS 20%, Social 30%, Call Center 20%",
                    "status", "success"
                );
            } else {
                response = Map.of(
                    "message", "Unknown command. Try: campaign, segment, channel, strategy, or use 'create [your request]' to generate a custom campaign.",
                    "status", "error"
                );
            }
        } catch (Exception e) {
            // Handle errors gracefully
            response = Map.of(
                "error", "Failed to process request: " + e.getMessage(),
                "status", "error"
            );
        }

        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
    }

    // Process request with MCP Server
    private String processWithMcpServer(String request) {
        try {
            // For now, use mock response since MCP Server is simplified
            return generateMockMcpResponse(request);
        } catch (Exception e) {
            // Fall back to mock response if MCP Server fails
            return generateMockMcpResponse(request);
        }
    }

    // Mock MCP response generator
    private String generateMockMcpResponse(String request) {
        if (request.toLowerCase().contains("campaign")) {
            return "Generated a comprehensive insurance marketing campaign targeting families with life insurance products.";
        } else if (request.toLowerCase().contains("segment")) {
            return "Identified target segment: families aged 30-55 with children, interested in financial security.";
        } else if (request.toLowerCase().contains("strategy")) {
            return "Developed multi-channel marketing strategy with email, SMS, and social media campaigns.";
        } else {
            return "Processed your request and generated relevant marketing campaign information.";
        }
    }

    // Helper method to extract campaign name
    private String extractCampaignName(String text) {
        // More complex logic or regex can be used here to extract campaign name
        if (text.contains("insurance")) {
            return "Insurance Promotion Campaign";
        } else if (text.contains("sale") || text.contains("discount")) {
            return "Seasonal Sale Campaign";
        } else {
            return "Custom Marketing Campaign";
        }
    }

    // Helper method to extract segment information
    private String extractSegmentInfo(String text) {
        // More complex logic can be used here to extract segment information
        if (text.contains("family") || text.contains("parent")) {
            return "Family-oriented customers";
        } else if (text.contains("young") || text.contains("millenial")) {
            return "Young professionals";
        } else if (text.contains("business")) {
            return "Business clients";
        } else {
            return "General customer segment";
        }
    }
}
