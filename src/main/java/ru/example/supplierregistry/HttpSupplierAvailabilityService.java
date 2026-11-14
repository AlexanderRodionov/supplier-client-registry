package ru.example.supplierregistry;

import org.springframework.http.HttpStatusCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class HttpSupplierAvailabilityService implements SupplierAvailabilityService {
    private final SupplierRegistry registry;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public HttpSupplierAvailabilityService(SupplierRegistry registry, RestClient.Builder builder, ObjectMapper objectMapper) {
        this(registry, builder.build(), objectMapper);
    }

    HttpSupplierAvailabilityService(SupplierRegistry registry, RestClient restClient, ObjectMapper objectMapper) {
        this.registry = registry;
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    @Override public boolean isPhoneAvailable(String supplierId, String phone) {
        if (phone == null || phone.isBlank()) throw new IllegalArgumentException("Phone must not be blank");
        Supplier supplier = registry.get(supplierId);
        java.net.URI uri = UriComponentsBuilder.fromUriString(supplier.baseUrl())
                .pathSegment("subscribers", "{phone}").buildAndExpand(phone).encode().toUri();
        try {
            return restClient.get().uri(uri).header("X-API-Key", supplier.apiKey()).exchange((request, response) -> {
                HttpStatusCode status = response.getStatusCode();
                if (status.value() == 404) return false;
                if (status.value() != 200) throw new RestClientResponseException(
                        "Supplier returned HTTP " + status.value(), status.value(), status.toString(),
                        response.getHeaders(), response.getBody().readAllBytes(), null);
                AvailabilityResponse body = objectMapper.readValue(response.getBody(), AvailabilityResponse.class);
                return body != null && "ok".equals(body.status());
            });
        } catch (RestClientResponseException e) {
            if (e.getStatusCode().value() == 404) return false;
            throw e;
        }
    }

    private record AvailabilityResponse(String status) { }
}
