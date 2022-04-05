package ai.aliz.jarvis.context;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.CommandLinePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.stereotype.Component;

import static ai.aliz.jarvis.util.JarvisConstants.BASE_PARAMETERS;
import static ai.aliz.jarvis.util.JarvisConstants.CONTEXT;


@Component
@Slf4j
public class JarvisContextLoader {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Getter
    private Map<String, JarvisContext> contextIdToContexts;
    
    @Autowired
    public JarvisContextLoader(ConfigurableEnvironment environment) {
        String contextPath = environment.getProperty(CONTEXT);
        contextIdToContexts = parseContexts(contextPath,environment).stream().collect(Collectors.toMap(JarvisContext::getId, Function.identity()));
    }
    
    public JarvisContext getContext(String contextId) {
        JarvisContext context = contextIdToContexts.get(contextId);
        if (Objects.isNull(context)) {
            throw new IllegalStateException("Could not find context with id " + contextId);
        }
        return context;
    }
    
    @SneakyThrows
    private Set<JarvisContext> parseContexts(String contextPath, ConfigurableEnvironment environment) {
        
        log.info("Loading jarvis context from: {}", contextPath);
        TypeReference<Set<JarvisContext>> typeReference = new TypeReference<Set<JarvisContext>>() {};
        
        Set<JarvisContext> contexts = objectMapper.readValue(Files.asCharSource(new File(contextPath), StandardCharsets.UTF_8).read(), typeReference);
        log.info("Jarvis context loaded: {}", contexts);
        validateContexts(contexts);
        
        SimpleCommandLinePropertySource propertySource =(SimpleCommandLinePropertySource) environment.getPropertySources().get(CommandLinePropertySource.COMMAND_LINE_PROPERTY_SOURCE_NAME);
        
        Map<String,String> additionalParameters = Arrays.stream(propertySource.getPropertyNames())
              .filter(n->!BASE_PARAMETERS.contains(n))
              .collect(Collectors.toMap(Function.identity(), n -> propertySource.getProperty(n)));
        
        contexts.forEach(c->c.putAllParameters(additionalParameters));
        
        return contexts;
    }
    
    private void validateContexts(Set<JarvisContext> contexts) {
        String errors = contexts.stream()
                                .filter(context -> !context.getParameters().keySet().containsAll(context.getContextType().getRequiredParameters()))
                                .map(context -> context.toString() + " is missing parameters. " +
                                        "Required parameters for this context type: " + String.join(",", context.getContextType().getRequiredParameters()) + "."
                                )
                                .collect(Collectors.joining("\n"));
        if (!errors.isEmpty()) {
            throw new IllegalStateException(errors);
        }
    }
}
