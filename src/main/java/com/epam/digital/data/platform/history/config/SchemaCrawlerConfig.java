package com.epam.digital.data.platform.history.config;

import com.epam.digital.data.platform.history.config.properties.SchemaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import schemacrawler.inclusionrule.RegularExpressionInclusionRule;
import schemacrawler.schema.Catalog;
import schemacrawler.schema.Schema;
import schemacrawler.schemacrawler.LimitOptionsBuilder;
import schemacrawler.schemacrawler.LoadOptionsBuilder;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaCrawlerOptionsBuilder;
import schemacrawler.schemacrawler.SchemaInfoLevelBuilder;
import schemacrawler.utility.SchemaCrawlerUtility;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class SchemaCrawlerConfig {

  @Bean
  public LimitOptionsBuilder limitOptionsBuilder(SchemaProperties schemaProperties) {
    return LimitOptionsBuilder.builder()
            .includeSchemas(new RegularExpressionInclusionRule(schemaProperties.getName()));
  }

  @Bean
  public LoadOptionsBuilder loadOptionsBuilder() {
    return LoadOptionsBuilder.builder()
            .withSchemaInfoLevel(SchemaInfoLevelBuilder.standard());
  }

  @Bean
  public SchemaCrawlerOptions schemaCrawlerOptions(LoadOptionsBuilder loadOptionsBuilder,
                                                   LimitOptionsBuilder limitOptionsBuilder) {
    return SchemaCrawlerOptionsBuilder.newSchemaCrawlerOptions()
            .withLimitOptions(limitOptionsBuilder.toOptions())
            .withLoadOptions(loadOptionsBuilder.toOptions());
  }

  @Bean
  public Catalog catalog(DataSource dataSource, SchemaCrawlerOptions schemaCrawlerOptions)
      throws SQLException, SchemaCrawlerException {
    return SchemaCrawlerUtility.getCatalog(dataSource.getConnection(), schemaCrawlerOptions);
  }

  @Bean
  public Schema schema(Catalog catalog, SchemaProperties schemaProperties) {
    return catalog
        .lookupSchema(schemaProperties.getName())
        .orElseThrow(
            () -> new IllegalArgumentException("No such schema: " + schemaProperties.getName()));
  }
}
