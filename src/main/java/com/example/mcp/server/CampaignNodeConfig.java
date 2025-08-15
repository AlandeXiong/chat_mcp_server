package com.example.mcp.server;

import java.util.List;
import java.util.Map;

/**
 * Marketing Campaign Node Configuration Classes
 * Based on frontend panel design, defines configuration parameters for various nodes
 */
public class CampaignNodeConfig {
    
    /**
     * Target Segment Node Configuration
     */
    public static class SegmentConfig {
        private String ageGroup;           // Age group
        private String location;           // Geographic location
        private String occupation;         // Occupation
        private String needs;              // Needs
        private String interests;          // Interests
        private String behavior;           // Behavioral characteristics
        private Map<String, Object> customAttributes; // Custom attributes
        
        // Getters and Setters
        public String getAgeGroup() { return ageGroup; }
        public void setAgeGroup(String ageGroup) { this.ageGroup = ageGroup; }
        
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
        
        public String getOccupation() { return occupation; }
        public void setOccupation(String occupation) { this.occupation = occupation; }
        
        public String getNeeds() { return needs; }
        public void setNeeds(String needs) { this.needs = needs; }
        
        public String getInterests() { return interests; }
        public void setInterests(String interests) { this.interests = interests; }
        
        public String getBehavior() { return behavior; }
        public void setBehavior(String behavior) { this.behavior = behavior; }
        
        public Map<String, Object> getCustomAttributes() { return customAttributes; }
        public void setCustomAttributes(Map<String, Object> customAttributes) { this.customAttributes = customAttributes; }
    }
    
    /**
     * Delivery Strategy Node Configuration
     */
    public static class StrategyConfig {
        private List<String> channels;     // Delivery channels
        private Integer frequency;         // Delivery frequency (per week)
        private Integer budgetAllocation;  // Budget allocation percentage
        private String timing;             // Delivery timing
        private String optimizationGoal;   // Optimization goal
        private Map<String, Object> channelSettings; // Channel-specific settings
        
        // Getters and Setters
        public List<String> getChannels() { return channels; }
        public void setChannels(List<String> channels) { this.channels = channels; }
        
        public Integer getFrequency() { return frequency; }
        public void setFrequency(Integer frequency) { this.frequency = frequency; }
        
        public Integer getBudgetAllocation() { return budgetAllocation; }
        public void setBudgetAllocation(Integer budgetAllocation) { this.budgetAllocation = budgetAllocation; }
        
        public String getTiming() { return timing; }
        public void setTiming(String timing) { this.timing = timing; }
        
        public String getOptimizationGoal() { return optimizationGoal; }
        public void setOptimizationGoal(String optimizationGoal) { this.optimizationGoal = optimizationGoal; }
        
        public Map<String, Object> getChannelSettings() { return channelSettings; }
        public void setChannelSettings(Map<String, Object> channelSettings) { this.channelSettings = channelSettings; }
    }
    
    /**
     * Email Template Node Configuration
     */
    public static class EmailTemplateConfig {
        private String subject;            // Email subject
        private String body;               // Email body
        private String cta;                // Call to action
        private String senderName;         // Sender name
        private String senderEmail;        // Sender email
        private String templateType;       // Template type
        private Map<String, Object> personalization; // Personalization fields
        
        // Getters and Setters
        public String getSubject() { return subject; }
        public void setSubject(String subject) { this.subject = subject; }
        
        public String getBody() { return body; }
        public void setBody(String body) { this.body = body; }
        
        public String getCta() { return cta; }
        public void setCta(String cta) { this.cta = cta; }
        
        public String getSenderName() { return senderName; }
        public void setSenderName(String senderName) { this.senderName = senderName; }
        
        public String getSenderEmail() { return senderEmail; }
        public void setSenderEmail(String senderEmail) { this.senderEmail = senderEmail; }
        
        public String getTemplateType() { return templateType; }
        public void setTemplateType(String templateType) { this.templateType = templateType; }
        
        public Map<String, Object> getPersonalization() { return personalization; }
        public void setPersonalization(Map<String, Object> personalization) { this.personalization = personalization; }
    }
    
    /**
     * Condition Judgment Node Configuration
     */
    public static class ConditionConfig {
        private String name;               // Condition name
        private String conditionType;      // Condition type
        private List<FlowPath> flowPaths;  // Flow paths
        private String description;        // Condition description
        private Map<String, Object> conditionLogic; // Condition logic
        
        public static class FlowPath {
            private Integer id;
            private String name;
            private String condition;
            private String targetType;
            private String targetNodeId;
            
            // Getters and Setters
            public Integer getId() { return id; }
            public void setId(Integer id) { this.id = id; }
            
            public String getName() { return name; }
            public void setName(String name) { this.name = name; }
            
            public String getCondition() { return condition; }
            public void setCondition(String condition) { this.condition = condition; }
            
            public String getTargetType() { return targetType; }
            public void setTargetType(String targetType) { this.targetType = targetType; }
            
            public String getTargetNodeId() { return targetNodeId; }
            public void setTargetNodeId(String targetNodeId) { this.targetNodeId = targetNodeId; }
        }
        
        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getConditionType() { return conditionType; }
        public void setConditionType(String conditionType) { this.conditionType = conditionType; }
        
        public List<FlowPath> getFlowPaths() { return flowPaths; }
        public void setFlowPaths(List<FlowPath> flowPaths) { this.flowPaths = flowPaths; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public Map<String, Object> getConditionLogic() { return conditionLogic; }
        public void setConditionLogic(Map<String, Object> conditionLogic) { this.conditionLogic = conditionLogic; }
    }
    
    /**
     * Customer Journey Node Configuration
     */
    public static class CustomerJourneyConfig {
        private String journeyName;        // Journey name
        private String description;        // Journey description
        private List<String> touchpoints;  // Touchpoints
        private String duration;           // Journey duration
        private String goal;               // Journey goal
        private Map<String, Object> journeyMap; // Journey map
        
        // Getters and Setters
        public String getJourneyName() { return journeyName; }
        public void setJourneyName(String journeyName) { this.journeyName = journeyName; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public List<String> getTouchpoints() { return touchpoints; }
        public void setTouchpoints(List<String> touchpoints) { this.touchpoints = touchpoints; }
        
        public String getDuration() { return duration; }
        public void setDuration(String duration) { this.duration = duration; }
        
        public String getGoal() { return goal; }
        public void setGoal(String goal) { this.goal = goal; }
        
        public Map<String, Object> getJourneyMap() { return journeyMap; }
        public void setJourneyMap(Map<String, Object> journeyMap) { this.journeyMap = journeyMap; }
    }
}
