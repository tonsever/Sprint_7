package ru.yandex.praktikum.sprintseven.model;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;

import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class CreatingOrderTest {
    private final String firstName;
    private final String lastName;
    private final String address;
    private final int metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String[] color;
    private OrderMethods orderMethods;
    private Order order;
    private int orderTrack;

    public CreatingOrderTest(String firstName, String lastName, String address, int metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getParams() {
        return TestData.DATA;
    }

    @Before
    public void setUp() {
        orderMethods = new OrderMethods();
        order = new Order(
                firstName,
                lastName,
                address,
                metroStation,
                phone,
                rentTime,
                deliveryDate,
                comment,
                color);
    }

    @Test
    @DisplayName("Заказ можно создать")
    @Description("Проверяем код ответа и что есть номер заказа")
    public void сreateOrderPositiveResult() {
        ValidatableResponse createResponse = orderMethods.create(order);
        //System.out.println(createResponse.extract().asString());
        int statusCode = createResponse.extract().statusCode();
        int track = createResponse.extract().path("track");
        assertEquals("Не верный код статуса", HTTP_CREATED, statusCode);
        assertNotNull("Не пришёл номер заказа", track);
    }

    @Test
    @DisplayName("Получаем список заказов")
    @Description("Проверяем код ответа и что массив с заказами не пустой")
    public void getAllOrdersArrayWithOrdersNotEmpty() {
        ValidatableResponse createResponse = orderMethods.grtAllOrders();
        //System.out.println(createResponse.extract().asString());
        int statusCode = createResponse.extract().statusCode();
        ArrayList orders = createResponse.extract().path("orders");
        assertEquals("Не верный код статуса", HTTP_OK, statusCode);
        assertFalse("Массив с заказами пустой", orders.isEmpty());
    }
}
