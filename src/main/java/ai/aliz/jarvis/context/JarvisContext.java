package ai.aliz.jarvis.context;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.collect.ImmutableMap;

import static ai.aliz.jarvis.util.JarvisConstants.CONTEXT_TYPE;
import static ai.aliz.jarvis.util.JarvisConstants.ID;
import static ai.aliz.jarvis.util.JarvisConstants.PARAMETERS;

@Value
@Builder
@JsonDeserialize(builder = JarvisContext.JarvisContextBuilder.class)
public class JarvisContext {
    
    @JsonProperty(ID)
    private String id;
    
    @JsonProperty(CONTEXT_TYPE)
    private JarvisContextType contextType;
    
    @Singular
    @JsonProperty(PARAMETERS)
    private Map<String, String> parameters;
    
    @JsonPOJOBuilder(withPrefix = "")
    public static class JarvisContextBuilder {}
    
    public ImmutableMap<String, String> getParameters() {
        return ImmutableMap.copyOf(parameters);
    }
    
    public String getParameter(String paramName) {
        return parameters.get(paramName);
    }
}
