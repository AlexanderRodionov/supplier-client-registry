package ru.example.supplierregistry;

public interface SupplierAvailabilityService {
    boolean isPhoneAvailable(String supplierId, String phone);
}
