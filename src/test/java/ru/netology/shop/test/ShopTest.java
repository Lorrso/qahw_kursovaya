package ru.netology.shop.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.shop.data.DataHelper;
import ru.netology.shop.data.SQLHelper;
import ru.netology.shop.pages.ShopPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.shop.data.DataHelper.*;

public class ShopTest {
    ShopPage shopPage;
    DataHelper.CardInfo card;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
        SQLHelper.cleanDatabase();
    }

    @BeforeEach
    void setUp() {
        shopPage = open("http://localhost:8080", ShopPage.class);
        shopPage.openingBuyForm();
    }

    @Test
    void shouldBuySuccessfully() {
        var expectedText = "Операция одобрена Банком.";
        card = getValidCard();
        shopPage.fillingForm(card);
        shopPage.verifySucceedMessageVisibility(expectedText);
        String expectedStatusPayment = "APPROVED";
        String actualStatusPayment = SQLHelper.getPaymentStatus();
        boolean expectedStatusOrder = true;
        boolean actualStatusOrder = SQLHelper.getOrderStatus();
        assertAll(() -> assertEquals(expectedStatusPayment, actualStatusPayment),
                () -> assertEquals(expectedStatusOrder, actualStatusOrder));
    }

    @Test
    void shouldGetErrorNotificationWhenSubmitsEmptyForm() {
        var expectedText = "Неверный формат";
        shopPage.verifyErrorNotificationVisibility(expectedText);
    }

    @Test
    void shouldGetErrorNotificationWhenSubmitsFormWithIncompleteCardNumber() {
        var expectedText = "Неверный формат";
        var invalidCardNumber = "1111 2222 3333 444";
        card = getCardWithInvalidCardNumber(invalidCardNumber);
        shopPage.fillingForm(card);
        shopPage.verifyErrorNotificationVisibility(expectedText);
    }

    @Test
    void shouldGetErrorNotificationWhenSubmitsFormWithNonExistentCardNumber() {
        var expectedText = "Неверный формат";
        var invalidCardNumber = "0000 0000 0000 0000";
        card = getCardWithInvalidCardNumber(invalidCardNumber);
        shopPage.fillingForm(card);
        shopPage.verifyErrorNotificationVisibility(expectedText);
    }

    @Test
    void shouldGetErrorNotificationWhenSubmitsFormWithEmptyCardNumber() {
        var expectedText = "Неверный формат";
        var invalidCardNumber = "";
        card = getCardWithInvalidCardNumber(invalidCardNumber);
        shopPage.fillingForm(card);
        shopPage.verifyErrorNotificationVisibility(expectedText);
    }

    @Test
    void shouldGetErrorNotificationWhenSubmitsFormWithPreviousMonth() {
        var expectedText = "Неверно указан срок действия карты";
        var invalidMonth = generateCardPreviousMonth();
        card = getCardWithInvalidCardMonth(invalidMonth);
        shopPage.fillingForm(card);
        shopPage.verifyErrorNotificationVisibility(expectedText);
    }

    @Test
    void shouldGetErrorNotificationWhenSubmitsFormWithIncorrectFormatOfMonth() {
        var expectedText = "Неверный формат";
        var invalidMonth = generateCardMonthWithIncorrectFormat();
        card = getCardWithInvalidCardMonth(invalidMonth);
        shopPage.fillingForm(card);
        shopPage.verifyErrorNotificationVisibility(expectedText);
    }

    @Test
    void shouldGetErrorNotificationWhenSubmitsFormWithInvalidLowerLimitValueOfMonth() {
        var expectedText = "Неверно указан срок действия карты";
        var invalidMonth = "00";
        card = getCardWithInvalidCardMonth(invalidMonth);
        shopPage.fillingForm(card);
        shopPage.verifyErrorNotificationVisibility(expectedText);
    }

    @Test
    void shouldGetErrorNotificationWhenSubmitsFormWithInvalidUpperLimitValueOfMonth() {
        var expectedText = "Неверно указан срок действия карты";
        var invalidMonth = "13";
        card = getCardWithInvalidCardMonth(invalidMonth);
        shopPage.fillingForm(card);
        shopPage.verifyErrorNotificationVisibility(expectedText);
    }

    @Test
    void shouldGetErrorNotificationWhenSubmitsFormWithEmptyMonth() {
        var expectedText = "Неверный формат";
        var invalidMonth = "";
        card = getCardWithInvalidCardMonth(invalidMonth);
        shopPage.fillingForm(card);
        shopPage.verifyErrorNotificationVisibility(expectedText);
    }

    @Test
    void shouldGetErrorNotificationWhenSubmitsFormWithPreviousYear() {
        var expectedText = "Истёк срок действия карты";
        var invalidYear = generateCardPreviousYear();
        card = getCardWithInvalidCardYear(invalidYear);
        shopPage.fillingForm(card);
        shopPage.verifyErrorNotificationVisibility(expectedText);
    }

    @Test
    void shouldGetErrorNotificationWhenSubmitsFormWithYearUpperLimit() {
        var expectedText = "Неверно указан срок действия карты";
        var invalidYear = generateCardYearUpperLimit();
        card = getCardWithInvalidCardYear(invalidYear);
        shopPage.fillingForm(card);
        shopPage.verifyErrorNotificationVisibility(expectedText);
    }

    @Test
    void shouldGetErrorNotificationWhenSubmitsFormWithOneDigitInYear() {
        var expectedText = "Неверный формат";
        var invalidYear = "2";
        card = getCardWithInvalidCardYear(invalidYear);
        shopPage.fillingForm(card);
        shopPage.verifyErrorNotificationVisibility(expectedText);
    }

    @Test
    void shouldGetErrorNotificationWhenSubmitsFormWithEmptyYear() {
        var expectedText = "Неверный формат";
        var invalidYear = "";
        card = getCardWithInvalidCardYear(invalidYear);
        shopPage.fillingForm(card);
        shopPage.verifyErrorNotificationVisibility(expectedText);
    }

    @Test
    void shouldGetErrorNotificationWhenSubmitsFormWithIncompleteValueOfOwner() {
        var expectedText = "Неверный формат";
        var invalidOwner = generateCardOwnerWithIncompleteData();
        card = getCardWithInvalidCardOwner(invalidOwner);
        shopPage.fillingForm(card);
        shopPage.verifyErrorNotificationVisibility(expectedText);
    }

    @Test
    void shouldGetErrorNotificationWhenSubmitsFormWithOwnersNameWithSpecialChars() {
        var expectedText = "Неверный формат";
        var invalidOwner = getValidCard().getCardOwner() + "!№;%:?*";
        card = getCardWithInvalidCardOwner(invalidOwner);
        shopPage.fillingForm(card);
        shopPage.verifyErrorNotificationVisibility(expectedText);
    }

    @Test
    void shouldGetErrorNotificationWhenSubmitsFormWithOwnersNameWithHieroglyphs() {
        var expectedText = "Неверный формат";
        var invalidOwner = "田中太郎";
        card = getCardWithInvalidCardOwner(invalidOwner);
        shopPage.fillingForm(card);
        shopPage.verifyErrorNotificationVisibility(expectedText);
    }

    @Test
    void shouldGetErrorNotificationWhenSubmitsFormWithEmptyOwner() {
        var expectedText = "Поле обязательно для заполнения";
        var invalidOwner = "";
        card = getCardWithInvalidCardOwner(invalidOwner);
        shopPage.fillingForm(card);
        shopPage.verifyErrorNotificationVisibility(expectedText);
    }

    @Test
    void shouldGetErrorNotificationWhenSubmitsFormWithCVC() {
        var expectedText = "Неверный формат";
        var invalidCVC = generateIncompleteCVC();
        card = getCardWithInvalidCVC(invalidCVC);
        shopPage.fillingForm(card);
        shopPage.verifyErrorNotificationVisibility(expectedText);
    }

    @Test
    void shouldGetErrorNotificationWhenSubmitsFormWithEmptyCVC() {
        var expectedText = "Поле обязательно для заполнения";
        var invalidCVC = "";
        card = getCardWithInvalidCVC(invalidCVC);
        shopPage.fillingForm(card);
        shopPage.verifyErrorNotificationVisibility(expectedText);
    }
}
