package ai.aliz.jarvis.context;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;
import lombok.With;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import static ai.aliz.jarvis.util.JarvisConstants.CONTEXT_TYPE;
import static ai.aliz.jarvis.util.JarvisConstants.ID;
import static ai.aliz.jarvis.util.JarvisConstants.PARAMETERS;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder
@JsonDeserialize(builder = JarvisContext.JarvisContextBuilder.class)
public class JarvisContext {
    
    @JsonProperty(ID)
    private final String id;
    
    @JsonProperty(CONTEXT_TYPE)
    private final JarvisContextType contextType;
    
    
    @JsonProperty(PARAMETERS)
    private Map<String, String> parameters;
    
    @JsonPOJOBuilder(withPrefix = "")
    public static class JarvisContextBuilder {}
    
    public Map<String, String> getParameters() {
        return new HashMap<>(parameters);
    }
    
    public JarvisContext putParameter(String key, String value) {
        this.parameters.put(key,value);
        return this;
    }
    
    public JarvisContext putAllParameters(Map<String, String> newParameters) {
        this.parameters.putAll(newParameters);
        return this;
    }
    
    public String getParameter(String paramName) {
        return parameters.get(paramName);
    }
}
