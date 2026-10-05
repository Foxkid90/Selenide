package ru.netology.selenide;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryFormTest {

    @Test
    void shouldSendRequestForCardV1() {

        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] .input__control").setValue("Саратов");
        String datePlusDays = LocalDate.now().plusDays(3)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        SelenideElement date = form.$("[data-test-id=date] .input__control")
                .press(Keys.CONTROL, "a").press(Keys.BACK_SPACE).setValue(datePlusDays);
        form.$("[data-test-id=name] .input__control").setValue("Врангель Петр");
        form.$("[data-test-id=phone] .input__control").setValue("+79170001922");
        form.$("[data-test-id=agreement] .checkbox__box").click();
        form.$("button .button__content").click();
        $("[data-test-id=notification]").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=notification] .notification__content")
                .shouldHave(text(date.getValue()), text("Встреча успешно забронирована на "));

    }

    @Test
    void shouldSendRequestForCardV2() {

        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] .input__control").press("С").press("а");
        $$(".popup__container .menu-item__control").findBy(text("Саратов")).click();

        LocalDate datePlusDays = LocalDate.now().plusDays(7);
        int day = datePlusDays.getDayOfMonth();
        int year = datePlusDays.getYear();
        String monthOfYear = datePlusDays.format(DateTimeFormatter
                .ofPattern("LLLL yyyy", new Locale("ru")));

        form.$("[data-test-id=date] .icon-button").click();
        String currentDateValue = $(".calendar__name").getText();

        while (!currentDateValue.contains(String.valueOf(year))) {
            $(".calendar__arrow[data-step='12']").click();
            currentDateValue = $(".calendar__name").getText();
        }

        while (!currentDateValue.equalsIgnoreCase(monthOfYear)) {
            $(".calendar__arrow[data-step='1']").click();
            currentDateValue = $(".calendar__name").getText();
        }

        $$(".calendar__layout .calendar__day").findBy(text(String.valueOf(day))).click();

        form.$("[data-test-id=name] .input__control").setValue("Врангель Петр");
        form.$("[data-test-id=phone] .input__control").setValue("+79170001922");
        form.$("[data-test-id=agreement] .checkbox__box").click();
        String verificationDate = datePlusDays.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        form.$("button .button__content").click();

        $("[data-test-id=notification]").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=notification] .notification__content")
                .shouldHave(text(verificationDate), text("Встреча успешно забронирована на "));

    }
}