package ru.example.supplierregistry;

import java.net.URI;
import java.util.Objects;

public record Supplier(String id, String baseUrl, String apiKey) {
    public Supplier {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Supplier id must not be blank");
        if (baseUrl == null || baseUrl.isBlank()) throw new IllegalArgumentException("Supplier baseUrl must not be blank");
        URI uri = URI.create(baseUrl);
        if (!Objects.equals(uri.getScheme(), "http") && !Objects.equals(uri.getScheme(), "https"))
            throw new IllegalArgumentException("Supplier baseUrl must use http or https");
        if (uri.getHost() == null) throw new IllegalArgumentException("Supplier baseUrl must include a host");
        if (apiKey == null || apiKey.isBlank()) throw new IllegalArgumentException("Supplier apiKey must not be blank");
        baseUrl = baseUrl.replaceAll("/+$", "");
    }
}
