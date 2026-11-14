package ru.example.supplierregistry;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Component
public class JsonSupplierFileLoader {
    private final ObjectMapper objectMapper;
    private final SupplierRegistry registry;

    public JsonSupplierFileLoader(ObjectMapper objectMapper, SupplierRegistry registry) {
        this.objectMapper = objectMapper;
        this.registry = registry;
    }

    public List<Supplier> read(Path path) throws IOException {
        return objectMapper.readValue(path.toFile(), new TypeReference<>() { });
    }

    public void loadAndReplace(Path path) throws IOException { registry.replaceAll(read(path)); }
}
