package ru.netology.shop.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {
    private DataHelper() {
    }

    private static final Faker FAKER = new Faker(new Locale("en"));

    public static String validCardNumber() {
        return "1111 2222 3333 4444";
    }

    public static String generateValidCardMonth() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM");
        return formatter.format(date);
    }

    public static String generateCardPreviousMonth() {
        LocalDate date = LocalDate.now();
        date = date.minusMonths(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM");
        return formatter.format(date);
    }

    public static String generateCardMonthWithIncorrectFormat() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M");
        return formatter.format(date);
    }

    public static String generateValidCardYear() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy");
        return formatter.format(date);
    }

    public static String generateCardPreviousYear() {
        LocalDate date = LocalDate.now();
        date = date.minusYears(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy");
        return formatter.format(date);
    }

    public static String generateCardYearUpperLimit() {
        LocalDate date = LocalDate.now();
        date = date.plusYears(6);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy");
        return formatter.format(date);
    }

    public static String generateValidCardOwner() {
        return FAKER.name().firstName().toUpperCase() + " " + FAKER.name().lastName().toUpperCase();
    }

    public static String generateCardOwnerWithIncompleteData() {
        return FAKER.name().firstName().toUpperCase();
    }

    public static String generateValidCardCVC() {
        return FAKER.number().digits(3);
    }

    public static String generateIncompleteCVC() {
        return FAKER.number().digits(2);
    }

    public static CardInfo getValidCard() {
        return new CardInfo(validCardNumber(), generateValidCardMonth(), generateValidCardYear(), generateValidCardOwner(), generateValidCardCVC());
    }

    public static CardInfo getCardWithInvalidCardNumber(String invalidCardNumber) {
        return new CardInfo(invalidCardNumber, generateValidCardMonth(), generateValidCardYear(), generateValidCardOwner(), generateValidCardCVC());
    }

    public static CardInfo getCardWithInvalidCardMonth(String invalidCardMonth) {
        return new CardInfo(validCardNumber(), invalidCardMonth, generateValidCardYear(), generateValidCardOwner(), generateValidCardCVC());
    }

    public static CardInfo getCardWithInvalidCardYear(String invalidCardYear) {
        return new CardInfo(validCardNumber(), generateValidCardMonth(), invalidCardYear, generateValidCardOwner(), generateValidCardCVC());
    }

    public static CardInfo getCardWithInvalidCardOwner(String invalidOwner) {
        return new CardInfo(validCardNumber(), generateValidCardMonth(), generateValidCardYear(), invalidOwner, generateValidCardCVC());
    }

    public static CardInfo getCardWithInvalidCVC(String invalidCVC) {
        return new CardInfo(validCardNumber(), generateValidCardMonth(), generateValidCardYear(), generateValidCardOwner(), invalidCVC);
    }

    @Value
    public static class CardInfo {
        String cardNumber;
        String cardMonth;
        String cardYear;
        String cardOwner;
        String cardCvc;
    }
}
