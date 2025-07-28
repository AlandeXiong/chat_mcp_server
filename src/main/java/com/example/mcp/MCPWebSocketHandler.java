package com.example.mcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.Map;

public class MCPWebSocketHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String cmd = message.getPayload().trim().toLowerCase();
        Object response;
        if (cmd.contains("campaign")) {
            response = Map.of(
                "name", "Summer Insurance Promotion",
                "period", "2024-07-01 ~ 2024-07-31",
                "budget", 200000,
                "desc", "Multi-channel campaign to promote new life insurance products."
            );
        } else if (cmd.contains("segment") || cmd.contains("people")) {
            response = Map.of(
                "segment", "30-55 years",
                "location", "Nationwide",
                "needs", "Family protection, wealth management"
            );
        } else if (cmd.contains("channel")) {
            response = Map.of(
                "channels", List.of("Email", "SMS", "What's App", "Social Media", "Call Center")
            );
        } else if (cmd.contains("strategy")) {
            response = Map.of(
                "channels", "Email, SMS, Social Media",
                "frequency", "Once per week",
                "budgetAllocation", "Email 30%, SMS 20%, Social 30%, Call Center 20%"
            );
        } else {
            response = Map.of("message", "Unknown command. Try: campaign, segment, channel, strategy.");
        }
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
    }
} 