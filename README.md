# Campaign Journey MCP Server with Spring AI

A Spring Boot application that provides a complete MCP (Model Context Protocol) implementation with both client and server components, integrated with Spring AI for creating comprehensive marketing campaigns through chat-based interactions.

## Architecture

The application is now structured with two main components:

### 1. **MCP Server** (`McpServerConfig.java`)
- Provides MCP server functionality
- Integrates with Spring AI ChatClient
- Handles marketing campaign requests
- Manages server properties and configuration

### 2. **MCP Client** (`McpClientConfig.java`)
- Connects to MCP servers via SSE transport
- Supports both synchronous and asynchronous clients
- Provides tool callback integration
- Customizable client behavior

## Features

- **MCP Server Integration**: Full MCP server implementation
- **MCP Client Support**: Multiple client connections with customization
- **Spring AI Integration**: Powered by Spring AI framework
- **Marketing Campaign Creation**: Generate complete marketing campaigns
- **Target Audience Segmentation**: Define and analyze target audiences
- **Channel Strategy Development**: Create multi-channel marketing strategies
- **Email Template Generation**: Generate email templates and content
- **WebSocket Communication**: Real-time chat interface
- **REST API**: HTTP endpoints for all operations
- **Tool Callbacks**: MCP tool integration with Spring AI

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- OpenAI API key or Azure OpenAI credentials

## Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd campaign-journey-backend
   ```

2. **Set up your AI credentials**
   
   Option 1: OpenAI
   ```bash
   export OPENAI_API_KEY=your-openai-api-key-here
   export OPENAI_MODEL=gpt-4
   ```
   
   Option 2: Azure OpenAI
   ```bash
   export AZURE_OPENAI_API_KEY=your-azure-api-key-here
   export AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com
   export AZURE_OPENAI_DEPLOYMENT_NAME=gpt-4
   ```

3. **Build the application**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

## Configuration

### MCP Server Configuration

```yaml
spring:
  ai:
    mcp:
      server:
        enabled: true
        name: "Campaign Journey MCP Server"
        version: "1.0.0"
        description: "MCP Server for marketing campaign journey management using Spring AI"
```

### MCP Client Configuration

```yaml
spring:
  ai:
    mcp:
      client:
        enabled: true
        name: "campaign-journey-mcp-client"
        version: "1.0.0"
        type: SYNC  # or ASYNC
        request-timeout: 30s
        initialized: true
        root-change-notification: true
        toolcallback:
          enabled: true
        
        # SSE Transport
        sse:
          connections:
            local-server:
              url: http://localhost:8080
              sse-endpoint: /sse
```

## Usage

### WebSocket Connection

Connect to the WebSocket endpoint:
```
ws://localhost:8080/chat
```

### MCP Commands

The WebSocket interface supports MCP-specific commands:

```
/mcp/status    - Check MCP server status
/mcp/tools     - Get available MCP tools
/mcp/health    - Check MCP health
```

### Marketing Commands

```
"Create a marketing campaign for life insurance products"
"Define target audience for our insurance products"
"Develop a multi-channel marketing strategy"
"Generate an email template for promotion"
"Analyze campaign performance metrics"
"Optimize budget allocation across channels"
```

### REST API Endpoints

#### Marketing Operations
- `POST /api/marketing/campaign` - Create campaign
- `POST /api/marketing/audience` - Define audience
- `POST /api/marketing/strategy` - Develop strategy
- `POST /api/marketing/template` - Generate template
- `POST /api/marketing/performance` - Analyze performance
- `POST /api/marketing/optimization` - Optimize budget

#### MCP Operations
- `GET /api/mcp/server/info` - Get MCP server info
- `GET /api/mcp/server/health` - Check MCP health
- `GET /api/mcp/clients` - Get MCP client info
- `POST /api/mcp/request` - Send MCP request

#### General
- `GET /api/capabilities` - Get all capabilities

## MCP Client Customization

The application provides customizable MCP clients:

### Sync Client Customizer
```java
@Component
public class CustomMcpSyncClientCustomizer implements McpSyncClientCustomizer {
    @Override
    public void customize(String serverConfigurationName, McpClient.SyncSpec spec) {
        spec.requestTimeout(Duration.ofSeconds(30));
        spec.toolsChangeConsumer((tools) -> {
            // Handle tools change
        });
        // ... more customization options
    }
}
```

### Async Client Customizer
```java
@Component
public class CustomMcpAsyncClientCustomizer implements McpAsyncClientCustomizer {
    @Override
    public void customize(String serverConfigurationName, McpClient.AsyncSpec spec) {
        spec.requestTimeout(Duration.ofSeconds(30));
    }
}
```

## Transport Support

- **SSE (Server-Sent Events)**: HTTP-based transport for remote MCP servers
- **Stdio**: Standard I/O transport for local MCP servers
- **WebFlux**: Reactive transport support (optional)

## Tool Callback Integration

The MCP client automatically integrates with Spring AI's tool execution framework:

```java
@Autowired
private SyncMcpToolCallbackProvider toolCallbackProvider;

ToolCallback[] toolCallbacks = toolCallbackProvider.getToolCallbacks();
```

## Response Structure

All responses include:
- `type`: Response type (campaign, segment, strategy, etc.)
- `content`: AI-generated content
- `status`: Success/error status
- `timestamp`: Response timestamp
- Additional fields based on response type

## Troubleshooting

### Common Issues

1. **MCP Client Connection**: Check SSE endpoint configuration
2. **AI Model Access**: Verify API keys and model configuration
3. **Transport Issues**: Ensure proper transport configuration

### Logs

Enable debug logging:
```yaml
logging:
  level:
    org.springframework.ai: DEBUG
    com.example.mcp: DEBUG
    org.springframework.ai.mcp: DEBUG
```

## Development

### Project Structure

```
src/main/java/com/example/mcp/
├── McpServerConfig.java        # MCP Server configuration
├── McpClientConfig.java        # MCP Client configuration
├── MarketingCampaignService.java    # Marketing business logic
├── MCPClientService.java       # MCP client operations
├── MCPWebSocketHandler.java    # WebSocket message handler
├── WebSocketConfig.java        # WebSocket configuration
├── MarketingCampaignController.java # REST API controller
└── McpServerApplication.java   # Spring Boot main class
```

### Adding New Features

1. **New MCP Tools**: Extend the MCP server with new tool definitions
2. **Client Customization**: Add new client customizers for specific behavior
3. **Transport Support**: Implement new transport mechanisms

## License

This project is licensed under the MIT License.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request
