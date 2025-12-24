package studio.alpacode.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;

/**
 * JDBC configuration for Spring Data JDBC.
 *
 * <p>Configures:
 * <ul>
 *   <li>Table naming: app_* prefix with snake_case (e.g., User -> app_user)</li>
 *   <li>Column naming: camelCase -> snake_case (e.g., invoiceDate -> invoice_date)</li>
 *   <li>Auditing: automatic created_at/updated_at handling</li>
 * </ul>
 */
@Configuration
@EnableJdbcAuditing
public class JdbcConfig {

    @Bean
    public NamingStrategy namingStrategy() {
        return new NamingStrategy() {
            @Override
            public String getTableName(Class<?> type) {
                // app_ prefix + snake_case (e.g., User -> app_user, Invoice -> app_invoice)
                return "app_" + toSnakeCase(type.getSimpleName());
            }

            @Override
            public String getColumnName(RelationalPersistentProperty property) {
                // camelCase -> snake_case (e.g., invoiceDate -> invoice_date)
                return toSnakeCase(property.getName());
            }

            private String toSnakeCase(String name) {
                StringBuilder result = new StringBuilder();
                for (int i = 0; i < name.length(); i++) {
                    char c = name.charAt(i);
                    if (Character.isUpperCase(c) && i > 0) {
                        result.append('_');
                    }
                    result.append(Character.toLowerCase(c));
                }
                return result.toString();
            }
        };
    }
}
