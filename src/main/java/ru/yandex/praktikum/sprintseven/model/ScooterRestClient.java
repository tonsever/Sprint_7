package ru.yandex.praktikum.sprintseven.model;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ScooterRestClient {
    protected static final String BASE_URI = "http://qa-scooter.praktikum-services.ru/api/v1/";

    protected RequestSpecification getBaseReqSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URI)
                .build();
    }
}
