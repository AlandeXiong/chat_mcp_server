# MCP Client Integration Guide

## Overview

This document describes how to integrate the Model Context Protocol (MCP) client with marketing services to retrieve model context services and enhance AI recommendations.

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Campaign Journey Backend                 │
├─────────────────────────────────────────────────────────────┤
│  MCP Client Service                                        │
│  ├── Initialize connection to marketing service            │
│  ├── Retrieve available tools                             │
│  ├── Call marketing service tools                          │
│  ├── Get marketing resources                               │
│  └── Read marketing schemas                                │
├─────────────────────────────────────────────────────────────┤
│  AI Recommendation Generator                               │
│  ├── Enhanced with MCP client integration                  │
│  ├── Get marketing service information                     │
│  └── Call marketing service tools                          │
├─────────────────────────────────────────────────────────────┤
│  Marketing Service (External)                              │
│  ├── MCP Server implementation                             │
│  ├── Campaign tools and resources                          │
│  └── Marketing data schemas                                │
└─────────────────────────────────────────────────────────────┘
```

## Configuration

### 1. Application Properties

Add the following configuration to `application.yml`:

```yaml
spring:
  ai:
    mcp:
      client:
        enabled: true
        marketing:
          service:
            path: "/path/to/marketing/service"
```

### 2. Environment Variables

Set the following environment variables:

```bash
# Marketing service path
MCP_MARKETING_SERVICE_PATH=/usr/local/bin/marketing-service

# Enable MCP client
MCP_CLIENT_ENABLED=true
```

## Usage

### 1. Initialize MCP Client

```java
@Autowired
private McpClientService mcpClientService;

// Initialize connection to marketing service
mcpClientService.initializeMcpClient();
```

### 2. Get Marketing Service Information

```java
@Autowired
private AIRecommendationGenerator recommendationGenerator;

// Get available tools, resources, and schemas
Map<String, Object> serviceInfo = recommendationGenerator.getMarketingServiceInfo();
```

### 3. Call Marketing Service Tools

```java
// Call specific tool on marketing service
Map<String, Object> result = recommendationGenerator.callMarketingServiceTool(
    "create_campaign_segment", 
    Map.of("audience_type", "young_professionals", "budget", 10000.0)
);
```

## API Endpoints

### MCP Client Management

#### Connect MCP Client
```
POST /api/chat/mcp-client/connect
```
Initializes MCP client connection to marketing service.

#### Get MCP Client Status
```
GET /api/chat/mcp-client/status
```
Returns current connection status.

#### Get Marketing Service Info
```
GET /api/chat/mcp-client/marketing-info
```
Retrieves information about available tools, resources, and schemas.

#### Call Marketing Service Tool
```
POST /api/chat/mcp-client/call-tool
{
  "toolName": "create_campaign_segment",
  "arguments": {
    "audience_type": "young_professionals",
    "budget": 10000.0
  }
}
```
Calls a specific tool on the marketing service.

## Frontend Integration

### 1. Connect to MCP Client

```javascript
import { chatService } from './services/chatService';

// Connect to MCP client
const result = await chatService.connectMcpClient();
console.log('MCP Client connected:', result.connected);
```

### 2. Get Marketing Service Information

```javascript
// Get marketing service info
const serviceInfo = await chatService.getMarketingServiceInfo();
console.log('Available tools:', serviceInfo.data);
```

### 3. Call Marketing Service Tools

```javascript
// Call marketing service tool
const result = await chatService.callMarketingServiceTool(
    'create_campaign_segment',
    { audience_type: 'young_professionals', budget: 10000.0 }
);
console.log('Tool result:', result);
```

## Marketing Service Requirements

### 1. MCP Server Implementation

The marketing service must implement the MCP server protocol:

```python
# Example Python MCP server
from mcp.server import Server
from mcp.server.models import Tool

server = Server("marketing-service")

@server.tool()
async def create_campaign_segment(audience_type: str, budget: float) -> dict:
    """Create a campaign segment based on audience type and budget."""
    # Implementation logic
    return {"segment_id": "seg_123", "audience_type": audience_type, "budget": budget}

@server.list_resources()
async def list_campaign_resources() -> list:
    """List available campaign resources."""
    return [
        {"uri": "campaign://segments", "name": "Campaign Segments"},
        {"uri": "campaign://strategies", "name": "Campaign Strategies"}
    ]

@server.read_resource()
async def read_campaign_resource(uri: str) -> str:
    """Read campaign resource content."""
    # Implementation logic
    return "Campaign resource content"
