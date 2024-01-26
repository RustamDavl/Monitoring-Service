package ru.rstdv.monitoringservice.in;

import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateThermalMeterReadingDto;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateUserDto;
import ru.rstdv.monitoringservice.dto.createupdate.CreateUpdateWaterMeterReadingDto;
import ru.rstdv.monitoringservice.dto.filter.MonthFilter;
import ru.rstdv.monitoringservice.exception.UserNotFoundException;
import ru.rstdv.monitoringservice.service.ThermalMeterService;
import ru.rstdv.monitoringservice.service.UserServiceImpl;
import ru.rstdv.monitoringservice.service.WaterMeterService;

import java.util.Scanner;

public class ConsoleApplication {

    private final UserServiceImpl userServiceImpl = UserServiceImpl.getInstance();
    private final WaterMeterService waterMeterService = WaterMeterService.getInstance();
    private final ThermalMeterService thermalMeterService = ThermalMeterService.getInstance();

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
                var res = userServiceImpl.register(createUpdateUserDto);
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
                    var user = userServiceImpl.authenticate(email, password);
                    System.out.println();
                    System.out.println("Hello, " + user.firstname() + " !");
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
                            var res = waterMeterService.create(createUpdateWaterMeterReadingDto);
                            System.out.println(res);

                        } else if (answer.equals("2")) {
                            System.out.println("Введите значение в Гкал");
                            var value = scanner.nextLine();
                            CreateUpdateThermalMeterReadingDto createUpdateThermalMeterReadingDto = new CreateUpdateThermalMeterReadingDto(user.id(), value);
                            var res = thermalMeterService.create(createUpdateThermalMeterReadingDto);
                            System.out.println(res);
                        } else if (answer.equals("3")) {
                            var res = thermalMeterService.getActual();
                            System.out.println(res);
                        } else if (answer.equals("4")) {
                            var res = waterMeterService.getActual();
                            System.out.println(res);
                        }else if (answer.equals("5")) {
                            var res = thermalMeterService.getAll();
                            System.out.println(res);
                        } else if (answer.equals("6")) {
                            var res = waterMeterService.getActual();
                            System.out.println(res);
                        }else if (answer.equals("7")) {
                            System.out.println("Введите номер месяца");
                            var monthValue = scanner.nextLine();
                            var res = thermalMeterService.getByFilter(new MonthFilter(Integer.valueOf(monthValue)));
                            System.out.println(res);
                        }else if (answer.equals("8")) {
                            System.out.println("Введите номер месяца");
                            var monthValue = scanner.nextLine();
//                            var res = waterMeterService.getByFilter(new MonthFilter(Integer.valueOf(monthValue)));
//                            System.out.println(res);
                        }

                    } while (!answer.equalsIgnoreCase("Q"));

                } catch (UserNotFoundException e) {
                    String message = e.getMessage();
                    System.out.println("Oops... " + message);
                }


            }
        }
    }
}
