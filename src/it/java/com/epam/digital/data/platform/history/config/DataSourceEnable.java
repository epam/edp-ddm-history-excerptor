package com.epam.digital.data.platform.history.config;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@AutoConfigureEmbeddedDatabase(provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY)
@EnableAutoConfiguration
@EnableJdbcRepositories
public @interface DataSourceEnable {

}
