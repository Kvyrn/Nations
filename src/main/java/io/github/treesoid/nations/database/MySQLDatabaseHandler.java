package io.github.treesoid.nations.database;

import io.github.treesoid.nations.Nations;
import io.github.treesoid.nations.abilities.util.Ability;
import io.github.treesoid.nations.abilities.util.PlayerAbility;
import io.github.treesoid.nations.abilities.util.PlayerAbilityList;
import io.github.treesoid.nations.helper.ServerPlayerHelper;
import io.github.treesoid.nations.storage.OfflinePlayerData;
import io.github.treesoid.nations.storage.OnlinePlayerData;
import io.github.treesoid.nations.storage.PlayerData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static io.github.treesoid.nations.Nations.LOGGER;

public class MySQLDatabaseHandler implements IDatabaseHandler {
    private static final String listAbilitiesQuery = "SELECT * FROM PlayerAbilities WHERE PlayerUuid = ?;";
    private static final String removePlayerAbilities = "DELETE FROM PlayerAbilities WHERE UUID = ?;";
    private static final String removePlayer = "DELETE FROM Players WHERE UUID = ?;";
    private static final String listHoldersOfAbility = "SELECT PlayerUuid FROM PlayerAbilities WHERE AbilityId = ?;";
    private static final String getSelectedAbility = "SELECT SelectedAbility FROM Players WHERE Uuid = ?;";
    private static final String updatePlayerData = "UPDATE Players SET Name = ?, Playtime = ?, Nation = ?, ResourcePoints = ?, SelectedAbility = ? WHERE Uuid = ?;";
    private static final String updateAbility = "UPDATE PlayerAbilities SET Favourite = ?, Cooldown = ? WHERE PlayerUuid = ? AND AbilityId = ?;";
    private static final String insertAbility = "INSERT INTO PlayerAbilities VALUES (?, ?, ?, ?);";
    private static final String removePlayerAbility = "DELETE FROM PlayerAbilities WHERE Uuid = ? AND AbilityId = ?;";
    private static final String getPlayerData = "SELECT * FROM Players WHERE Uuid = ?;";
    private static final String createPlayerData = "INSERT INTO Players VALUES (?, ?, ?, ?, ?, ?);";
    private static final String setSelectedAbility = "UPDATE Players SET SelectedAbility = ? WHERE Uuid = ?;";

    private final Connection databaseConnection;
    private final HashMap<UUID, PlayerAbilityList> abilityListCache = new HashMap<>();
    private final HashMap<UUID, PlayerData> playerDataCache = new HashMap<>();

    public MySQLDatabaseHandler(Connection conn) {
        this.databaseConnection = conn;
        createTables();
    }

    private void createTables() {
        /*
        Nations table
        | Name                  | Data type     |
        |-----------------------|---------------|
        | Name *(key*)          | VARCHAR(255)  |
         */
        String createNationsTable = "CREATE TABLE IF NOT EXISTS Nations (" +
                "Identifier VARCHAR(255)," +
                "PRIMARY KEY (Name)" +
                ");";
        try {
            PreparedStatement statement = databaseConnection.prepareStatement(createNationsTable);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.warn("Failed to create Nations table in database!", e);
        }

        /*
        Players Table
        | Name                  | Data type     |
        |-----------------------|---------------|
        | Uuid *(key)*          | CHAR(36)      |
        | Name                  | TINYTEXT      |
        | Playtime              | BIGINT        |
        | Nation **(fkey)**     | VARCHAR(255)  |
        | ResourcePoints        | INT           |
        | SelectedAbility       | TINYTEXT      |
         */
        String createPlayersTable = "CREATE TABLE IF NOT EXISTS Players (" +
                "Uuid CHAR(36) NOT NULL," +
                "Name TINYTEXT," +
                "Playtime BIGINT," +
                "Nation VARCHAR(255)," +
                "ResourcePoints INT," +
                "SelectedAbility TINYTEXT," +
                "PRIMARY KEY (Uuid)," +
                "FOREIGN KEY (Nation) REFERENCES Nations(Name)" +
                ");";
        try {
            PreparedStatement statement = databaseConnection.prepareStatement(createPlayersTable);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.warn("Failed to create Players table in database!", e);
        }

        /*
        PlayerAbilities table
        | Name                  | Data type     |public void registerNetworkRecivers() {
        ActivateAbilityPacket.registerReciver();
        SelectAbilityPacket.registerReciver();
    }
        |-----------------------|---------------|
        | PlayerUuid **(fkey)** | CHAR(36)      |
        | AbilityId             | TINYTEXT      |
        | Favourite             | BOOL          |
        | Cooldown              | INT           |
         */
        String createPlayerAbilitiesTable = "CREATE TABLE IF NOT EXISTS PlayerAbilities (" +
                "PlayerUuid CHAR(36) NOT NULL," +
                "AbilityId TINYTEXT NOT NULL," +
                "Favourite BOOL," +
                "Cooldown INT," +
                "FOREIGN KEY (PlayerUuid) REFERENCES Players(Uuid)" +
                ");";
        try {
            PreparedStatement statement = databaseConnection.prepareStatement(createPlayerAbilitiesTable);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.warn("Failed to create PlayerAbilities table in database!", e);
        }

        try {
            PreparedStatement statement = databaseConnection.prepareStatement("INSERT IGNORE INTO Nations VALUES ('nations:default');");
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.warn("Failed to create default nation!", e);
        }
    }

