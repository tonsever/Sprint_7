package ru.yandex.praktikum.sprintseven.model;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class СourierMethods extends ScooterRestClient {
    private static final String COURIER_URI = BASE_URI + "courier/";

    @Step("Создаём курьера")
    public ValidatableResponse create(Courier courier) {
        return given()
                .spec(getBaseReqSpec())
                .body(courier)
                .when()
                .post(COURIER_URI)
                .then();
    }

    @Step("Логируемся")
    public ValidatableResponse login(CourierAccount courierAccount) {
        return given()
                .spec(getBaseReqSpec())
                .body(courierAccount)
                .when()
                .post(COURIER_URI + "login/")
                .then();
    }

    @Step("Удаляем курьера")
    public ValidatableResponse delete(int id) {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .delete(COURIER_URI + id)
                .then();
    }
}
