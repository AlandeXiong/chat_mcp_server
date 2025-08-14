# MCP Server with Stdio Transport - Spring AI 1.0.1

This MCP (Model Context Protocol) server is configured to use **stdio transport only** with Spring AI 1.0.1, providing a lightweight, command-line interface without web dependencies.

## Features

- ✅ Stdio transport support (Spring AI 1.0.1)
- ❌ No SSE (Server-Sent Events) 
- ❌ No WebFlux/WebSocket
- ❌ No MCP client auto-configuration
- ✅ Spring AI 1.0.1 integration
- ✅ OpenAI and Azure OpenAI support
- ✅ Marketing campaign assistance capabilities

## Configuration

The server is configured in `application.yml`:

```yaml
spring:
  ai:
    mcp:
      server:
        enabled: true
        transport: stdio
        stdio:
          enabled: true
          root-change-notification: false
    model:
      tool:
        enabled: false  # Disabled to avoid OAuth2 issues
```

## Dependencies

- Spring Boot 3.4.5
- Spring AI 1.0.1
- MCP Server (stdio transport only)
- Spring Security OAuth2 Client (required for Spring AI compatibility)
- No web dependencies

## Running the Server

### Option 1: Using Maven
```bash
mvn spring-boot:run \
  -Dspring.ai.mcp.server.enabled=true \
  -Dspring.ai.mcp.server.transport=stdio \
  -Dspring.ai.mcp.server.stdio.enabled=true \
  -Dspring.ai.model.tool.enabled=false
```

### Option 2: Using the startup script
```bash
# Linux/Mac
./start-mcp-server.sh

# Windows
start-mcp-server.bat
```

### Option 3: Using the JAR file
```bash
mvn clean package -DskipTests
java -jar target/mcp-backend-1.0.0.jar \
  --spring.ai.mcp.server.enabled=true \
  --spring.ai.mcp.server.transport=stdio \
  --spring.ai.mcp.server.stdio.enabled=true \
  --spring.ai.model.tool.enabled=false
```

## Troubleshooting

### Common Issues

1. **Port conflicts**: The server runs on port 8080 by default
2. **MCP client errors**: Ensure `spring.ai.mcp.client.enabled=false` (not needed in this config)
3. **Transport errors**: Ensure `spring.ai.mcp.server.stdio.enabled=true`
4. **OAuth2 errors**: Ensure `spring.ai.model.tool.enabled=false`

### Debug Mode

Enable debug logging:
```bash
--logging.level.org.springframework.ai.mcp=DEBUG
--logging.level.org.springframework.ai=DEBUG
```

## Integration

This MCP server can be integrated with:
- MCP clients that support stdio transport
- AI development tools
- Command-line interfaces
- Automation scripts

## Architecture

- **McpServerConfig**: Configures ChatClient and PromptTemplate
- **WebSocketConfig**: Disabled (stdio transport only)
- **McpServerApplication**: Main application with conditional MCP server loading
- **Stdio Transport**: Handles MCP communication via standard input/output
