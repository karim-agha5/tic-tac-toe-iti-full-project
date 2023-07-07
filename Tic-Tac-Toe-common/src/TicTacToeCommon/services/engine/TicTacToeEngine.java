/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TicTacToeCommon.services.engine;

import TicTacToeCommon.models.UserModel;
import TicTacToeCommon.services.engine.board.Board;
import TicTacToeCommon.services.engine.move.Move;
import TicTacToeCommon.services.engine.piece.League;
import TicTacToeCommon.services.engine.player.Player;
import TicTacToeCommon.services.engine.player.ai.MiniMax;
import TicTacToeCommon.services.engine.player.ai.TicTacToeAlgorithm;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 *
 * @author m-essam
 */
public class TicTacToeEngine {

    public static final UserModel AI = new UserModel("ai", "Robot", null, new Date().getTime());

    private final UserModel crossPlayer;
    private final UserModel noughtPlayer;
    private final Random random;
    private final TicTacToeAlgorithm miniMax;
    private final List<Move> moves = new LinkedList<>();
    private Board board;

    public TicTacToeEngine(UserModel player) {
        this(player, AI);
    }

    public TicTacToeEngine(UserModel player1, UserModel player2) {
        random = new Random();
        if (random.nextBoolean()) {
            this.crossPlayer = player1;
            this.noughtPlayer = player2;
        } else {
            this.crossPlayer = player2;
            this.noughtPlayer = player1;
        }
        miniMax = new MiniMax(10);
        board = Board.createStandardBoard(3);
    }

    public UserModel getPlayer(League league) {
        switch (league) {
            case Cross:
                return crossPlayer;
            default:
                return noughtPlayer;
        }
    }

    public UserModel getPlayer(String playerId) {
        if (Objects.equals(playerId, crossPlayer.getId())) {
            return crossPlayer;
        } else if (Objects.equals(playerId, noughtPlayer.getId())) {
            return noughtPlayer;
        }
        return null;
    }

    public UserModel getOtherPlayer(String playerId) {
        if (Objects.equals(playerId, crossPlayer.getId())) {
            return noughtPlayer;
        } else if (Objects.equals(playerId, noughtPlayer.getId())) {
            return crossPlayer;
        }
        return null;
    }

    public League getLeague(String playerId) {
        if (Objects.equals(playerId, crossPlayer.getId())) {
            return League.Cross;
        } else if (Objects.equals(playerId, noughtPlayer.getId())) {
            return League.Nought;
        }
        return null;
    }

    public Player getCurrentPlayer() {
        return board.getCurrentPlayer();
    }

    public List<Move> getMoves() {
        return moves;
    }

    public GameResult makeMove(String playerId, Integer position) throws InvalidMoveException {
        Move move = new Move(board, getLeague(playerId), position);
        return makeMove(move);
    }

    public GameResult makeAIMove(Difficulty difficulty) throws InvalidMoveException {
        if (board.isDraw() || board.isWin()) {
            throw new InvalidMoveException();
        }
        Move move = null;
        switch (difficulty) {
            case EASY:
                move = createaEasyMove();
                break;
            case MEDIUM:
                move = createaMediumMove();
                break;
            case HARD:
                move = createaHardMove();
                break;
        }
        return makeMove(move);
    }

    private GameResult makeMove(Move move) throws InvalidMoveException {
        if (board.isDraw() || board.isWin() || board.getCurrentPlayer().getLeague() != move.getPiece().getLeague()) {
            throw new InvalidMoveException();
        }
        board = move.execute();
        moves.add(move);
        if (board.isWin(League.Cross)) {
            return GameResult.CROSS_WINS;
        } else if (board.isWin(League.Nought)) {
            return GameResult.NOUGHT_WINS;
        } else if (board.isDraw()) {
            return GameResult.DRAW;
        } else {
            return GameResult.ONGOING;
        }
    }

    private Move createaMediumMove() {
        if (random.nextBoolean()) {
            return createaEasyMove();
        } else {
            return createaHardMove();
        }
    }

    private Move createaHardMove() {
        return miniMax.execute(board);
    }

    private Move createaEasyMove() {
        while (true) {
            int index = random.nextInt(board.getSize());
            if (board.getTileList().get(index).tileNotOccupied()) {
                return new Move(board, board.getCurrentPlayer().getLeague(), index);
            }
        }
    }

    // Driver code
    public static void main(String[] args) throws InvalidMoveException {
        TicTacToeEngine brain = new TicTacToeEngine(AI);
        System.out.println(brain.board);
        brain.makeAIMove(Difficulty.MEDIUM);
        System.out.println(brain.board);
        brain.makeAIMove(Difficulty.EASY);
        System.out.println(brain.board);
        brain.makeAIMove(Difficulty.HARD);
        System.out.println(brain.board);
        brain.makeAIMove(Difficulty.MEDIUM);
        System.out.println(brain.board);
        brain.makeAIMove(Difficulty.EASY);
        System.out.println(brain.board);
        brain.makeAIMove(Difficulty.HARD);
        System.out.println(brain.board);
        brain.makeAIMove(Difficulty.MEDIUM);
        System.out.println(brain.board);
        brain.makeAIMove(Difficulty.EASY);
        System.out.println(brain.board);
        brain.makeAIMove(Difficulty.HARD);
        System.out.println(brain.board);
        brain.makeAIMove(Difficulty.MEDIUM);
        System.out.println(brain.board);
        brain.makeAIMove(Difficulty.EASY);
        System.out.println(brain.board);
        brain.makeAIMove(Difficulty.HARD);
        System.out.println(brain.board);
    }

    public static enum Difficulty {
        EASY,
        MEDIUM,
        HARD
    }

    public static enum GameResult {
        CROSS_WINS,
        NOUGHT_WINS,
        DRAW,
        ONGOING,
    }

    public static class InvalidMoveException extends Exception {

    }
}
