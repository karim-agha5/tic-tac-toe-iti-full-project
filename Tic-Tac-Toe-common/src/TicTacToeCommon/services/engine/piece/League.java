package TicTacToeCommon.services.engine.piece;

import TicTacToeCommon.services.engine.player.Player;
import TicTacToeCommon.services.engine.player.Player.CrossPlayer;
import TicTacToeCommon.services.engine.player.Player.NoughtPlayer;
import java.io.Serializable;

public enum League implements Serializable {
    Cross {
        @Override
        public boolean isNought() { return false; }
        @Override
        public int getScore() { return -1; }
        @Override
        public boolean isCross() { return true; }
        @Override
        public Player nextPlayer(final CrossPlayer crossPlayer, final NoughtPlayer noughtPlayer) { return crossPlayer; }
        @Override
        public String toString() { return "X"; }
    }, Nought {
        @Override
        public boolean isNought() { return true; }
        @Override
        public int getScore() { return 1; }
        @Override
        public boolean isCross() { return false; }
        @Override
        public Player nextPlayer(final CrossPlayer crossPlayer, final NoughtPlayer noughtPlayer) { return noughtPlayer; }
        @Override
        public String toString() { return "O"; }
    };
    public abstract int getScore();
    public abstract boolean isCross();
    public abstract boolean isNought();
    public abstract Player nextPlayer(CrossPlayer crossPlayer, NoughtPlayer noughtPlayer);
}