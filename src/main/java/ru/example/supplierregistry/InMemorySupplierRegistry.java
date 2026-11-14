package ru.example.supplierregistry;

import org.springframework.stereotype.Component;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class InMemorySupplierRegistry implements SupplierRegistry {
    private final AtomicReference<Map<String, Supplier>> suppliers = new AtomicReference<>(Map.of());

    public InMemorySupplierRegistry() { }
    public InMemorySupplierRegistry(List<Supplier> initial) { replaceAll(initial); }

    @Override public void replaceAll(List<Supplier> values) {
        Objects.requireNonNull(values, \"suppliers\");
        Map<String, Supplier> replacement = values.stream().map(s -> Objects.requireNonNull(s, \"supplier\"))
                .collect(Collectors.toUnmodifiableMap(Supplier::id, Function.identity(), (a, b) -> {
                    throw new IllegalArgumentException(\"Duplicate supplier id: \" + a.id());
                }));
        suppliers.set(replacement);
    }

    @Override public Supplier get(String supplierId) {
        Supplier supplier = suppliers.get().get(supplierId);
        if (supplier == null) throw new UnknownSupplierException(supplierId);
        return supplier;
    }
}
