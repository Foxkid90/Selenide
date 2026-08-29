package ru.netology.selenide;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryFormTest {

    @Test
    void shouldSendRequestForCard () {

        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] .input__control").setValue("Саратов");
        String datePlusDays = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        SelenideElement date = form.$("[data-test-id=date] .input__control")
                .press(Keys.CONTROL, "a").press(Keys.BACK_SPACE).setValue(datePlusDays);
        form.$("[data-test-id=name] .input__control").setValue("Врангель Петр");
        form.$("[data-test-id=phone] .input__control").setValue("+79170001922");
        form.$("[data-test-id=agreement] .checkbox__box").click();
        form.$("button .button__content").click();
        $("[data-test-id=notification]").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=notification] .notification__content").shouldHave(text(date.getValue()));



    }
}