    @Override
    public PlayerAbilityList getOrCreatePlayerAbilityList(UUID uuid, MinecraftServer server, boolean forceRefresh, boolean cache) {
        if (!forceRefresh && abilityListCache.containsKey(uuid)) return abilityListCache.get(uuid);
        PlayerAbilityList object = new PlayerAbilityList(uuid, server);

        try {
            PreparedStatement statement = databaseConnection.prepareStatement(listAbilitiesQuery);
            statement.setString(1, uuid.toString());

            ResultSet set = statement.executeQuery();
            while (set.next()) {
                Identifier abilityId = new Identifier(set.getString("AbilityId"));
                boolean favourite = set.getBoolean("Favourite");
                int cooldown = set.getInt("Cooldown");

                PlayerAbility ability = new PlayerAbility(ServerPlayerHelper.getPlayer(uuid, server), Nations.ABILITY_REGISTRY.get(abilityId));
                ability.setCooldown(cooldown);
                ability.setFavourite(favourite);
                object.abilities.add(ability);
            }

            if (cache) abilityListCache.put(uuid, object);
            return object;
        } catch (SQLException e) {
            LOGGER.warn("Failed to query abilities!", e);
        }
        return null;
    }

    @Override
    public int removePlayerData(UUID uuid) {
        try {
            String uuidstr = uuid.toString();
            PreparedStatement removeAbilities = databaseConnection.prepareStatement(removePlayerAbilities);
            removeAbilities.setString(1, uuidstr);
            PreparedStatement removeData = databaseConnection.prepareStatement(removePlayer);
            removeData.setString(1, uuidstr);
            // return combined count of rows affected
            return removeAbilities.executeUpdate() + removeData.executeUpdate();
        } catch (SQLException e) {
            LOGGER.warn("Failed to remove player data!");
        }
        return 0;
    }

    @Override
    public int updatePlayerData(PlayerData data) {
        try {
            PreparedStatement statement = databaseConnection.prepareStatement(updatePlayerData);
            statement.setString(1, data.name);
            statement.setLong(2, data.playtime);
            statement.setString(3, data.nation.toString());
            statement.setInt(4, data.resourcePoints);
            statement.setString(5, data.selectedAbility == null ? "nations:null" : data.selectedAbility.toString());
            // Specify target
            statement.setString(6, data.uuid.toString());
            return statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.warn("Failed to update player data!", e);
        }
        return 0;
    }

    @Override
    public int updateCachedPlayerData(UUID uuid) {
        if (!playerDataCache.containsKey(uuid)) return 0;
        return updatePlayerData(playerDataCache.get(uuid));
    }

