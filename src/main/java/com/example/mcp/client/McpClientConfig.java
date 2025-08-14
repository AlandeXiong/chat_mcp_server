package com.example.mcp.client;




import io.modelcontextprotocol.client.McpClient;
import org.springframework.ai.mcp.customizer.McpAsyncClientCustomizer;
import org.springframework.ai.mcp.customizer.McpSyncClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.Duration;
import java.util.List;

@Configuration
public class McpClientConfig {
    
    /**
     * MCP Client Customizer for synchronous clients
     */
    @Bean
    public McpSyncClientCustomizer mcpSyncClientCustomizer() {
        return new McpSyncClientCustomizer() {
            @Override
            public void customize(String serverConfigurationName, McpClient.SyncSpec spec) {
                // Customize the request timeout configuration
                spec.requestTimeout(Duration.ofSeconds(30));
                
                // Add tools change consumer
                spec.toolsChangeConsumer((tools) -> {
                    System.out.println("Tools changed for " + serverConfigurationName + ": " + tools.size() + " tools available");
                });
                
                // Add resources change consumer
                spec.resourcesChangeConsumer((resources) -> {
                    System.out.println("Resources changed for " + serverConfigurationName + ": " + resources.size() + " resources available");
                });
                
                // Add prompts change consumer
                spec.promptsChangeConsumer((prompts) -> {
                    System.out.println("Prompts changed for " + serverConfigurationName + ": " + prompts.size() + " prompts available");
                });
                
                // Add logging consumer
                /*                spec.loggingConsumer((log) -> {
                    System.out.println("Log from " + serverConfigurationName + ": " + log.message());
                });*/
            }
        };
    }
    
    /**
     * MCP Client Customizer for asynchronous clients
     */
    @Bean
    public McpAsyncClientCustomizer mcpAsyncClientCustomizer() {
        return new McpAsyncClientCustomizer() {
            @Override
            public void customize(String serverConfigurationName, McpClient.AsyncSpec spec) {
                // Customize the async client configuration
                spec.requestTimeout(Duration.ofSeconds(30));
            }
        };
    }
} 