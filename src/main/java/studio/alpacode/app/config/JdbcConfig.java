package studio.alpacode.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;

@Configuration
public class JdbcConfig {

    @Bean
    public NamingStrategy namingStrategy() {
        return new NamingStrategy() {
            @Override
            public String getTableName(Class<?> type) {
                // Return APP_ prefix + uppercase table name to avoid reserved words
                return "APP_" + type.getSimpleName().toUpperCase();
            }

            @Override
            public String getColumnName(RelationalPersistentProperty property) {
                // Convert camelCase to UPPER_SNAKE_CASE
                return toUpperSnakeCase(property.getName());
            }

            private String toUpperSnakeCase(String name) {
                StringBuilder result = new StringBuilder();
                for (int i = 0; i < name.length(); i++) {
                    char c = name.charAt(i);
                    if (Character.isUpperCase(c) && i > 0) {
                        result.append('_');
                    }
                    result.append(Character.toUpperCase(c));
                }
                return result.toString();
            }
        };
    }
}
