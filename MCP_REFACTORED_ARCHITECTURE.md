# MCP Refactored Architecture

## Overview

This document describes the refactored MCP (Model Context Protocol) architecture that separates server and client functionality into different packages for independent testing and development.

## Package Structure

```
src/main/java/com/example/mcp/
├── McpServerApplication.java          # Main Spring Boot application
├── server/                            # MCP Server package
│   ├── McpServerConfig.java          # Server configuration
│   ├── McpServerService.java         # Server core service
│   └── McpServerController.java      # Server testing endpoints
├── client/                            # MCP Client package
│   ├── McpClientService.java         # Client service (placeholder)
│   └── McpClientController.java      # Client testing endpoints
└── chat/                              # Chat functionality package
    ├── ChatService.java               # Chat service
    └── ChatController.java            # Chat API endpoints
```

## MCP Server Package

### McpServerConfig.java
- Configures Azure OpenAI ChatModel
- Creates ChatClient with marketing campaign system prompt
- Provides marketing prompt template

### McpServerService.java
- Core service for MCP server functionality
- Processes chat messages using AI
- Generates campaign advice
- Provides marketing tools and server health

### McpServerController.java
- Independent testing endpoints for MCP server
- `/api/mcp-server/chat` - Test chat functionality
- `/api/mcp-server/campaign-advice` - Test campaign advice
- `/api/mcp-server/tools` - Get available tools
- `/api/mcp-server/health` - Server health check
- `/api/mcp-server/config` - Server configuration

## MCP Client Package

### McpClientService.java
- Placeholder implementation for future MCP client functionality
- Currently provides mock responses for testing
- Will be implemented when Spring AI 1.0.1 MCP client is stable

### McpClientController.java
- Testing endpoints for MCP client functionality
- `/api/mcp-client/initialize` - Initialize client
- `/api/mcp-client/status` - Get client status
- `/api/mcp-client/tools` - Get marketing tools
- `/api/mcp-client/call-tool` - Call marketing tool
- `/api/mcp-client/resources/{type}` - Get resources
- `/api/mcp-client/disconnect` - Disconnect client

## Chat Package

### ChatService.java
- Integrates MCP server and client services
- Provides unified chat interface
- Handles message processing and campaign advice

### ChatController.java
- Simplified chat API endpoints
- `/api/chat/send` - Send chat message
- `/api/chat/campaign-advice` - Get campaign advice
- `/api/chat/test-connection` - Test backend connection
- `/api/chat/status` - Get service status

## Testing Strategy

### 1. Test MCP Server Independently
```bash
# Start server with MCP server enabled, client disabled
./start-mcp-server-test.sh

# Test server endpoints
curl http://localhost:8088/api/mcp-server/health
curl http://localhost:8088/api/mcp-server/tools
```

### 2. Test MCP Client Independently
```bash
# Test client endpoints (placeholder functionality)
curl http://localhost:8088/api/mcp-client/status
curl http://localhost:8088/api/mcp-client/tools
```

### 3. Test Chat Functionality
```bash
# Test chat endpoints
curl -X POST http://localhost:8088/api/chat/send \
  -H "Content-Type: application/json" \
  -d '{"message": "Hello, how can you help with marketing?"}'
```

## Configuration

### application.yml
```yaml
spring:
  ai:
    mcp:
      server:
        enabled: true
        transport: stdio
      client:
        enabled: false  # Disabled for now
    model:
      azure:
        openai:
          endpoint: ${AZURE_OPENAI_ENDPOINT}
          api-key: ${AZURE_OPENAI_API_KEY}
          deployment-name: ${AZURE_OPENAI_DEPLOYMENT_NAME}
```

### Environment Variables
```bash
export AZURE_OPENAI_ENDPOINT='your-azure-openai-endpoint'
export AZURE_OPENAI_API_KEY='your-azure-openai-api-key'
export AZURE_OPENAI_DEPLOYMENT_NAME='your-deployment-name'
```

## Benefits of Refactored Architecture

1. **Separation of Concerns**: Server and client functionality are clearly separated
2. **Independent Testing**: Each package can be tested independently
3. **Easier Maintenance**: Clear package boundaries and responsibilities
4. **Future Extensibility**: Easy to add new features to specific packages
5. **Spring AI 1.0.1 Compatibility**: Uses correct APIs and dependencies

## Next Steps

1. **Test MCP Server**: Ensure server functionality works correctly
2. **Implement MCP Client**: When Spring AI 1.0.1 MCP client is stable
3. **Add Integration Tests**: Test package interactions
4. **Performance Optimization**: Optimize AI responses and caching
5. **Security**: Add authentication and authorization

## Troubleshooting

### Common Issues

1. **Azure OpenAI Configuration**: Ensure environment variables are set correctly
2. **Port Conflicts**: Check if port 8088 is available
3. **Dependencies**: Ensure all Spring AI dependencies are correct for version 1.0.1
4. **Network Issues**: Check if external services are accessible

### Debug Mode
```bash
# Enable debug logging
--logging.level.com.example.mcp=DEBUG
--logging.level.org.springframework.ai=DEBUG
```

## API Documentation

Access Swagger UI at: http://localhost:8088/swagger-ui.html

This provides interactive documentation for all endpoints and allows testing directly from the browser.
