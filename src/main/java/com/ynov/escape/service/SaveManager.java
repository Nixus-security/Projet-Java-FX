package com.ynov.escape.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ynov.escape.model.GameState;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SaveManager {

    private static final Path SAVE_PATH = Paths.get(
            System.getProperty("user.home"), ".javaescape", "save.json");

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void save(GameState state) {
        try {
            Files.createDirectories(SAVE_PATH.getParent());
            try (Writer w = new FileWriter(SAVE_PATH.toFile())) {
                GSON.toJson(state, w);
            }
        } catch (IOException e) {
            System.err.println("Sauvegarde impossible : " + e.getMessage());
        }
    }

    public static GameState load() {
        if (!Files.exists(SAVE_PATH)) return null;
        try (Reader r = new FileReader(SAVE_PATH.toFile())) {
            return GSON.fromJson(r, GameState.class);
        } catch (IOException e) {
            System.err.println("Chargement impossible : " + e.getMessage());
            return null;
        }
    }

    public static boolean hasSave() {
        return Files.exists(SAVE_PATH);
    }

    public static void delete() {
        try {
            Files.deleteIfExists(SAVE_PATH);
        } catch (IOException ignored) {
        }
    }
}
