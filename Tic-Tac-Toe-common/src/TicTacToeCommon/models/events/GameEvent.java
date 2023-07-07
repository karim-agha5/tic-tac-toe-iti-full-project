package TicTacToeCommon.models.events;

import TicTacToeCommon.models.GameModel;
import TicTacToeCommon.models.MoveModel;
import TicTacToeCommon.models.PlayerModel;
import TicTacToeCommon.models.UserModel;
import TicTacToeCommon.models.base.RemoteEvent;
import TicTacToeCommon.services.engine.piece.League;

public abstract class GameEvent implements RemoteEvent {

    private String gameId;

    public GameEvent() {
    }

    public GameEvent(String gameId) {
        this.gameId = gameId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public static class Started extends GameEvent {

        static final long serialVersionUID = 42L;
        private GameModel game;
        private UserModel player;
        private League league;

        public Started() {
            super();
        }

        public Started(String gameId, GameModel game, UserModel player, League league) {
            super(gameId);
            this.game = game;
            this.player = player;
            this.league = league;
        }

        public GameModel getGame() {
            return game;
        }

        public void setGame(GameModel game) {
            this.game = game;
        }

        public UserModel getPlayer() {
            return player;
        }

        public void setPlayer(UserModel player) {
            this.player = player;
        }

        public League getLeague() {
            return league;
        }

        public void setLeague(League league) {
            this.league = league;
        }
        
    }

    public static class Won extends GameEvent {

        static final long serialVersionUID = 42L;

        public Won() {
            super();
        }

        public Won(String gameId) {
            super(gameId);
        }
    }

    public static class Lost extends GameEvent {

        static final long serialVersionUID = 42L;

        public Lost() {
            super();
        }

        public Lost(String gameId) {
            super(gameId);
        }
    }

    public static class Withdraw extends GameEvent {

        static final long serialVersionUID = 42L;

        public Withdraw() {
            super();
        }

        public Withdraw(String gameId) {
            super(gameId);
        }
    }
    
    public static class Draw extends GameEvent {

        static final long serialVersionUID = 42L;

        public Draw() {
            super();
        }

        public Draw(String gameId) {
            super(gameId);
        }
    }

    public static class Ended extends GameEvent {

        static final long serialVersionUID = 42L;

        public Ended() {
            super();
        }

        public Ended(String gameId) {
            super(gameId);
        }
    }

    public static class Moved extends GameEvent {

        static final long serialVersionUID = 42L;
        
        private String playerId;
        private MoveModel move;

        public Moved() {
            super();
        }

        public Moved(String gameId) {
            super(gameId);
        }

        public Moved(String gameId, String playerId, MoveModel move) {
            super(gameId);
            this.playerId = playerId;
            this.move = move;
        }

        public String getPlayerId() {
            return playerId;
        }

        public void setPlayerId(String playerId) {
            this.playerId = playerId;
        }

        public MoveModel getMove() {
            return move;
        }

        public void setMove(MoveModel move) {
            this.move = move;
        }

    }
}
