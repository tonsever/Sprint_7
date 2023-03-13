package ru.yandex.praktikum.sprintseven.model;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static java.net.HttpURLConnection.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CreatingСourierTest {
    private СourierMethods courierMethods;
    private Courier courier;
    private CourierAccount courierAccount;
    private int courierId;

    @Before
    public void setUp() {
        courierMethods = new СourierMethods();
        courier = new Courier("Hatake", "1999", "Kakashi");
        courierAccount = new CourierAccount("Hatake", "1999");
    }

    @After
    public void clearData() {
        CourierAccount courierAccount = new CourierAccount("Hatake", "1999");
        ValidatableResponse loginResponse = courierMethods.login(courierAccount);
        courierId = loginResponse.extract().path("id");
        courierMethods.delete(courierId);
    }

    @Test
    @DisplayName("Курьера можно создать")
    @Description("Проверяем код ответа и ok: true")
    public void сreateСourierPositiveResult() {
        ValidatableResponse createResponse = courierMethods.create(courier);
        //System.out.println(createResponse.extract().asString());
        int statusCode = createResponse.extract().statusCode();
        boolean isOkTrue = createResponse.extract().path("ok");
        assertEquals("Не верный код статуса", HTTP_CREATED, statusCode);
        assertTrue("Не верное значение ключа ok", isOkTrue);
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    @Description("Проверям код ошибки и сообщение")
    public void сreateTwoIdenticalCouriersError() {
        courierMethods.create(courier);
        ValidatableResponse createResponse = courierMethods.create(courier);
        //System.out.println(createResponse.extract().asString());
        int statusCode = createResponse.extract().statusCode();
        String errorMessageExpected = "Этот логин уже используется. Попробуйте другой.";
        String errorMessageActual = createResponse.extract().path("message");
        assertEquals("Не верный код статуса", HTTP_CONFLICT, statusCode);
        assertEquals("Не верное сообщение ошибки", errorMessageExpected, errorMessageActual);
    }

    @Test
    @DisplayName("Нельзя создать курьера без логина")
    @Description("Проверям код ошибки и сообщение")
    public void createСourierWithoutLoginError() {
        Courier courierWithoutlogin = new Courier(null, "1999", "Kakashi");
        ValidatableResponse createResponse = courierMethods.create(courierWithoutlogin);
        //System.out.println(createResponse.extract().asString());
        int statusCode = createResponse.extract().statusCode();
        String errorMessageExpected = "Недостаточно данных для создания учетной записи";
        String errorMessageActual = createResponse.extract().path("message");
        assertEquals("Не верный код статуса", HTTP_BAD_REQUEST, statusCode);
        assertEquals("Не верное сообщение ошибки", errorMessageExpected, errorMessageActual);
        courierMethods.create(courier); //Иначе clearData() падает с NullPointerException
    }
}
