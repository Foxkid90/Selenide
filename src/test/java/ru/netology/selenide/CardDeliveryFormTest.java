package ru.netology.selenide;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

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
                .shouldHave(text("Встреча успешно забронирована на " + datePlusDays));

    }

    @Test
    void shouldSendRequestForCardV2() {

        open("http://localhost:9999");
        SelenideElement form = $("form");

        form.$("[data-test-id=city] .input__control").press("С").press("а");
        $$(".popup__container .menu-item__control").findBy(text("Саратов")).click();

        LocalDate dateNow = LocalDate.now();
        LocalDate datePlusDays = dateNow.plusDays(7);

        String verificationDate = datePlusDays.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        int day = datePlusDays.getDayOfMonth();

        YearMonth currentMonth = YearMonth.from(dateNow);
        YearMonth targetMonth = YearMonth.from(datePlusDays);
        int monthsBetween = (int) ChronoUnit.MONTHS.between(currentMonth, targetMonth);

        Year currentYear = Year.from(dateNow);
        Year targetYear = Year.from(datePlusDays);
        int yearBetween = (int) ChronoUnit.YEARS.between(currentYear, targetYear);


        form.$("[data-test-id=date] .icon-button").click();
        for (int i = 0; i <yearBetween; i++) {
            $(".calendar__arrow[data-step='12']").click();
        }
        for (int i = 0; i < monthsBetween; i++) {
            $(".calendar__arrow[data-step='1']").click();
        }
        $$(".calendar__layout .calendar__day").findBy(text(String.valueOf(day))).click();

        form.$("[data-test-id=name] .input__control").setValue("Врангель Петр");
        form.$("[data-test-id=phone] .input__control").setValue("+79170001922");
        form.$("[data-test-id=agreement] .checkbox__box").click();
        form.$("button .button__content").click();

        $("[data-test-id=notification]").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=notification] .notification__content")
                .shouldHave(text("Встреча успешно забронирована на " + verificationDate));

    }
}