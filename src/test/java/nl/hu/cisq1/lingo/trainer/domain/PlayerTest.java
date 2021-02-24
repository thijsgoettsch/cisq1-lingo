package nl.hu.cisq1.lingo.trainer.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    @Test
    @DisplayName("check if playername is correct")
    void checkPlayerName() {
        Player player = new Player("Jim");
        assertEquals(player.getName(), "Jim");
    }
}