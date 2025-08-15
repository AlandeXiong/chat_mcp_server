package com.example.mcp.server;

import org.springframework.ai.azure.openai.AzureOpenAiChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * MCP Server Configuration
 * Configures the MCP server with AI components and marketing campaign tools
 */
@Configuration
public class McpServerConfig {

    @Autowired
    @Qualifier("azureOpenAiChatModel")
    private AzureOpenAiChatModel chatModel;

    /**
     * ChatClient bean for MCP server
     * Provides AI chat capabilities for marketing campaign assistance
     */
    @Bean
    public ChatClient chatClient() {
        return ChatClient.builder(chatModel)
            .defaultSystem("""
                You are a marketing campaign assistant specialized in creating comprehensive marketing campaigns.
                You can help with:
                1. Creating marketing campaigns with detailed strategies
                2. Defining target audience segments
                3. Developing channel strategies (Email, SMS, Social Media, etc.)
                4. Creating email templates and content
                5. Budget allocation and campaign planning
                6. Campaign performance metrics and KPIs
                7. Multi-channel marketing optimization
                
                Always provide structured, actionable marketing advice with specific recommendations.
                Format your responses in a clear, professional manner suitable for business use.
                """)
            .build();
    }

    /**
     * ChatClient.Builder bean for dependency injection
     * Allows services to build their own ChatClient instances
     */
    @Bean
    @Primary
    public ChatClient.Builder chatClientBuilder() {
        return ChatClient.builder(chatModel)
            .defaultSystem("""
                You are a marketing campaign assistant specialized in creating comprehensive marketing campaigns.
                You can help with:
                1. Creating marketing campaigns with detailed strategies
                2. Defining target audience segments
                3. Developing channel strategies (Email, SMS, Social Media, etc.)
                4. Creating email templates and content
                5. Budget allocation and campaign planning
                6. Campaign performance metrics and KPIs
                7. Multi-channel marketing optimization
                
                Always provide structured, actionable marketing advice with specific recommendations.
                Format your responses in a clear, professional manner suitable for business use.
                """);
    }

    /**
     * Marketing prompt template for campaign creation
     * Provides structured prompts for AI marketing assistance
     */
    @Bean
    public PromptTemplate marketingPromptTemplate() {
        return new PromptTemplate("""
            Create a marketing campaign for:
            Industry: {industry}
            Target Audience: {targetAudience}
            Budget: ${budget}
            
            Please provide:
            1. Campaign strategy overview
            2. Target audience analysis
            3. Channel recommendations
            4. Content suggestions
            5. Timeline and milestones
            6. Success metrics
            """);
    }
} 