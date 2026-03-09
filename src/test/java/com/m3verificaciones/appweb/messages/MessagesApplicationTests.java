package com.m3verificaciones.appweb.messages;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") // Usa un perfil específico para tests
class MessagesApplicationTests {

    @Test
    void contextLoads() {
        // Test vacío solo para verificar que el contexto se carga
    }
}