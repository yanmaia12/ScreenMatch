package com.yanmaia12.screenMatch.service;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ConsultaMyMemory {
    public static String obterTraducao(String texto) {
        String langPair = URLEncoder.encode("en|pt-br", StandardCharsets.UTF_8);
        String url = "https://api.mymemory.translated.net/get?q="
                + URLEncoder.encode(texto, StandardCharsets.UTF_8)
                + "&langpair=" + langPair;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());

            return root.path("responseData").path("translatedText").asText();
        } catch (Exception e) {
            return texto;
        }
    }
}