```

### 2. Available Tools

The marketing service should provide the following tools:

- **create_campaign_segment**: Create target audience segments
- **generate_delivery_strategy**: Generate delivery strategies
- **create_email_template**: Create email templates
- **design_customer_journey**: Design customer journey maps
- **analyze_campaign_performance**: Analyze campaign performance

### 3. Resource Types

The marketing service should provide the following resources:

- **campaign://segments**: Available audience segments
- **campaign://strategies**: Delivery strategies
- **campaign://templates**: Email templates
- **campaign://journeys**: Customer journey maps
- **campaign://analytics**: Performance analytics

## Error Handling

### 1. Connection Errors

```java
try {
    mcpClientService.initializeMcpClient();
} catch (Exception e) {
    // Handle connection failure
    log.error("Failed to connect to marketing service: {}", e.getMessage());
    // Fallback to local recommendations
}
```

### 2. Tool Call Errors

```java
try {
    Map<String, Object> result = recommendationGenerator.callMarketingServiceTool(
        toolName, arguments);
    // Process successful result
} catch (Exception e) {
    // Handle tool call failure
    log.error("Failed to call marketing service tool: {}", e.getMessage());
    // Use local fallback or cached data
}
```

## Security Considerations

### 1. Service Authentication

- Implement authentication between MCP client and marketing service
- Use secure transport protocols
- Validate tool calls and resource access

### 2. Data Validation

- Validate all input parameters
- Sanitize tool responses
- Implement rate limiting

### 3. Access Control

- Restrict tool access based on user permissions
- Audit all tool calls and resource access
- Implement resource-level access control

## Performance Optimization

### 1. Connection Pooling

- Maintain persistent connections to marketing service
- Implement connection health checks
- Use connection pooling for multiple requests

### 2. Caching

- Cache frequently accessed resources
- Cache tool responses when appropriate
- Implement cache invalidation strategies

### 3. Async Processing

- Use async/await for tool calls
- Implement request queuing for high-load scenarios
- Use background processing for long-running operations

## Monitoring and Logging

### 1. Connection Monitoring

```java
@Scheduled(fixedRate = 30000) // Every 30 seconds
public void monitorMcpClientConnection() {
    boolean isConnected = mcpClientService.isConnected();
    if (!isConnected) {
        log.warn("MCP client disconnected, attempting reconnection...");
        try {
            mcpClientService.initializeMcpClient();
        } catch (Exception e) {
            log.error("Failed to reconnect MCP client: {}", e.getMessage());
        }
    }
}
```

### 2. Tool Call Logging

```java
@Aspect
@Component
public class McpToolCallLoggingAspect {
    
    @Around("execution(* com.example.mcp.server.AIRecommendationGenerator.callMarketingServiceTool(..))")
    public Object logToolCall(ProceedingJoinPoint joinPoint) throws Throwable {
        String toolName = (String) joinPoint.getArgs()[0];
        long startTime = System.currentTimeMillis();
        
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            log.info("Tool call to '{}' completed in {}ms", toolName, duration);
            return result;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("Tool call to '{}' failed after {}ms: {}", toolName, duration, e.getMessage());
            throw e;
        }
    }
}
```

## Testing

### 1. Unit Tests

```java
@ExtendWith(MockitoExtension.class)
class McpClientServiceTest {
    
    @Mock
    private McpClient mcpClient;
    
    @InjectMocks
    private McpClientService mcpClientService;
    
    @Test
    void testInitializeMcpClient() {
        // Test client initialization
        assertDoesNotThrow(() -> mcpClientService.initializeMcpClient());
    }
    
    @Test
    void testGetMarketingTools() {
        // Test tool retrieval
        List<Map<String, Object>> tools = mcpClientService.getMarketingTools();
        assertNotNull(tools);
    }
}
```

### 2. Integration Tests

```java
@SpringBootTest
@AutoConfigureTestDatabase
class McpClientIntegrationTest {
    
    @Autowired
    private McpClientService mcpClientService;
    
    @Test
    void testFullMcpClientWorkflow() {
        // Test complete workflow
        mcpClientService.initializeMcpClient();
        assertTrue(mcpClientService.isConnected());
        
        List<Map<String, Object>> tools = mcpClientService.getMarketingTools();
        assertFalse(tools.isEmpty());
        
        mcpClientService.disconnect();
        assertFalse(mcpClientService.isConnected());
    }
}
```

## Troubleshooting

### 1. Common Issues

#### Connection Failed
- Check marketing service path configuration
- Verify marketing service is running
- Check network connectivity and firewall settings

#### Tool Call Failed
- Verify tool name and parameters
- Check marketing service logs
- Validate tool implementation

#### Resource Access Denied
- Check authentication credentials
- Verify resource URI format
- Check access permissions

### 2. Debug Mode

Enable debug logging:

```yaml
logging:
  level:
    com.example.mcp.client: DEBUG
    com.example.mcp.server: DEBUG
```

### 3. Health Checks

Use the status endpoint to check client health:

```bash
curl http://localhost:8088/api/chat/mcp-client/status
```

## Future Enhancements

### 1. Advanced Features

- **Load Balancing**: Support multiple marketing services
- **Service Discovery**: Automatic service discovery
- **Circuit Breaker**: Implement circuit breaker pattern
- **Metrics**: Advanced performance metrics

### 2. Integration Patterns

- **Event Streaming**: Real-time event streaming
- **Batch Processing**: Batch tool calls and resource retrieval
- **Webhook Support**: Push-based updates from marketing service

### 3. AI Enhancement

- **Tool Learning**: AI learns from tool usage patterns
- **Predictive Caching**: Predict and cache frequently needed resources
- **Intelligent Fallbacks**: Smart fallback strategies based on context

## Conclusion

The MCP client integration provides a powerful way to extend the AI recommendation system with external marketing services. By following this guide, you can successfully integrate with marketing services and enhance the campaign creation process with real-time data and specialized tools.

For additional support or questions, refer to the MCP protocol documentation and Spring AI integration guides.
