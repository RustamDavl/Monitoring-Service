package ru.rstdv.monitoringservice.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rstdv.monitoringservice.dto.filter.MonthFilter;
import ru.rstdv.monitoringservice.entity.embeddable.MeterReadingDate;


import ru.rstdv.monitoringservice.entity.ThermalMeterReading;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Year;
import java.util.*;
@Component
@RequiredArgsConstructor
public class ThermalMeterReadingRepositoryImpl implements MeterReadingRepository<ThermalMeterReading> {

    private final DataSource dataSource;

    private static final String SAVE_SQL = """
            INSERT INTO thermal_meter_reading(user_id, giga_calories, meter_reading_year, meter_reading_month, meter_reading_day)
            VALUES (?, ?, ?, ?, ?);
            """;

    private static final String FIND_ACTUAL_BY_USER_ID_SQL = """
            SELECT id, user_id, giga_calories, meter_reading_year, meter_reading_month, meter_reading_day
            FROM thermal_meter_reading
            WHERE user_id = ?
            ORDER BY meter_reading_month DESC 
            LIMIT 1
            """;

    private static final String FIND_ALL_BY_USER_ID_SQL = """
            SELECT id, user_id, giga_calories, meter_reading_year, meter_reading_month, meter_reading_day
            FROM thermal_meter_reading
            WHERE user_id = ?;
            """;

    private static final String FIND_BY_DATE_AND_USER_ID_SQL = """
            SELECT id, user_id, giga_calories, meter_reading_year, meter_reading_month, meter_reading_day
            FROM thermal_meter_reading
            WHERE user_id = ? AND meter_reading_month = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT id, user_id, giga_calories, meter_reading_year, meter_reading_month, meter_reading_day
            FROM thermal_meter_reading
            """;
    @Override
    public ThermalMeterReading save(ThermalMeterReading thermalMeterReading) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, thermalMeterReading.getUserId());
            preparedStatement.setFloat(2, thermalMeterReading.getGigaCalories());
            preparedStatement.setString(3, thermalMeterReading.getMeterReadingDate().getYear().toString());
            preparedStatement.setInt(4, thermalMeterReading.getMeterReadingDate().getMonth());
            preparedStatement.setInt(5, thermalMeterReading.getMeterReadingDate().getMonthDay());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                thermalMeterReading.setId(generatedKeys.getLong("id"));
            }
            return thermalMeterReading;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<ThermalMeterReading> findActualByUserId(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ACTUAL_BY_USER_ID_SQL)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(
                        ThermalMeterReading.builder()
                                .id(resultSet.getLong("id"))
                                .userId(resultSet.getLong("user_id"))
                                .gigaCalories(resultSet.getFloat("giga_calories"))
                                .meterReadingDate(
                                        MeterReadingDate.builder()
                                                .year(Year.parse(resultSet.getString("meter_reading_year")))
                                                .month(resultSet.getInt("meter_reading_month"))
                                                .monthDay(resultSet.getInt("meter_reading_day"))
                                                .build()
                                )
                                .build()
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<ThermalMeterReading> findAllByUserId(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BY_USER_ID_SQL)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<ThermalMeterReading> thermalMeterReadings = new ArrayList<>();
            while (resultSet.next()) {
                thermalMeterReadings.add(
                        ThermalMeterReading.builder()
                                .id(resultSet.getLong("id"))
                                .userId(resultSet.getLong("user_id"))
                                .gigaCalories(resultSet.getFloat("giga_calories"))
                                .meterReadingDate(
                                        MeterReadingDate.builder()
                                                .year(Year.parse(resultSet.getString("meter_reading_year")))
                                                .month(resultSet.getInt("meter_reading_month"))
                                                .monthDay(resultSet.getInt("meter_reading_day"))
                                                .build()
                                )
                                .build()
                );
            }
            return thermalMeterReadings;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<ThermalMeterReading> findByMonthAndUserId(MonthFilter monthFilter, Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_DATE_AND_USER_ID_SQL)) {
            preparedStatement.setLong(1, id);
            preparedStatement.setInt(2, monthFilter.getMonthNumber());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(
                        ThermalMeterReading.builder()
                                .id(resultSet.getLong("id"))
                                .userId(resultSet.getLong("user_id"))
                                .gigaCalories(resultSet.getFloat("giga_calories"))
                                .meterReadingDate(
                                        MeterReadingDate.builder()
                                                .year(Year.parse(resultSet.getString("meter_reading_year")))
                                                .month(resultSet.getInt("meter_reading_month"))
                                                .monthDay(resultSet.getInt("meter_reading_day"))
                                                .build()
                                )
                                .build()
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<ThermalMeterReading> findAll() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<ThermalMeterReading> thermalMeterReadings = new ArrayList<>();
            while (resultSet.next()) {
                thermalMeterReadings.add(
                        ThermalMeterReading.builder()
                                .id(resultSet.getLong("id"))
                                .userId(resultSet.getLong("user_id"))
                                .gigaCalories(resultSet.getFloat("giga_calories"))
                                .meterReadingDate(
                                        MeterReadingDate.builder()
                                                .year(Year.parse(resultSet.getString("meter_reading_year")))
                                                .month(resultSet.getInt("meter_reading_month"))
                                                .monthDay(resultSet.getInt("meter_reading_day"))
                                                .build()
                                )
                                .build()
                );
            }
            return thermalMeterReadings;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
