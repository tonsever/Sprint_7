package ru.yandex.praktikum.sprintseven.model;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderMethods extends ScooterRestClient {
    private static final String COURIER_URI = BASE_URI + "orders/";

    @Step("Создаём заказа")
    public ValidatableResponse create(Order order) {
        return given()
                .spec(getBaseReqSpec())
                .body(order)
                .when()
                .post(COURIER_URI)
                .then();
    }

    @Step("Получение списка заказов")
    public ValidatableResponse grtAllOrders() {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .get(COURIER_URI)
                .then();
    }
}
