package ru.rstdv.monitoringservice.in;


import ru.rstdv.monitoringservice.dto.createupdate.CreateAuditDto;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateThermalMeterReadingDto;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateUserDto;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateWaterMeterReadingDto;
import ru.rstdv.monitoringservice.dto.filter.MonthFilterImpl;
import ru.rstdv.monitoringservice.dto.read.ReadThermalMeterReadingDto;
import ru.rstdv.monitoringservice.dto.read.ReadUserDto;
import ru.rstdv.monitoringservice.dto.read.ReadWaterMeterReadingDto;
import ru.rstdv.monitoringservice.entity.ThermalMeterReading;
import ru.rstdv.monitoringservice.entity.WaterMeterReading;
import ru.rstdv.monitoringservice.entity.embeddable.AuditAction;
import ru.rstdv.monitoringservice.entity.embeddable.Role;
import ru.rstdv.monitoringservice.exception.IncorrectMonthValueException;
import ru.rstdv.monitoringservice.exception.MeterReadingNotFoundException;
import ru.rstdv.monitoringservice.exception.UserNotFoundException;
import ru.rstdv.monitoringservice.factory.*;
import ru.rstdv.monitoringservice.mapper.*;
import ru.rstdv.monitoringservice.repository.*;
import ru.rstdv.monitoringservice.service.*;
import ru.rstdv.monitoringservice.util.CommonConnectionProvider;


import java.time.LocalDateTime;
import java.util.Scanner;

