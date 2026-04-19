package com.ynov.escape.service;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizApi {

    private static final String URL = "https://opentdb.com/api.php?amount=10&type=multiple";

    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static class Question {
        public String question;
        public String correctAnswer;
        public List<String> answers;
    }

    public static List<Question> fetch() throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        HttpResponse<String> res = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
        ApiResponse parsed = new Gson().fromJson(res.body(), ApiResponse.class);

        List<Question> list = new ArrayList<>();
        for (RawQuestion r : parsed.results) {
            Question q = new Question();
            q.question = decode(r.question);
            q.correctAnswer = decode(r.correctAnswer);
            List<String> all = new ArrayList<>();
            all.add(q.correctAnswer);
            for (String w : r.incorrectAnswers) all.add(decode(w));
            Collections.shuffle(all);
            q.answers = all;
            list.add(q);
        }
        return list;
    }

    private static String decode(String s) {
        if (s == null) return "";
        return s.replace("&quot;", "\"")
                .replace("&#039;", "'")
                .replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&eacute;", "é")
                .replace("&egrave;", "è")
                .replace("&agrave;", "à")
                .replace("&ocirc;", "ô");
    }

    private static class ApiResponse {
        @SerializedName("response_code") int responseCode;
        List<RawQuestion> results;
    }

    private static class RawQuestion {
        String category;
        String type;
        String difficulty;
        String question;
        @SerializedName("correct_answer") String correctAnswer;
        @SerializedName("incorrect_answers") List<String> incorrectAnswers;
    }
}
