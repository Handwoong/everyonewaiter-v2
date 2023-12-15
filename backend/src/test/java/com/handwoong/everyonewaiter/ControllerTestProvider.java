package com.handwoong.everyonewaiter;

import static com.handwoong.everyonewaiter.util.Constants.ADMIN_EMAIL;
import static com.handwoong.everyonewaiter.util.Constants.ADMIN_PASSWORD;
import static com.handwoong.everyonewaiter.util.Constants.MANAGER_EMAIL;
import static com.handwoong.everyonewaiter.util.Constants.MANAGER_PASSWORD;
import static com.handwoong.everyonewaiter.util.Constants.USER_EMAIL;
import static com.handwoong.everyonewaiter.util.Constants.USER_PASSWORD;
import static com.handwoong.everyonewaiter.util.RestDocsUtils.getSpecification;
import static com.handwoong.everyonewaiter.util.RestDocsUtils.setSpecification;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

import com.handwoong.everyonewaiter.config.security.jwt.TokenResponse;
import com.handwoong.everyonewaiter.domain.member.dto.MemberLoginRequest;
import com.handwoong.everyonewaiter.util.DatabaseCleaner;
import com.handwoong.everyonewaiter.util.RestDocsUtils;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@Sql({"classpath:db/data.sql"})
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureRestDocs
public class ControllerTestProvider {
    public static final MemberLoginRequest userRequest = new MemberLoginRequest(USER_EMAIL, USER_PASSWORD);
    public static final MemberLoginRequest managerRequest = new MemberLoginRequest(MANAGER_EMAIL, MANAGER_PASSWORD);
    public static final MemberLoginRequest adminRequest = new MemberLoginRequest(ADMIN_EMAIL, ADMIN_PASSWORD);

    public static String userAccessToken;
    public static String managerAccessToken;
    public static String adminAccessToken;

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    private static String getToken(final MemberLoginRequest loginRequest) {
        final ExtractableResponse<Response> loginResponse = login(loginRequest);
        final TokenResponse tokenResponse = loginResponse.body().as(TokenResponse.class);
        return tokenResponse.token();
    }

    private static ExtractableResponse<Response> login(final MemberLoginRequest loginRequest) {
        return RestAssured
                .given(getSpecification()).log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(loginRequest)
                .when().post("/api/members/login")
                .then().log().all().extract();
    }

    @BeforeEach
    void setUp(final RestDocumentationContextProvider provider) {
        RestAssured.port = port;
        final RequestSpecification specification = new RequestSpecBuilder()
                .addFilter(
                        documentationConfiguration(provider)
                                .operationPreprocessors()
                                .withRequestDefaults(RestDocsUtils.removeHeaders())
                                .withResponseDefaults(RestDocsUtils.removeHeaders())
                )
                .addFilter(RestDocsUtils.getFilter())
                .build();
        setSpecification(specification);
        userAccessToken = getToken(userRequest);
        managerAccessToken = getToken(managerRequest);
        adminAccessToken = getToken(adminRequest);
    }

    @AfterEach
    void clear() {
        databaseCleaner.execute();
    }
}
