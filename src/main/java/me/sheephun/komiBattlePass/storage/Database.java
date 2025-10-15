package me.sheephun.komiBattlePass.storage;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import me.sheephun.komiBattlePass.data.PlayerData;
import me.sheephun.komiBattlePass.data.ResetTimer;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.Map;
import java.util.UUID;

import static me.sheephun.komiBattlePass.managers.PlayerDataManager.allPlayers;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Database {
    private static MongoDatabase database;
    public static MongoCollection<PlayerData> playerDataCollection = null;
    public static MongoCollection<ResetTimer> resetCollection = null;

    public Database() {
        if (database != null) return;

        CodecRegistry pojoCodecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new com.mongodb.ConnectionString("mongodb+srv://komjatigaborkornel21i:EgressKom2025@database.1y4tm.mongodb.net/?retryWrites=true&w=majority&appName=DATABASE"))
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(pojoCodecRegistry)
                .build();

        MongoClient mongoClient = MongoClients.create(settings);
        database = mongoClient.getDatabase("BattlePass");

        playerDataCollection = database.getCollection("playerData", PlayerData.class);
        resetCollection = database.getCollection("resetTimers", ResetTimer.class);

        System.out.println("Database is now connected.");
    }

    public static PlayerData getUser(UUID uuid) {
        return Database.playerDataCollection.find(Filters.eq("uuid", uuid)).first();
    }
    public static void saveUser(PlayerData playerData){
        Database.playerDataCollection.replaceOne(
                Filters.eq("uuid", playerData.getUuid()),
                playerData,
                new ReplaceOptions().upsert(true)
        );
    }
    public static void saveAllUser(){
        for (Map.Entry<UUID, PlayerData> entry : allPlayers.entrySet()){
            saveUser(entry.getValue());
        }
        System.out.println("All users have been saved!");
    }
    public static ResetTimer getResetTimer() {
        ResetTimer timer = resetCollection.find(Filters.eq("_id", "resetTimers")).first();
        if (timer == null) {
            timer = new ResetTimer("resetTimers", 0, 0);
            resetCollection.insertOne(timer);
        }
        return timer;
    }

    public static void saveResetTimer(ResetTimer timer) {
        resetCollection.replaceOne(
                Filters.eq("_id", "resetTimers"),
                timer,
                new ReplaceOptions().upsert(true)
        );
    }
}
