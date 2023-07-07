package TicTacToeCommon.services.engine.player.ai;

import TicTacToeCommon.services.engine.board.Board;
import TicTacToeCommon.services.engine.move.Move;

public interface TicTacToeAlgorithm {

    Move execute(final Board board);

}
