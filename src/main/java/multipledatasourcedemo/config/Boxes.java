package multipledatasourcedemo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Configuration
@Component
@ConfigurationProperties(prefix = "datasource")
@Data
public class Boxes {
    private List<String> boxes;
    private List<String> accUrl;
    private List<String> invUrl;
    private String username;
    private String password;
}
