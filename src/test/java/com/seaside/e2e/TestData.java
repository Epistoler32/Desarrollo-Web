package com.seaside.e2e;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Central place for all test data used in e2e tests.
 * Change values here if the DataLoader seeds different credentials.
 */
public final class TestData {

    private TestData() {
    }

    // ── Admin credentials (seeded by DataLoader) ──────────────────────────
    public static final String ADMIN_CORREO = "carlos.admin@seaside.com";
    public static final String ADMIN_PASSWORD = "admin123";
    public static final String ADMIN_WRONG_PASS = "wrongpassword";

    // ── Cliente credentials (seeded by DataLoader) ────────────────────────
    public static final String CLIENTE_CORREO = "laura.gomez@email.com";
    public static final String CLIENTE_PASSWORD = "1234";

    // ── Operador credentials (seeded by DataLoader) ───────────────────────
    public static final String OPERADOR_USUARIO = "drojas";
    public static final String OPERADOR_PASSWORD = "OpDani2026";

    // ── New product data for Case 1 ───────────────────────────────────────
    /** Unique name per run so repeated executions don't collide. */
    public static final String PRODUCTO_NOMBRE = "TestProduct_"
            + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
    public static final String PRODUCTO_PRECIO = "25000";
    public static final String PRODUCTO_TIEMPO = "20";
    public static final String PRODUCTO_DESCRIPCION = "Producto de prueba automatizada e2e";
    public static final String PRODUCTO_IMAGE_URL = "";
    /** Visible text of the category to select in the form dropdown. */
    public static final String PRODUCTO_CATEGORIA = "Platos Fuertes";
}
