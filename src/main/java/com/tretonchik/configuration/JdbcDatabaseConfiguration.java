package com.tretonchik.configuration;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class JdbcDatabaseConfiguration implements DatabaseConfiguration {
    private final ConnectionSource connectionSource;

    public JdbcDatabaseConfiguration(String jdbcConnectionString) throws SQLException {
        connectionSource = new JdbcConnectionSource(jdbcConnectionString);
    }

    @Override
    public ConnectionSource connectionSource() {
        return connectionSource;
    }
}
