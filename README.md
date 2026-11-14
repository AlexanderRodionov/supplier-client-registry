# Supplier HTTP client registry

Java 17 and Spring service for checking phone availability with configured suppliers.

## Build and run

```sh
mvn test
mvn spring-boot:run
```

This is a library-style Spring application with no HTTP server endpoints. Other code can inject `SupplierAvailabilityService` and call `isPhoneAvailable(supplierId, phone)`. The supplied `JsonSupplierFileLoader` reads a JSON array and atomically replaces the full registry:

```java
loader.loadAndReplace(Path.of("suppliers.json"));
boolean available = availabilityService.isPhoneAvailable("provider-a", "+15551234567");
```

JSON entries have `id`, `baseUrl`, and `apiKey` fields. Requests use `GET {baseUrl}/subscribers/{phone}` and send the key in `X-API-Key`. HTTP 200 returns `true` only for `{"status":"ok"}`; HTTP 404 returns `false`. Other HTTP responses and network failures propagate to the caller. The tests use Spring's `MockRestServiceServer`, so they make no real network calls.

The in-memory registry accepts a ready-made list through `replaceAll(List<Supplier>)`; duplicate IDs or invalid supplier data are rejected before the existing registry is replaced.
