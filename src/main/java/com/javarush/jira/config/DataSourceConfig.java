package com.javarush.jira.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    @Profile("!test") // PostgreSQL для боевого режима
    public DataSource postgresDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5432/jira")
                .username("jira")
                .password("JiraRush")
                .driverClassName("org.postgresql.Driver")
                .build();
    }

    @Bean
    @Profile("test") // H2 в памяти для тестов
    public DataSource h2DataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .addScript("classpath:/db/changelog.sql") // Подключаем схему
                .addScript("classpath:/data4dev/data.sql")   // Тестовые данные
                .build();
    }
}



//package com.javarush.jira.config;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class DataSourceConfig {
//
//    @Bean
//    @Profile("!test") // Основная база данных для продакшн/разработки
//    public DataSource postgresDataSource(DataSourceProperties properties) {
//        return properties.initializeDataSourceBuilder().build();
//    }
//
//    @Bean
//    @Profile("test") // H2 в памяти для тестов
//    public DataSource h2DataSource() {
//        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
//        builder.setType(EmbeddedDatabaseType.H2)
//                .setScriptEncoding("UTF-8")
//                .addScript("src/main/resources/data4dev/data.sql") // Схема БД
//                .addScript("src/main/resources/db/changelog.sql");  // Тестовые данные
//        return builder.build();
//    }
//}
//package com.javarush.jira.config;
//
//
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//
//import javax.sql.DataSource;
//
//
//@Configuration
//public class DataSourceConfig {
//    @Bean
//    @Profile("!test")
//    public DataSource postgresDataSource() {
//        return DataSourceBuilder.create()
//                .url("jdbc:postgresql://localhost:5432/jira")
//                .username("jira")
//                .password("JiraRush")
//                .driverClassName("org.postgresql.Driver")
//                .build();
//    }
//    @Bean
//    @Profile("test")
//    public DataSource h2DataSource() {
//        return DataSourceBuilder.create()
//                .url("jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1")
//                .username("jira")
//                .password("JiraRush")
//                .driverClassName("org.h2.Driver")
//                .build();
//    }



//    @Bean
//    @Profile("test")
//    public DataSource h2DataSource() {
//        try {
//            // Явно загрузить класс драйвера
//            Class.forName("org.h2.Driver");
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException("H2 Driver not found", e);
//        }
//
//        return DataSourceBuilder.create()
//                .url("jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1")
//                .username("jira")
//                .password("JiraRush")
////                .driverClassName("org.h2.Driver")
//                .build();
//    }
//}
//@Configuration
//public class DataSourceConfig {
//    // Удалим явное создание DataSource и полагаемся на автоконфигурацию Spring Boot
//}




