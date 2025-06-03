package com.enquete.service;

import io.github.cdimascio.dotenv.Dotenv;

import java.net.http.*;
import java.net.URI;

import com.google.gson.*;

public class TestEnv {
    public static void main(String[] args) {
        try {
            Dotenv dotenv = Dotenv.load();
            String API_KEY = dotenv.get("GEMINI_API_KEY");

            String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;
            String prompt = "Raconte une blague courte";

            // JsonObject jsonPrompt = new JsonObject();
            JsonArray parts = new JsonArray();
            JsonObject messagePart = new JsonObject();
            messagePart.addProperty("text", prompt);
            parts.add(messagePart);

            JsonObject contents = new JsonObject();
            contents.add("parts", parts);
            contents.addProperty("role", "user");

            JsonObject finalPayload = new JsonObject();
            finalPayload.add("contents", new JsonArray());
            finalPayload.getAsJsonArray("contents").add(contents);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(finalPayload.toString()))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("🧾 Réponse brute (UTF-8 check):\n" + response.body());

            JsonObject root = JsonParser.parseString(response.body()).getAsJsonObject();
            String output = root
                    .getAsJsonArray("candidates")
                    .get(0).getAsJsonObject()
                    .getAsJsonArray("content")
                    .get(0).getAsJsonObject()
                    .get("text").getAsString();

            System.out.println("✅ Réponse IA :\n" + output);

        } catch (Exception e) {
            System.err.println("❌ Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
