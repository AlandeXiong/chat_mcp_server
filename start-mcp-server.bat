@echo off
echo Starting MCP Server with stdio transport...

REM Build the project
call mvn clean package -DskipTests

REM Run the server
java -jar target\mcp-backend-1.0.0.jar ^
  --spring.ai.mcp.server.enabled=true ^
  --spring.ai.mcp.server.transport=stdio ^
  --spring.ai.mcp.server.stdio.enabled=true ^
  --spring.ai.model.tool.enabled=false ^
  --spring.security.enabled=false ^
  --spring.security.basic.enabled=false ^
  --management.security.enabled=false ^
  --management.endpoints.web.base-path=/actuator ^
  --logging.level.org.springframework.ai.mcp=DEBUG ^
  --logging.level.org.springframework.ai=DEBUG ^
  --logging.level.org.springframework.security=OFF ^
  --logging.level.org.springframework.boot.actuator=OFF

echo MCP Server started with stdio transport
pause
