package ru.yandex.praktikum.sprintseven.model;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static java.net.HttpURLConnection.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class СourierLoginTest {
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
    @DisplayName("Курьер может авторизоваться")
    @Description("Проверяем код ответа и что есть id")
    public void loginСourierPositiveResult() {
        courierMethods.create(courier);
        ValidatableResponse loginResponse = courierMethods.login(courierAccount);
        //System.out.println(loginResponse.extract().statusCode());
        int statusCode = loginResponse.extract().statusCode();
        int isNotNull = loginResponse.extract().path("id");
        assertEquals("Не верный код статуса", HTTP_OK, statusCode);
        assertNotNull("Не верное значение ключа ok", isNotNull);
    }

    @Test
    @DisplayName("Нельзя авторизоваться без логина")
    @Description("Проверям код ошибки и сообщение")
    public void loginСourierWithoutLoginError() {
        courierMethods.create(courier);
        CourierAccount courierAccount = new CourierAccount(null, "1999");
        ValidatableResponse loginResponse = courierMethods.login(courierAccount);
        //System.out.println(loginResponse.extract().asString());
        int statusCode = loginResponse.extract().statusCode();
        String errorMessageExpected = "Недостаточно данных для входа";
        String errorMessageActual = loginResponse.extract().path("message");
        assertEquals("Не верный код статуса", HTTP_BAD_REQUEST, statusCode);
        assertEquals("Не верное сообщение ошибки", errorMessageExpected, errorMessageActual);
    }

    @Test
    @DisplayName("Нельзя авторизоваться c неверным паролем")
    @Description("Проверям код ошибки и сообщение")
    public void loginСourierWithWrongPasswordError() {
        courierMethods.create(courier);
        CourierAccount courierAccount = new CourierAccount("Hatake", "xxx");
        ValidatableResponse loginResponse = courierMethods.login(courierAccount);
        //System.out.println(loginResponse.extract().asString());
        int statusCode = loginResponse.extract().statusCode();
        String errorMessageExpected = "Учетная запись не найдена";
        String errorMessageActual = loginResponse.extract().path("message");
        assertEquals("Не верный код статуса", HTTP_NOT_FOUND, statusCode);
        assertEquals("Не верное сообщение ошибки", errorMessageExpected, errorMessageActual);
    }

    @Test
    @DisplayName("Нельзя авторизоваться под несуществующим пользователем")
    @Description("Проверям код ошибки и сообщение")
    public void loginNonExistentСourierError() {
        ValidatableResponse loginResponse = courierMethods.login(courierAccount);
        //System.out.println(loginResponse.extract().asString());
        int statusCode = loginResponse.extract().statusCode();
        String errorMessageExpected = "Учетная запись не найдена";
        String errorMessageActual = loginResponse.extract().path("message");
        assertEquals("Не верный код статуса", HTTP_NOT_FOUND, statusCode);
        assertEquals("Не верное сообщение ошибки", errorMessageExpected, errorMessageActual);
        courierMethods.create(courier); //чтобы delete не падал
    }
}
