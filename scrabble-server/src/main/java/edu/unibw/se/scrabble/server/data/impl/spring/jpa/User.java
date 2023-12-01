package edu.unibw.se.scrabble.server.data.impl.spring.jpa;

import edu.unibw.se.scrabble.common.base.Statistics;
import jakarta.persistence.*;

/**
 * @author Bößendörfer
 */
@Entity
@Table(name = "SCRABBLE_USER")
public class User {

    @Id
    private String username;

    @Column(length = 20, nullable = false)
    private String password;

    @Column(length = 9999)
    private int gamesPlayed;

    @Column(length = 9999)
    private int gamesWon;

    @Column(length = 999999)
    private int highestScore;

    @Column(length = 999999999)
    private int totalScore;

    public User() {
    }

    public User(String username, String password, int gamesPlayed, int gamesWon, int highestScore, int totalScore) {
        this.username = username;
        this.password = password;
        this.gamesPlayed = gamesPlayed;
        this.gamesWon = gamesWon;
        this.highestScore = highestScore;
        this.totalScore = totalScore;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Statistics getStatistics() {
        return new Statistics(gamesPlayed, gamesWon, highestScore, totalScore);
    }

    public void setStatistics(Statistics statistics) {
        this.gamesPlayed = statistics.gamesPlayed();
        this.gamesWon = statistics.gamesWon();
        this.highestScore = statistics.highestScore();
        this.totalScore = statistics.totalScore();
    }

    @Override
    public String toString() {
        return "User{" + "username='" + username + '\'' + ", " +
                "password='" + password + '\'' + ", gamesPlayed='" + gamesPlayed + '\'' + ", " +
                "gamesWon='" + gamesWon + '\'' + ", highestScore='" + highestScore + '\'' + ", " +
                "totalScore='" + totalScore + '\'' + '}';
    }
}
