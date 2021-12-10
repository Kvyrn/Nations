package io.github.treesoid.nations.config;

public class NationsConfigObject {
    public DatabaseConfig database = new DatabaseConfig();

    public static class DatabaseConfig {
        public String url = "localhost:3306/database";
        public String username = "user";
        public String password = "password";
    }

    public AbilityConfigs abilities = new AbilityConfigs();

    public static class AbilityConfigs {
        public FartJumpConfig fartJump = new FartJumpConfig();

        public static class FartJumpConfig {
            public double verticalVelocity = 2.0d;
        }
    }
}
