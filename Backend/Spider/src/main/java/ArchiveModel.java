import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "T_ArchivedRecords")
public class ArchiveModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column(unique = true, nullable = false)
    private String matchID;

    @Column(columnDefinition = "DATETIME", nullable = false)
    private Date matchTime;

    @Column(nullable = false)
    private String leagueID;
    @Column(nullable = false)
    private String leagueNameCH;

    @Column(nullable = false)
    private String hometeamID;
    @Column(nullable = false)
    private String hometeamNameCH;

    @Column(nullable = false)
    private String awayteamID;
    @Column(nullable = false)
    private String awayteamNameCH;

    @Column(columnDefinition = "MEDIUMTEXT", nullable = false)
    private String matchInfo;
    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String oddsData;

    public ArchiveModel(String matchID, Date matchTime, String leagueID, String leagueNameCH, String hometeamID,
            String hometeamNameCH, String awayteamID, String awayteamNameCH, String matchInfo, String oddsData) {
        this.matchID = matchID;
        this.matchTime = matchTime;
        this.leagueID = leagueID;
        this.leagueNameCH = leagueNameCH;
        this.hometeamID = hometeamID;
        this.hometeamNameCH = hometeamNameCH;
        this.awayteamID = awayteamID;
        this.awayteamNameCH = awayteamNameCH;
        this.matchInfo = matchInfo;
        this.oddsData = oddsData;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMatchID() {
        return matchID;
    }

    public void setMatchID(String matchID) {
        this.matchID = matchID;
    }

    public Date getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(Date matchTime) {
        this.matchTime = matchTime;
    }

    public String getLeagueID() {
        return leagueID;
    }

    public void setLeagueID(String leagueID) {
        this.leagueID = leagueID;
    }

    public String getLeagueNameCH() {
        return leagueNameCH;
    }

    public void setLeagueNameCH(String leagueNameCH) {
        this.leagueNameCH = leagueNameCH;
    }

    public String getHometeamID() {
        return hometeamID;
    }

    public void setHometeamID(String hometeamID) {
        this.hometeamID = hometeamID;
    }

    public String getHometeamNameCH() {
        return hometeamNameCH;
    }

    public void setHometeamNameCH(String hometeamNameCH) {
        this.hometeamNameCH = hometeamNameCH;
    }

    public String getAwayteamID() {
        return awayteamID;
    }

    public void setAwayteamID(String awayteamID) {
        this.awayteamID = awayteamID;
    }

    public String getAwayteamNameCH() {
        return awayteamNameCH;
    }

    public void setAwayteamNameCH(String awayteamNameCH) {
        this.awayteamNameCH = awayteamNameCH;
    }

    public String getMatchInfo() {
        return matchInfo;
    }

    public void setMatchInfo(String matchInfo) {
        this.matchInfo = matchInfo;
    }

    public String getOddsData() {
        return oddsData;
    }

    public void setOddsData(String oddsData) {
        this.oddsData = oddsData;
    }
}
