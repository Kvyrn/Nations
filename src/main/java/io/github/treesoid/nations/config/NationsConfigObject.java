package io.github.treesoid.nations.config;

public class NationsConfigObject {
    public DatabaseConfig database = new DatabaseConfig();

    public static class DatabaseConfig {
        public String url = "localhost:3306/database";
        public String username = "user";
        public String password = "password";
    }
}
