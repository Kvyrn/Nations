package io.github.treesoid.nations.config;

public class NationsConfigObject {
    public DatabaseConfig database = new DatabaseConfig();

    public static class DatabaseConfig {
        public String url = "localhost:3306/database";
        public String username = "user";
        public String password = "password";

        @Override
        public String toString() {
            return "DatabaseConfig{" +
                    "url='" + url + '\'' +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }

    public AbilityConfigs abilities = new AbilityConfigs();

    public static class AbilityConfigs {
        public FartJumpConfig fartJump = new FartJumpConfig();

        public static class FartJumpConfig {
            public double verticalVelocity = 2.0d;

            @Override
            public String toString() {
                return "FartJumpConfig{" +
                        "verticalVelocity=" + verticalVelocity +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "AbilityConfigs{" +
                    "fartJump=" + fartJump +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "NationsConfigObject{" +
                "database=" + database +
                ", abilities=" + abilities +
                '}';
    }
}
