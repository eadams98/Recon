package com.idea.recon.config;

import org.apache.commons.lang.StringUtils;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LowercasePhysicalNamingStrategy implements PhysicalNamingStrategy {
	
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Identifier toPhysicalCatalogName(Identifier identifier, JdbcEnvironment jdbcEnv) {
        return convertToLowerCase(identifier);
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier identifier, JdbcEnvironment jdbcEnv) {
        return convertToLowerCase(identifier);
    }

    @Override
    public Identifier toPhysicalTableName(Identifier identifier, JdbcEnvironment jdbcEnv) {
        return convertToLowerCase(identifier);
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier identifier, JdbcEnvironment jdbcEnv) {
        return convertToLowerCase(identifier);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier identifier, JdbcEnvironment jdbcEnv) {
        return identifier;
    }

    private Identifier convertToLowerCase(Identifier identifier) {
        if (identifier == null || StringUtils.isBlank(identifier.getText())) {
            return identifier;
        }
        
    	logger.warn("Identifyer: " + identifier.getText());
        if ("Trainee".equals(identifier.getText())) {
        	
            return new Identifier("trainee", identifier.isQuoted());
        }
        return Identifier.toIdentifier(identifier.getText().toLowerCase());
    }
}
