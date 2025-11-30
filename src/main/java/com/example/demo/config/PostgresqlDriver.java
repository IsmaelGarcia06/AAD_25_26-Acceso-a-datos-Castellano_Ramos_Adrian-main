package com.example.demo.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PostgresqlDriver {
    private final String url;
    private final String username;
    private final String password;
    private final String driverClassName;

    private final ThreadLocal<Connection> transactionConnection = new ThreadLocal<>();

    @Value("classpath*:sql/ddl/*.sql")
    private Resource[] scripts;

    public PostgresqlDriver(
            @Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.username}") String username,
            @Value("${spring.datasource.password}") String password,
            @Value("${spring.datasource.driver-class-name:org.postgresql.Driver}") String driverClassName) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.driverClassName = driverClassName;

        try {
            Class.forName(this.driverClassName);
            log.info("JDBC driver loaded: {}", this.driverClassName);
        } catch (ClassNotFoundException e) {
            log.error("JDBC driver not found: {}", this.driverClassName, e);
            throw new RuntimeException("Driver not found", e);
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }


    @PostConstruct
    public void init() {
        log.info("Initializing database from SQL scripts...");

        if (scripts == null || scripts.length == 0) {
            log.warn("No SQL scripts found in classpath:sql/ddl/");
            return;
        }

        java.util.Arrays.sort(scripts, (r1, r2) -> {
            try {
                return r1.getFilename().compareTo(r2.getFilename());
            } catch (Exception e) {
                return 0;
            }
        });

        for (Resource script : scripts) {
            executeSql(script);
        }


        System.out.println("Database initialized from SQL scripts");
        log.info("Database initialized successfully!");
    }


    private void executeSql(Resource resource) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(resource.getInputStream()))) {

            String sql = reader.lines()
                    .filter(line -> !line.trim().startsWith("--")) // Filtrar comentarios
                    .collect(Collectors.joining("\n"));

            if (!sql.trim().isEmpty()) {
                stmt.execute(sql);
                log.info("Executed script: {}", resource.getFilename());
            }

        } catch (Exception e) {
            log.error("Error executing script {}: {}",
                    resource.getFilename(), e.getMessage(), e);
        }
    }

    public void beginTransaction() {
        try {
            Connection conn = transactionConnection.get();
            if (conn != null && !conn.isClosed()) {
                throw new IllegalStateException("Transaction already active in this thread");
            }

            conn = DriverManager.getConnection(url, username, password);
            conn.setAutoCommit(false);
            transactionConnection.set(conn);

            log.debug("Transaction started");

        } catch (SQLException e) {
            log.error("Error starting transaction", e);
            throw new RuntimeException("Error starting transaction: " + e.getMessage(), e);
        }
    }

    public void commit() {
        Connection conn = transactionConnection.get();

        if (conn == null) {
            throw new IllegalStateException("No active transaction to commit");
        }

        try {
            conn.commit();
            log.debug("Transaction committed");

        } catch (SQLException e) {
            log.error("Error committing transaction", e);
            throw new RuntimeException("Error committing transaction: " + e.getMessage(), e);

        } finally {
            closeTransactionConnection();
        }
    }

    public void rollback() {
        Connection conn = transactionConnection.get();

        if (conn == null) {
            log.warn("No active transaction to rollback");
            return;
        }

        try {
            conn.rollback();
            log.warn("Transaction rolled back");

        } catch (SQLException e) {
            log.error("Error rolling back transaction", e);

        } finally {
            closeTransactionConnection();
        }
    }


    private void closeTransactionConnection() {
        Connection conn = transactionConnection.get();

        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                log.error("Error closing transaction connection", e);
            } finally {
                transactionConnection.remove();
            }
        }
    }


    public Connection getTransactionConnection() {
        return transactionConnection.get();
    }
}