package ru.rstdv.monitoringservice.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rstdv.monitoringservice.dto.filter.MonthFilter;
import ru.rstdv.monitoringservice.entity.embeddable.MeterReadingDate;
import ru.rstdv.monitoringservice.entity.WaterMeterReading;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Year;
import java.util.*;

@Component
@RequiredArgsConstructor
public class WaterMeterReadingRepositoryImpl implements MeterReadingRepository<WaterMeterReading> {

    private final DataSource dataSource;
    private static final String SAVE_SQL = """
            INSERT INTO water_meter_reading(user_id, cold_water, hot_water, meter_reading_year, meter_reading_month, meter_reading_day)
            VALUES (?, ?, ?, ?, ?, ?);
            """;

    private static final String FIND_ACTUAL_BY_USER_ID_SQL = """
            SELECT id, user_id, cold_water,hot_water, meter_reading_year, meter_reading_month, meter_reading_day
            FROM water_meter_reading
            WHERE user_id = ?
            ORDER BY meter_reading_month DESC 
            LIMIT 1
            """;

    private static final String FIND_ALL_BY_USER_ID_SQL = """
            SELECT id, user_id, cold_water,hot_water, meter_reading_year, meter_reading_month, meter_reading_day
            FROM water_meter_reading
            WHERE user_id = ?;
            """;

    private static final String FIND_BY_DATE_AND_USER_ID_SQL = """
            SELECT id, user_id, cold_water,hot_water, meter_reading_year, meter_reading_month, meter_reading_day
            FROM water_meter_reading
            WHERE user_id = ? AND meter_reading_month = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT id, user_id, cold_water,hot_water, meter_reading_year, meter_reading_month, meter_reading_day
            FROM water_meter_reading
            """;

    @Override
    public WaterMeterReading save(WaterMeterReading waterMeterReading) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, waterMeterReading.getUserId());
            preparedStatement.setInt(2, waterMeterReading.getColdWater());
            preparedStatement.setInt(3, waterMeterReading.getHotWater());
            preparedStatement.setString(4, waterMeterReading.getMeterReadingDate().getYear().toString());
            preparedStatement.setInt(5, waterMeterReading.getMeterReadingDate().getMonth());
            preparedStatement.setInt(6, waterMeterReading.getMeterReadingDate().getMonthDay());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                waterMeterReading.setId(generatedKeys.getLong("id"));
            }
            return waterMeterReading;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<WaterMeterReading> findActualByUserId(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ACTUAL_BY_USER_ID_SQL)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(
                        WaterMeterReading.builder()
                                .id(resultSet.getLong("id"))
                                .userId(resultSet.getLong("user_id"))
                                .coldWater(resultSet.getInt("cold_water"))
                                .hotWater(resultSet.getInt("hot_water"))
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
    public List<WaterMeterReading> findAllByUserId(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BY_USER_ID_SQL)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<WaterMeterReading> waterMeterReadings = new ArrayList<>();
            while (resultSet.next()) {
                waterMeterReadings.add(
                        WaterMeterReading.builder()
                                .id(resultSet.getLong("id"))
                                .userId(resultSet.getLong("user_id"))
                                .coldWater(resultSet.getInt("cold_water"))
                                .hotWater(resultSet.getInt("hot_water"))
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
            return waterMeterReadings;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<WaterMeterReading> findByMonthAndUserId(MonthFilter monthFilter, Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_DATE_AND_USER_ID_SQL)) {
            preparedStatement.setLong(1, id);
            preparedStatement.setInt(2, monthFilter.getMonthNumber());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(
                        WaterMeterReading.builder()
                                .id(resultSet.getLong("id"))
                                .userId(resultSet.getLong("user_id"))
                                .coldWater(resultSet.getInt("cold_water"))
                                .hotWater(resultSet.getInt("hot_water"))
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
    public List<WaterMeterReading> findAll() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<WaterMeterReading> waterMeterReadings = new ArrayList<>();
            while (resultSet.next()) {
                waterMeterReadings.add(
                        WaterMeterReading.builder()
                                .id(resultSet.getLong("id"))
                                .userId(resultSet.getLong("user_id"))
                                .coldWater(resultSet.getInt("cold_water"))
                                .hotWater(resultSet.getInt("hot_water"))
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
            return waterMeterReadings;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
