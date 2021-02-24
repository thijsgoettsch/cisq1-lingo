package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.words.domain.Word;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private Player player;
    private List<Round> rounds = new ArrayList<>();

    public Game(Player player, List<Round> rounds) {
        this.player = player;
        this.rounds = rounds;
    }

//    public Round startGame(Word word) {
//
//    }
//
//    public Round startNewRound() {
//
//    }

}
