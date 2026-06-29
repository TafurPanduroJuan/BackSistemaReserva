package com.grupo6.Comanda.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Verifica un Google ID Token llamando al endpoint público de Google.
 * No requiere librerías adicionales — usa solo java.net.
 *
 * Flujo:
 *   Frontend  → Google Sign-In → idToken
 *   Frontend  → POST /api/auth/google  { idToken }
 *   Backend   → GET https://oauth2.googleapis.com/tokeninfo?id_token=<idToken>
 *   Google    → { sub, email, name, picture, ... }
 */
@Component
public class GoogleTokenVerifier {

    private static final String VERIFY_URL =
            "https://oauth2.googleapis.com/tokeninfo?id_token=";

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Verifica el token y devuelve los datos del usuario de Google.
     * Lanza RuntimeException si el token es inválido.
     */
    public GoogleUserInfo verify(String idToken) {
        try {
            URL url = new URL(VERIFY_URL + idToken);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            int status = conn.getResponseCode();
            if (status != 200) {
                throw new RuntimeException("Token de Google inválido o expirado");
            }

            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
            }

            JsonNode node = mapper.readTree(sb.toString());

            if (node.has("error_description")) {
                throw new RuntimeException("Token rechazado por Google: "
                        + node.get("error_description").asText());
            }

            return new GoogleUserInfo(
                    node.path("sub").asText(),
                    node.path("email").asText(),
                    node.path("name").asText(),
                    node.path("picture").asText()
            );

        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar token de Google: " + e.getMessage(), e);
        }
    }

    // ── Record interno ────────────────────────────────────────────────────────
    public record GoogleUserInfo(
            String sub,
            String email,
            String name,
            String picture
    ) {}
}
