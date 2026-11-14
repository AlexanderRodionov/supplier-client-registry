package ru.example.supplierregistry;

public class UnknownSupplierException extends RuntimeException {
    public UnknownSupplierException(String supplierId) { super(\"Unknown supplier: \" + supplierId); }
}
