package ru.rstdv.monitoringservice.intergration.util;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.experimental.UtilityClass;
import java.sql.Connection;


@UtilityClass
public class LiquibaseTestUtil {

    public void start(Connection connection) {
        try {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase =
                    new Liquibase("db/changelog/db.changelog-master.yaml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            System.out.println("Migration completed!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
