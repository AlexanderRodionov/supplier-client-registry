package ru.example.supplierregistry;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class HttpSupplierAvailabilityServiceTest {
    private MockRestServiceServer server;
    private HttpSupplierAvailabilityService service;

    @BeforeEach void setUp() {
        RestClient.Builder builder = RestClient.builder();
        server = MockRestServiceServer.bindTo(builder).build();
        service = new HttpSupplierAvailabilityService(
                new InMemorySupplierRegistry(java.util.List.of(new Supplier("s1", "https://supplier.test/api", "secret"))),
                builder.build(), new ObjectMapper());
    }

    @Test void returnsTrueForOkResponse() {
        server.expect(requestTo("https://supplier.test/api/subscribers/+15551234567"))
                .andExpect(method(HttpMethod.GET)).andExpect(header("X-API-Key", "secret"))
                .andRespond(withSuccess("""
                        {"status":"ok"}
                        """, MediaType.APPLICATION_JSON));
        assertTrue(service.isPhoneAvailable("s1", "+15551234567"));
        server.verify();
    }

    @Test void returnsFalseForNotFound() {
        server.expect(requestTo("https://supplier.test/api/subscribers/15551234567"))
                .andExpect(method(HttpMethod.GET)).andRespond(withStatus(org.springframework.http.HttpStatus.NOT_FOUND));
        assertFalse(service.isPhoneAvailable("s1", "15551234567"));
        server.verify();
    }
}
