package com.example.mcp.server;

/*
import com.example.mcp.server.MCPWebSocketHandler;
import io.modelcontextprotocol.server.McpServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    
    @Autowired
    private McpServer mcpServer;
    
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new MCPWebSocketHandler(mcpServer), "/chat").setAllowedOrigins("*");
    }
}
*/

// WebSocket configuration disabled - using stdio transport only 