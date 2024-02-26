package ru.netology.shop.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import ru.netology.shop.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class ShopPage {
    private final SelenideElement buyButton = $$("[role=button]").findBy(exactText("Купить"));
    private final SelenideElement cardInput = $(By.xpath("//span[contains(text(),'Номер карты')]/following-sibling::span[@class='input__box']/input[@class='input__control']"));
    ;
    private final SelenideElement monthInput = $(By.xpath("//span[contains(text(),'Месяц')]/following-sibling::span[@class='input__box']/input[@class='input__control']"));
    ;

    private final SelenideElement yearInput = $(By.xpath("//span[contains(text(),'Год')]/following-sibling::span[@class='input__box']/input[@class='input__control']"));
    ;

    private final SelenideElement ownerInput = $(By.xpath("//span[contains(text(),'Владелец')]/following-sibling::span[@class='input__box']/input[@class='input__control']"));
    ;

    private final SelenideElement cvcInput = $(By.xpath("//span[contains(text(),'CVC/CVV')]/following-sibling::span[@class='input__box']/input[@class='input__control']"));
    ;

    private final SelenideElement continueButton = $$("[role=button]").findBy(exactText("Продолжить"));
    private final SelenideElement errorNotification = $(".input__sub");
    private final SelenideElement succeedNotification = $(".notification__content");

    public void openingBuyForm() {
        buyButton.click();
    }

    public void fillingForm(DataHelper.CardInfo card) {
        cardInput.setValue(card.getCardNumber());
        monthInput.setValue(card.getCardMonth());
        yearInput.setValue(card.getCardYear());
        ownerInput.setValue(card.getCardOwner());
        cvcInput.setValue(card.getCardCvc());
    }

    public void verifySucceedMessageVisibility(String expectedText) {
        continueButton.click();
        succeedNotification.shouldHave(Condition.exactText(expectedText), Duration.ofSeconds(15)).shouldBe(visible);
    }

    public void verifyErrorNotificationVisibility(String expectedText) {
        continueButton.click();
        errorNotification.shouldHave(Condition.exactText(expectedText)).shouldBe(visible);
    }
}
