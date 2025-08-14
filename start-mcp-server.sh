#!/bin/bash

# Start MCP Server with stdio transport
echo "Starting MCP Server with stdio transport..."

# Build the project
mvn clean package -DskipTests

# Run the server
java -jar target/mcp-backend-1.0.0.jar \
  --spring.ai.mcp.server.enabled=true \
  --spring.ai.mcp.server.transport=stdio \
  --spring.ai.mcp.server.stdio.enabled=true \
  --spring.ai.model.tool.enabled=false \
  --spring.security.enabled=false \
  --spring.security.basic.enabled=false \
  --management.security.enabled=false \
  --management.endpoints.web.base-path=/actuator \
  --springdoc.api-docs.enabled=true \
  --springdoc.swagger-ui.enabled=true \
  --logging.level.org.springframework.ai.mcp=DEBUG \
  --logging.level.org.springframework.ai=DEBUG \
  --logging.level.org.springframework.security=OFF \
  --logging.level.org.springframework.boot.actuate=OFF

echo "MCP Server started with stdio transport"
echo "Swagger UI available at: http://localhost:8088/swagger-ui.html"
echo "API Docs available at: http://localhost:8088/v3/api-docs"
