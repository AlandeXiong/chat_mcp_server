package com.example.mcp.server;

import java.util.Map;

/**
 * User Intention
 *
 */
public class UserIntentAnalysis {
    
    private ConversationContext.UserIntent intent;
    private double confidence;
    private Map<String, Object> extractedParams;
    private boolean requiresMoreInfo;
    private String nextQuestion;
    
    public UserIntentAnalysis(ConversationContext.UserIntent intent, double confidence, 
                            Map<String, Object> extractedParams, boolean requiresMoreInfo, String nextQuestion) {
        this.intent = intent;
        this.confidence = confidence;
        this.extractedParams = extractedParams;
        this.requiresMoreInfo = requiresMoreInfo;
        this.nextQuestion = nextQuestion;
    }
    
    // Getters
    public ConversationContext.UserIntent getIntent() { return intent; }
    public double getConfidence() { return confidence; }
    public Map<String, Object> getExtractedParams() { return extractedParams; }
    public boolean isRequiresMoreInfo() { return requiresMoreInfo; }
    public String getNextQuestion() { return nextQuestion; }
    
    // Setters
    public void setIntent(ConversationContext.UserIntent intent) { this.intent = intent; }
    public void setConfidence(double confidence) { this.confidence = confidence; }
    public void setExtractedParams(Map<String, Object> extractedParams) { this.extractedParams = extractedParams; }
    public void setRequiresMoreInfo(boolean requiresMoreInfo) { this.requiresMoreInfo = requiresMoreInfo; }
    public void setNextQuestion(String nextQuestion) { this.nextQuestion = nextQuestion; }
    
    @Override
    public String toString() {
        return String.format("UserIntentAnalysis{intent=%s, confidence=%.2f, requiresMoreInfo=%s}", 
            intent, confidence, requiresMoreInfo);
    }
}
