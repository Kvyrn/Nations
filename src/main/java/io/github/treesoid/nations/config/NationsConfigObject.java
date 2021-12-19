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
            public double verticalVelocity = 1.0d;
            public double verticalVelocityLarge = 1.5d;
            public double verticalVelocityExplosive = 4.0d;

            public double largeChance = 0.1f;
            public double explosiveChance = 0.02f;
        }
    }

    public ResourcePointsConfig resourcePoints = new ResourcePointsConfig();

    public static class ResourcePointsConfig {
        public double fortune1Multiplier = 1.05f;
        public double fortune2Multiplier = 1.1f;
        public double fortune3Multiplier = 1.2f;

        public int coalOre = 20;
        public int ironOre = 50;
        public int goldOre = 60;
        public int copperOre = 30;
        public int redstoneOre = 80;
        public int lapisOre = 80;
        public int diamondOre = 120;
        public int emeraldOre = 130;
    }
}
