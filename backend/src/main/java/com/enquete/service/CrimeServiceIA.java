package com.enquete.service;

import com.enquete.model.*;
import com.google.gson.*;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CrimeServiceIA {

    private static final Dotenv dotenv = Dotenv.load();
    private static final String API_KEY = dotenv.get("GEMINI_API_KEY");
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;

    private static final Gson gson = new Gson();

    public static Crime genererCrimeParIA() throws IOException, InterruptedException {
        String prompt = """
        Tu es un générateur d'enquête policière. Réponds uniquement avec un objet JSON, sans texte ni explication.

        Voici le format attendu :

        {
          "victime": "Alice Moreau",
          "indices": [
            {"nom": "Couteau", "description": "Retrouvé sous le lit", "crucial": true}
          ],
          "suspects": [
            {
              "nom": "Bob",
              "alibi": "Chez lui",
              "ment": false,
              "questions": [
                {"question": "Pourquoi étiez-vous en retard ce soir-là ?", "reponse": "J'ai eu une panne de voiture."},
                {"question": "Quel était votre lien avec la victime ?", "reponse": "On était collègues."}
              ]
            }
          ],
          "coupable": "Bob"
        }

        Génère une nouvelle enquête avec 5 suspects, chacun ayant au moins 3 questions personnalisées (différentes pour chaque suspect) avec leurs réponses. Réponds uniquement en JSON.
        """;

        JsonObject requestBody = new JsonObject();
        JsonArray contents = new JsonArray();
        JsonObject content = new JsonObject();
        JsonArray parts = new JsonArray();
        JsonObject part = new JsonObject();
        part.addProperty("text", prompt);
        parts.add(part);
        content.add("parts", parts);
        contents.add(content);
        requestBody.add("contents", contents);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestBody)))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        String jsonRaw = response.body();
        System.out.println("\uD83D\uDCCE Réponse brute (UTF-8 check):\n" + new String(jsonRaw.getBytes(), StandardCharsets.UTF_8));

        JsonObject root = JsonParser.parseString(jsonRaw).getAsJsonObject();
        JsonArray candidates = root.getAsJsonArray("candidates");
        if (candidates == null || candidates.size() == 0) {
            System.err.println("Erreur : réponse IA invalide (pas de candidats)");
            System.err.println("Contenu brut :\n" + jsonRaw);
            throw new IllegalStateException("Réponse IA invalide (pas de candidats)");
        }

        String texteGenere = candidates.get(0).getAsJsonObject()
                .getAsJsonObject("content")
                .getAsJsonArray("parts")
                .get(0).getAsJsonObject()
                .get("text").getAsString();

        String jsonExtracted = extractJson(texteGenere);
        System.out.println("jsonExtracted : " + jsonExtracted);
        return parseCrime(jsonExtracted);
    }

    private static String extractJson(String texte) {
        int start = texte.indexOf('{');
        int end = texte.lastIndexOf('}');
        return (start != -1 && end != -1) ? texte.substring(start, end + 1) : "{}";
    }

    private static Crime parseCrime(String jsonContent) {
        JsonObject root = JsonParser.parseString(jsonContent).getAsJsonObject();

        String victime = root.get("victime").getAsString();

        List<Indice> indices = new ArrayList<>();
        for (JsonElement e : root.getAsJsonArray("indices")) {
            JsonObject o = e.getAsJsonObject();
            Indice i = new Indice(
                    o.get("nom").getAsString(),
                    o.get("description").getAsString(),
                    o.get("crucial").getAsBoolean()
            );
            indices.add(i);
        }

        List<Suspect> suspects = new ArrayList<>();
        Suspect coupableFinal = null;
        String coupableNom = root.get("coupable").getAsString();

        for (JsonElement e : root.getAsJsonArray("suspects")) {
            JsonObject s = e.getAsJsonObject();

            List<QuestionReponse> questions = new ArrayList<>();
            for (JsonElement qElem : s.getAsJsonArray("questions")) {
                JsonObject q = qElem.getAsJsonObject();
                questions.add(new QuestionReponse(
                        q.get("question").getAsString(),
                        q.get("reponse").getAsString()
                ));
            }

            Suspect suspect = new Suspect(
                    s.get("nom").getAsString(),
                    s.get("alibi").getAsString(),
                    s.get("ment").getAsBoolean(),
                    new ArrayList<>(),
                    questions
            );

            if (suspect.getNom().equalsIgnoreCase(coupableNom)) {
                coupableFinal = suspect;
            }

            suspects.add(suspect);
        }

        return new Crime(victime, suspects, indices, coupableFinal);
    }
}