public class ConsoleApplication {
    private ServiceFactory serviceFactory;
    public ConsoleApplication() {
        serviceFactory = new ServiceFactoryImpl();
    }
    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Введите одно из значений : ");
            System.out.println();
            System.out.println("1. Регистрация");
            System.out.println("2. Авторизация");
            System.out.println("Q. Выход");
            String choice = scanner.nextLine();
            if (choice.equals("1")) {
                System.out.println("Для регистрации необходимо ввести следующие данные : ");

                System.out.println("Введите ваше имя : ");
                var firstname = scanner.nextLine();

                System.out.println("Введите адрес почты : ");
                var email = scanner.nextLine();
                System.out.println("Введите пароль : ");
                var password = scanner.nextLine();
                System.out.println("Введите лицевой счет : ");
                var personalAccount = scanner.nextLine();
                System.out.println("Введите город проживания : ");
                var city = scanner.nextLine();
                System.out.println("Введите улицу проживания : ");
                var street = scanner.nextLine();
                System.out.println("Введите номер дома : ");
                var houseNumber = scanner.nextLine();
                var createUpdateUserDto = new CreateUpdateUserDto(firstname, email, password, personalAccount, city, street, houseNumber);
                var res = serviceFactory.createUserService().register(createUpdateUserDto);
                System.out.println(res);
                System.out.println("registration success ! ");
                System.out.println();
            } else if (choice.equals("2")) {
                System.out.println("Для авторизации необходимо ввести следующие данные : ");
                System.out.println("Введите адрес почты : ");
                var email = scanner.nextLine();
                System.out.println("Введите пароль : ");
                var password = scanner.nextLine();
                try {
                    var user = serviceFactory.createUserService().authenticate(email, password);
                    System.out.println();
                    System.out.println("Hello, " + user.firstname() + " !");
                    if (user.role().equals(Role.ADMIN.name())) {
                        String answer = null;
                        do {
                            System.out.println("Выберите одно из действий : ");
                            System.out.println();
                            System.out.println("1. Вывести информацию о всех пользователях");
                            System.out.println("2. Вывести информацию о пользователе");
                            System.out.println("Q. Выход");
                            answer = scanner.nextLine();
                            if (answer.equals("1")) {
                                var users = serviceFactory.createUserService().findAll();
                                users.forEach(
                                        System.out::println
                                );
                            } else if (answer.equals("2")) {
                                System.out.println("Введите идентификатор пользователя");
                                var id = scanner.nextLine();

                                try {
                                    ReadUserDto readUserDto = readUserDto = serviceFactory.createUserService().findById(Long.valueOf(id));
                                    var audits = serviceFactory.createAuditService().findUserAudits(readUserDto.id());
                                    System.out.println("User : ");
                                    System.out.println(readUserDto);
                                    System.out.println("User audits");
                                    audits.forEach(System.out::println);
                                } catch (UserNotFoundException e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                        } while (!answer.equalsIgnoreCase("Q"));
                    } else if (user.role().equals("USER")) {
                        String answer = null;
                        do {
                            System.out.println("Выберите одно из действий : ");
                            System.out.println();
                            System.out.println("1. Подать показание счетчика воды");
                            System.out.println("2. Подать показание счетчика тепла");
                            System.out.println("3. Получить последнее показание счетчика тепла");
                            System.out.println("4. Получить последнее показание счетчика воды");
                            System.out.println("5. История показаний счетчика тепла");
                            System.out.println("6. История показаний счетчика воды");
                            System.out.println("7. Получить показание счетчика тепла за месяц");
                            System.out.println("8. Получить показание счетчика воды за месяц");
                            System.out.println("Q. Выход");
                            answer = scanner.nextLine();
                            if (answer.equals("1")) {
                                System.out.println("Введите значения в кубометрах");
                                System.out.println("холодной воды");
                                var coldValue = scanner.nextLine();
                                System.out.println("горячей воды");
                                var hotValue = scanner.nextLine();
                                CreateUpdateWaterMeterReadingDto createUpdateWaterMeterReadingDto = new CreateUpdateWaterMeterReadingDto(user.id(), coldValue, hotValue);
                                try {
                                    var res = serviceFactory.createWaterMeterReadingService().save(createUpdateWaterMeterReadingDto);
                                    System.out.println(res);
                                } catch (UserNotFoundException e) {
                                    System.out.println(e.getMessage());
                                }
                            } else if (answer.equals("2")) {
                                System.out.println("Введите значение в Гкал");
                                var value = scanner.nextLine();
                                CreateUpdateThermalMeterReadingDto createUpdateThermalMeterReadingDto = new CreateUpdateThermalMeterReadingDto(user.id(), value);
                                try {
                                    var res = serviceFactory.createThermalMeterReadingService().save(createUpdateThermalMeterReadingDto);
                                    System.out.println(res);
                                } catch (UserNotFoundException e) {
                                    System.out.println(e.getMessage());
                                }
                            } else if (answer.equals("3")) {
                                try {
                                    var res = serviceFactory.createThermalMeterReadingService().findActualByUserId(Long.valueOf(user.id()));
                                    System.out.println(res);
                                } catch (MeterReadingNotFoundException e) {
                                    System.out.println(e.getMessage());
                                }
                            } else if (answer.equals("4")) {
                                try {
                                    var res = serviceFactory.createWaterMeterReadingService().findActualByUserId(Long.valueOf(user.id()));
                                    System.out.println(res);
                                } catch (MeterReadingNotFoundException e) {
                                    System.out.println(e.getMessage());
                                }
                            } else if (answer.equals("5")) {
                                var res = serviceFactory.createThermalMeterReadingService().findAllByUserId(Long.valueOf(user.id()));
                                System.out.println(res);
                            } else if (answer.equals("6")) {
                                var res = serviceFactory.createWaterMeterReadingService().findActualByUserId(Long.valueOf(user.id()));
                                System.out.println(res);
                            } else if (answer.equals("7")) {
                                System.out.println("Введите номер месяца");
                                var monthValue = scanner.nextLine();
                                try {
                                    var res = serviceFactory.createThermalMeterReadingService().findByMonthAndUserId(new MonthFilterImpl(Integer.parseInt(monthValue)), Long.valueOf(user.id()));
                                    System.out.println(res);

                                } catch (MeterReadingNotFoundException | IncorrectMonthValueException e) {
                                    System.out.println(e.getMessage());
                                }
                            } else if (answer.equals("8")) {
                                System.out.println("Введите номер месяца");
                                var monthValue = scanner.nextLine();
                                try {
                                    var res = serviceFactory.createWaterMeterReadingService().findByMonthAndUserId(new MonthFilterImpl(Integer.parseInt(monthValue)), Long.valueOf(user.id()));
                                    System.out.println(res);

                                } catch (MeterReadingNotFoundException | IncorrectMonthValueException e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                        } while (!answer.equalsIgnoreCase("Q"));
                        serviceFactory.createAuditService().saveAudit(new CreateAuditDto(
                                user.id(),
                                AuditAction.LOGOUT.name(),
                                LocalDateTime.now(),
                                "user log out"
                        ));
                    }
                } catch (UserNotFoundException e) {
                    String message = e.getMessage();
                    System.out.println("Oops... " + message);
                }
            } else if (choice.equalsIgnoreCase("Q")) {
                break;

            } else {
                System.out.println("Неверная команда?");
            }
        }
        scanner.close();
    }
}
