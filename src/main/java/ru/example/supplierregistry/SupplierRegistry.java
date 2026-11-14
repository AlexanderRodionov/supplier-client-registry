package ru.example.supplierregistry;

import java.util.List;

public interface SupplierRegistry {
    void replaceAll(List<Supplier> suppliers);
    Supplier get(String supplierId);
}
