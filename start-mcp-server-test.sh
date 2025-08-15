#!/bin/bash

echo "Starting MCP Server for Testing..."
echo "=================================="

# Set environment variables for testing
export SPRING_AI_MCP_SERVER_ENABLED=true
export SPRING_AI_MCP_CLIENT_ENABLED=false
export SPRING_AI_MODEL_AZURE_OPENAI_ENDPOINT=${AZURE_OPENAI_ENDPOINT}
export SPRING_AI_MODEL_AZURE_OPENAI_API_KEY=${AZURE_OPENAI_API_KEY}
export SPRING_AI_MODEL_AZURE_OPENAI_DEPLOYMENT_NAME=${AZURE_OPENAI_DEPLOYMENT_NAME}

echo "Environment Configuration:"
echo "  MCP Server Enabled: $SPRING_AI_MCP_SERVER_ENABLED"
echo "  MCP Client Enabled: $SPRING_AI_MCP_CLIENT_ENABLED"
echo "  Azure OpenAI Endpoint: $SPRING_AI_MODEL_AZURE_OPENAI_ENDPOINT"
echo "  Azure OpenAI Deployment: $SPRING_AI_MODEL_AZURE_OPENAI_DEPLOYMENT_NAME"
echo ""

# Check if required environment variables are set
if [ -z "$AZURE_OPENAI_ENDPOINT" ] || [ -z "$AZURE_OPENAI_API_KEY" ] || [ -z "$AZURE_OPENAI_DEPLOYMENT_NAME" ]; then
    echo "ERROR: Required Azure OpenAI environment variables are not set!"
    echo "Please set the following environment variables:"
    echo "  export AZURE_OPENAI_ENDPOINT='your-azure-openai-endpoint'"
    echo "  export AZURE_OPENAI_API_KEY='your-azure-openai-api-key'"
    echo "  export AZURE_OPENAI_DEPLOYMENT_NAME='your-deployment-name'"
    echo ""
    exit 1
fi

echo "Starting Spring Boot application..."
echo ""

# Start the application with MCP server enabled
java -jar target/mcp-backend-1.0.0.jar \
    --spring.ai.mcp.server.enabled=true \
    --spring.ai.mcp.client.enabled=false \
    --spring.ai.model.azure.openai.endpoint=$AZURE_OPENAI_ENDPOINT \
    --spring.ai.model.azure.openai.api-key=$AZURE_OPENAI_API_KEY \
    --spring.ai.model.azure.openai.deployment-name=$AZURE_OPENAI_DEPLOYMENT_NAME \
    --server.port=8088 \
    --logging.level.org.springframework.ai=INFO \
    --logging.level.com.example.mcp=DEBUG

echo ""
echo "MCP Server started successfully!"
echo "Test endpoints available at:"
echo "  - MCP Server Health: http://localhost:8088/api/mcp-server/health"
echo "  - MCP Server Tools: http://localhost:8088/api/mcp-server/tools"
echo "  - MCP Server Chat: http://localhost:8088/api/mcp-server/chat"
echo "  - Chat API: http://localhost:8088/api/chat/send"
echo "  - Campaign Advice: http://localhost:8088/api/chat/campaign-advice"
echo "  - Marketing Service Info: http://localhost:8088/api/chat/marketing-service-info"
echo "  - Swagger UI: http://localhost:8088/swagger-ui.html"
echo ""
echo "Testing the new ChatClient.Builder pattern:"
echo "  - ChatService now uses ChatClient.Builder for dependency injection"
echo "  - All AI calls use prompt().user().call().content() pattern"
echo ""
