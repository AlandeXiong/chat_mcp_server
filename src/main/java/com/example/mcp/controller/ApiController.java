package com.example.mcp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Campaign Journey API", description = "Marketing campaign management endpoints")
@CrossOrigin(origins = "*") // Allow ReactFlow frontend to connect
public class ApiController {

    @GetMapping("/health")
    @Operation(summary = "Health Check", description = "Check if the MCP server is running")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Server is healthy"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    public Map<String, Object> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Campaign Journey MCP Server");
        response.put("version", "1.0.0");
        response.put("transport", "stdio");
        return response;
    }

    @GetMapping("/campaigns")
    @Operation(summary = "Get Campaigns", description = "Retrieve list of marketing campaigns")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved campaigns"),
        @ApiResponse(responseCode = "404", description = "No campaigns found")
    })
    public Map<String, Object> getCampaigns() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Campaigns retrieved successfully");
        response.put("count", 0);
        response.put("data", new String[]{});
        return response;
    }

    @PostMapping("/campaigns")
    @Operation(summary = "Create Campaign", description = "Create a new marketing campaign")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Campaign created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid campaign data")
    })
    public Map<String, Object> createCampaign(
        @Parameter(description = "Campaign name", required = true) @RequestParam String name,
        @Parameter(description = "Campaign description") @RequestParam(required = false) String description
    ) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Campaign created successfully");
        response.put("name", name);
        response.put("description", description);
        response.put("id", "campaign_" + System.currentTimeMillis());
        return response;
    }

    @GetMapping("/mcp/status")
    @Operation(summary = "MCP Server Status", description = "Get MCP server status and configuration")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "MCP server status retrieved")
    })
    public Map<String, Object> getMcpStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("mcpServer", "enabled");
        response.put("transport", "stdio");
        response.put("aiModels", new String[]{"OpenAI", "Azure OpenAI"});
        response.put("features", new String[]{"ChatClient", "PromptTemplate", "Marketing Campaigns"});
        return response;
    }
}
