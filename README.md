n# MCP Backend (Spring Boot)

This is a Spring Boot WebSocket server for the MCP (Multi-Channel Platform) demo. It simulates marketing campaign, segment, channel, and strategy info for front-end chat integration.

## Project Structure

```
backend/
├── pom.xml
├── README.md
└── src/
    └── main/
        └── java/
            └── com/
                └── example/
                    └── mcp/
                        ├── McpServerApplication.java
                        ├── WebSocketConfig.java
                        └── MCPWebSocketHandler.java
```

## How to Run

1. Make sure you have JDK 17+ and Maven installed.
2. In the `backend` directory, run:
   ```bash
   mvn spring-boot:run
   ```
3. The WebSocket server will start at `ws://localhost:8080/chat`.

## Usage
- Connect your front-end chat client to `ws://localhost:8080/chat`.
- Send commands like `campaign`, `segment`, `channel`, `strategy` to get simulated responses.

## Dependencies
- spring-boot-starter-web
- spring-boot-starter-websocket
- jackson-databind

---
For more details, see the Java source files in `src/main/java/com/example/mcp/`. 