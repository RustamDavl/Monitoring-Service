package ru.rstdv.monitoringservice.util;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.experimental.UtilityClass;

import java.sql.Connection;

@UtilityClass
public class LiquibaseUtil {

    private ConnectionProvider connectionProvider;
    private Liquibase liquibase;
    public void start(ConnectionProvider connectionProvider) {
        try {
            Connection connection = connectionProvider.getConnection();
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            liquibase =
                    new Liquibase("db/changelog/db.changelog-master.yaml", new ClassLoaderResourceAccessor(), database);

            liquibase.update();
            System.out.println("Migration completed!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dropAll() {
        try {
            liquibase.dropAll();
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }
}
