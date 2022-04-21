import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import redis.clients.jedis.Jedis;

public class ArchiveMatch {
    private Hibernate hibernate;
    private Jedis redis_driver;
    private String match_id;
    private Map<String, String> match_data;

    public ArchiveMatch(Hibernate hibernate, Jedis redis_driver, String match_id) {
        this.hibernate = hibernate;
        this.redis_driver = redis_driver;
        this.match_id = match_id;
    }

    public void load() {
        match_data = redis_driver.hgetAll(match_id);
    }

    public void save() throws ParseException {
        JsonObject match_info = JsonParser.parseString(match_data.get("matchInfo")).getAsJsonObject();
        Date matchTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
                .parse(match_info.get("matchTime").getAsString());
        String leagueID = match_info.get("league").getAsJsonObject().get("leagueID").getAsString();
        String leagueNameCH = match_info.get("league").getAsJsonObject().get("leagueNameCH").getAsString();
        String hometeamID = match_info.get("homeTeam").getAsJsonObject().get("teamID").getAsString();
        String hometeamNameCH = match_info.get("homeTeam").getAsJsonObject().get("teamNameCH").getAsString();
        String awayteamID = match_info.get("awayTeam").getAsJsonObject().get("teamID").getAsString();
        String awayteamNameCH = match_info.get("awayTeam").getAsJsonObject().get("teamNameCH").getAsString();

        JsonArray oddsData = new JsonArray();
        match_data.entrySet().stream().filter(e1 -> e1.getKey().contains("oddsData"))
                .forEach(e1 -> oddsData.add(JsonParser.parseString(e1.getValue())));

        ArchiveModel new_model = new ArchiveModel(
                match_id,
                matchTime,
                leagueID,
                leagueNameCH,
                hometeamID,
                hometeamNameCH,
                awayteamID,
                awayteamNameCH,
                match_data.get("matchInfo"),
                new Gson().toJson(oddsData));
        hibernate.session.persist(new_model);
    }
}
