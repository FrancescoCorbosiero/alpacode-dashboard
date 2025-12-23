package studio.alpacode.app.config;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Flyway configuration to ensure migrations run before any repository access.
 */
@Configuration
public class FlywayConfig {

    private static final Logger log = LoggerFactory.getLogger(FlywayConfig.class);

    /**
     * Custom migration strategy with logging.
     */
    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> {
            log.info("========================================");
            log.info("Flyway Migration Starting...");
            log.info("Locations: {}", Arrays.toString(flyway.getConfiguration().getLocations()));
            log.info("========================================");

            var result = flyway.migrate();

            log.info("========================================");
            log.info("Flyway Migration Complete!");
            log.info("Migrations executed: {}", result.migrationsExecuted);
            log.info("Schema version: {}", result.targetSchemaVersion);
            log.info("========================================");
        };
    }
}
