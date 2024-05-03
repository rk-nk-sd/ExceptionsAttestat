package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println("Введите данные в следующем формате: Фамилия Имя Отчество дата_рождения номер_телефона пол");
        Scanner inputData = new Scanner(System.in);
        String data = null;
        data = inputData.nextLine();
        if (!data.isBlank()) {
            try {
                String[] arr = data.split(" ");
                if (arr.length != 6) throw new IllegalArgumentException(String.format("Вы ввели не корректные данные %s", Arrays.toString(arr)));
                parseDate(arr[3], Arrays.asList("dd.MM.yyyy"));
                parsePhone(arr[4]);
                checkSex(arr[5]);
                System.out.println(Arrays.toString(arr));
                save(arr);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Вы не ввели данные, программа ничего не выполняя завершила свою работу");
        }

    }

    public static LocalDate parseDate(String dateString, List<String> formatStrings) {
        for (String formatString : formatStrings) {
            // TODO по хорошему надо учитывать временную зону по UTC - пока забил на это
            LocalDate now = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatString);
            String formattedDateNow = now.format(formatter);
            LocalDate parsedDateNow = LocalDate.parse(formattedDateNow, formatter);
            System.out.println(parsedDateNow);

            LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern(formatString));
            String formattedDate = date.format(formatter);
            LocalDate parsedDate = LocalDate.parse(formattedDate, formatter);
            if(parsedDate.isAfter(parsedDateNow)) throw new RuntimeException("Дата рождения указана не верно. Указана позднее текущей даты.");
            return date;
        }
        return null;
    }

    public static Long parsePhone(String phone) {
        if (phone.length() != 10) throw new NumberFormatException("Номер телефона должен состоять из 10 цифр");
        try{
            return Long.parseLong(phone);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Номер телефона должен содержать цифры");
        }
    }

    public static Boolean checkSex(String sex) {
        if (!sex.equals("f") & !sex.equals("m")) throw new IllegalArgumentException("Неверно указан пол. Ожидаемое значение f или m");
        return Boolean.TRUE;
    }

    public static void save(String[] data) {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(data[0], true))) {
            bw.write(String.join(" ", data));
            bw.write("\n");
            bw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