    @Override
    public PlayerData getOrCreatePlayerData(UUID uuid, MinecraftServer server, boolean forceRefresh, boolean cache) {
        if (!forceRefresh && playerDataCache.containsKey(uuid)) return playerDataCache.get(uuid);
        try {
            PreparedStatement statement = databaseConnection.prepareStatement(getPlayerData);
            statement.setString(1, uuid.toString());
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                String name = set.getString("Name");
                long playtime = set.getLong("Playtime");
                Identifier nation = new Identifier(set.getString("Nation"));
                int resourcePoints = set.getInt("ResourcePoints");
                Ability selectedAbility = Nations.ABILITY_REGISTRY.get(new Identifier(set.getString("SelectedAbility")));
                PlayerData playerData;
                if (ServerPlayerHelper.playerOnline(uuid, server)) {
                    playerData = new OnlinePlayerData(ServerPlayerHelper.getPlayer(uuid, server), name);
                } else {
                    playerData = new OfflinePlayerData(uuid, name);
                }
                playerData.setPlaytime(playtime);
                playerData.setNation(nation);
                playerData.setResourcePoints(resourcePoints);
                playerData.setSelectedAbility(selectedAbility);
                if (cache) playerDataCache.put(uuid, playerData);
                return playerData;
            } else {
                boolean playerOnline = ServerPlayerHelper.playerOnline(uuid, server);
                String name = playerOnline ? ServerPlayerHelper.getPlayer(uuid, server).getGameProfile().getName() : "";

                PreparedStatement create = databaseConnection.prepareStatement(createPlayerData);
                create.setString(1, uuid.toString());
                create.setString(2, name);
                create.setLong(3, 0);
                create.setString(4, "nations:default");
                create.setInt(5, 0);
                create.setString(6, "");
                create.executeUpdate();

                PlayerData playerData;
                if (ServerPlayerHelper.playerOnline(uuid, server)) {
                    playerData = new OnlinePlayerData(ServerPlayerHelper.getPlayer(uuid, server), name);
                } else {
                    playerData = new OfflinePlayerData(uuid, name);
                }
                playerData.setPlaytime(0);
                playerData.setNation(new Identifier("nations", "default"));
                playerData.setResourcePoints(0);
                playerData.setSelectedAbility(null);
                if (cache) playerDataCache.put(uuid, playerData);
                return playerData;
            }
        } catch (SQLException e) {
            LOGGER.warn("Failed to get player data!", e);
        }
        return null;
    }

    @Override
    public List<UUID> listHoldersOfAbility(Ability ability) {
        try {
            List<UUID> out = new LinkedList<>();
            PreparedStatement statement = databaseConnection.prepareStatement(listHoldersOfAbility);
            statement.setString(1, ability.identifier.toString());
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                UUID uuid = UUID.fromString(set.getString("PlayerUuid"));
                if (!out.contains(uuid)) out.add(uuid);
            }
            return out;
        } catch (SQLException e) {
            LOGGER.warn("Failed to list holders of ability!", e);
        }
        return null;
    }

    @Override
    public int updatePlayerAbility(PlayerAbility ability, UUID uuid) {
        if (ability == null) return 0;
        try {
            PreparedStatement statement = databaseConnection.prepareStatement(updateAbility);
            statement.setBoolean(1, ability.isFavourite());
            statement.setInt(2, ability.getCooldown());
            // Conditions
            statement.setString(3, uuid.toString());
            statement.setString(4, ability.ability.identifier.toString());

            return statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.warn("Failed to updtate ability!", e);
        }
        return 0;
    }

    @Override
    public int addPlayerAbility(PlayerAbility ability, UUID player) {
        try {
            PreparedStatement statement = databaseConnection.prepareStatement(insertAbility);
            statement.setString(1, player.toString());
            statement.setString(2, ability.ability.identifier.toString());
            statement.setBoolean(3, ability.isFavourite());
            statement.setInt(4, ability.getCooldown());
            return statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.warn("Failed to insert ability!", e);
        }
        return 0;
    }

    @Override
    public int removePlayerAbility(Ability ability, UUID player) {
        try {
            PreparedStatement statement = databaseConnection.prepareStatement(removePlayerAbility);
            statement.setString(1, player.toString());
            statement.setString(2, ability.identifier.toString());
            return statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.warn("Failed to remove player ability!", e);
        }
        return 0;
    }

    @Override
    public Ability getSelectedAbility(UUID player) {
        try {
            PreparedStatement statement = databaseConnection.prepareStatement(getSelectedAbility);
            statement.setString(1, player.toString());
            ResultSet set = statement.executeQuery();

            if (set.next()) {
                return Nations.ABILITY_REGISTRY.get(new Identifier(set.getString("SelectedAbility")));
            }
        } catch (SQLException e) {
            LOGGER.warn("Failed to get selected ability!", e);
        }
        return null;
    }

    @Override
    public void setSelectedAbility(UUID player, Ability ability) {
        try {
            PreparedStatement statement = databaseConnection.prepareStatement(setSelectedAbility);
            statement.setString(1, ability == null ? "nations:null" : ability.identifier.toString());
            statement.setString(2, player.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.warn("Failed to set selected ability!", e);
        }
    }

    @Override
    public void removeAbilityListFromCache(UUID uuid) {
        playerDataCache.remove(uuid);
    }

    @Override
    public void removePlayerDataFromCache(UUID uuid) {
        playerDataCache.remove(uuid);
    }
}
