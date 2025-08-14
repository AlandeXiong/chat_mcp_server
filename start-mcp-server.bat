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
  --logging.level.org.springframework.ai.mcp=DEBUG ^
  --logging.level.org.springframework.ai=DEBUG

echo MCP Server started with stdio transport
pause
