package org.example.api;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.example.TestConfig;

public class RestApiBuilder {
    protected RequestSpecification getBaseSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(TestConfig.getApiUrl())
                .addHeader("Content-Type", "application/json")
                .setAuth(io.restassured.RestAssured.preemptive().basic(
                        TestConfig.getAdminLogin(), TestConfig.getAdminPassword()))
                .build();
    }
}