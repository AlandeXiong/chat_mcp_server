package com.example.mcp.server;

import java.util.List;
import java.util.Map;

/**
 * Marketing Campaign Node Definition
 * Based on frontend design structure, defines various marketing campaign nodes
 */
public class CampaignNode {
    
    public enum NodeType {
        START,              // Start node
        SEGMENT,           // Target segment node
        STRATEGY,          // Delivery strategy node
        EMAIL_TEMPLATE,    // Email template node
        CONDITION,         // Condition judgment node
        CUSTOMER_JOURNEY,  // Customer journey node
        END                // End node
    }
    
    private String id;
    private NodeType type;
    private String name;
    private Map<String, Object> data;
    private List<String> connections;
    private NodeStatus status;
    
    public enum NodeStatus {
        DRAFT,      // Draft
        CONFIGURING, // Configuring
        READY,      // Ready
        ACTIVE,     // Active
        PAUSED,     // Paused
        COMPLETED   // Completed
    }
    
    // Constructors
    public CampaignNode() {}
    
    public CampaignNode(String id, NodeType type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.status = NodeStatus.DRAFT;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public NodeType getType() { return type; }
    public void setType(NodeType type) { this.type = type; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Map<String, Object> getData() { return data; }
    public void setData(Map<String, Object> data) { this.data = data; }
    
    public List<String> getConnections() { return connections; }
    public void setConnections(List<String> connections) { this.connections = connections; }
    
    public NodeStatus getStatus() { return status; }
    public void setStatus(NodeStatus status) { this.status = status; }
    
    @Override
    public String toString() {
        return String.format("CampaignNode{id='%s', type=%s, name='%s', status=%s}", 
            id, type, name, status);
    }
}
