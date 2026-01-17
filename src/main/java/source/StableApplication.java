package source;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StableApplication {

    public static void main(String[] args) {
        SpringApplication.run(StableApplication.class, args);
    }

    @Bean
    public StableManager stableManager() {
        StableManager manager = new StableManager();

        if (manager.getAll().isEmpty()) {
            DataGenerator.getInstance().generateSampleData(manager);
        }

        return manager;
    }
}