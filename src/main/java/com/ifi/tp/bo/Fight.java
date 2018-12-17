package com.ifi.tp.bo;

import javax.persistence.*;
import java.util.List;

@Entity
public class Fight {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "logs")
    @ElementCollection(targetClass = String.class)
    private List<String> logs;

    private String trainer1;
    private String trainer2;
    private String winner;
    private String loser;
    private Boolean win = null;

    public Fight() {
    }

    public Fight(String t1, String t2, String winner, String loser, List<String> logs) {
        this.trainer1 = t1;
        this.trainer2 = t2;
        this.winner = winner;
        this.loser = loser;
        this.logs = logs;
    }

    public int getId() {
        return id;
    }

    public String getTrainer1() {
        return trainer1;
    }

    public String getTrainer2() {
        return trainer2;
    }

    public String getWinner() {
        return winner;
    }

    public String getLoser() {
        return loser;
    }

    public List<String> getLogs() {
        return logs;
    }

    public boolean isWin(String trainer) {
        this.win = winner.equals(trainer);
        return win;
    }

}
