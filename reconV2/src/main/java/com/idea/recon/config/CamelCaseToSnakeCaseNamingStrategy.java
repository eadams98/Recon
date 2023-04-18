package com.idea.recon.config;

import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.ImplicitNamingStrategy;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.hibernate.boot.model.naming.ImplicitForeignKeyNameSource;
import org.hibernate.boot.model.naming.ImplicitBasicColumnNameSource;
import org.hibernate.boot.model.naming.ImplicitIndexNameSource;
import org.hibernate.boot.model.naming.ImplicitJoinColumnNameSource;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;

@Component
public class CamelCaseToSnakeCaseNamingStrategy extends ImplicitNamingStrategyJpaCompliantImpl {

    @Override
    public Identifier determineBasicColumnName(ImplicitBasicColumnNameSource source) {
        return toSnakeCase(super.determineBasicColumnName(source));
    }

    @Override
    public Identifier determineJoinColumnName(ImplicitJoinColumnNameSource source) {
        return toSnakeCase(super.determineJoinColumnName(source));
    }

    @Override
    public Identifier determineForeignKeyName(ImplicitForeignKeyNameSource source) {
        return toSnakeCase(super.determineForeignKeyName(source));
    }

    @Override
    public Identifier determineIndexName(ImplicitIndexNameSource source) {
        return toSnakeCase(super.determineIndexName(source));
    }

    private Identifier toSnakeCase(Identifier identifier) {
        if (identifier == null) {
            return null;
        }

        String name = identifier.getText();
        String regex = "([a-z])([A-Z])";
        String replacement = "$1_$2";

        return Identifier.toIdentifier(name.replaceAll(regex, replacement).toLowerCase());
    }
}
