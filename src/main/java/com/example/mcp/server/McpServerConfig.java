package com.example.mcp.server;

import io.modelcontextprotocol.server.McpServer;
import org.springframework.ai.azure.openai.AzureOpenAiChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class McpServerConfig {


    @Autowired
    AzureOpenAiChatModel chatModel;

    @Bean
    @Primary
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
    
    @Bean
    public PromptTemplate marketingPromptTemplate() {
        return new PromptTemplate("""
            You are a marketing campaign expert. Based on the user's request, provide detailed, actionable marketing advice.
            
            User request: {request}
            
            Please provide a comprehensive response that includes:
            1. Campaign strategy and objectives
            2. Target audience segmentation
            3. Channel recommendations
            4. Budget allocation suggestions
            5. Timeline and milestones
            6. Key performance indicators
            7. Risk assessment and mitigation strategies
            
            Format your response in a clear, structured manner with actionable insights.
            """);
    }
} 