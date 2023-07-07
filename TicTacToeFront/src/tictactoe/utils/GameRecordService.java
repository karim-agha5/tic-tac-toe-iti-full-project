/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe.utils;

import TicTacToeCommon.models.MoveModel;
import TicTacToeCommon.models.UserModel;
import TicTacToeCommon.services.engine.piece.League;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author ITI
 */
public class GameRecordService {

    private final File dir = new File("Records");
    
    public GameRecordService() {
        dir.mkdir();
    }
    
    public void writeGame(Record record) throws FileNotFoundException, IOException {
        File file = new File(dir.getAbsoluteFile()+ File.separator + record.gameId);
        try (ObjectOutputStream writeStream = new ObjectOutputStream(new FileOutputStream(file))) {
            writeStream.writeObject(record);
            writeStream.flush();
        }
    }

    public Record readGame(String id) throws IOException, ClassNotFoundException {
        File file = new File(dir.getAbsoluteFile()+ File.separator +id);
        try (ObjectInputStream readStream = new ObjectInputStream(new FileInputStream(file))) {
            return (Record) readStream.readObject();
        }
    }

    public List<String> getGameList() {
        List<String> results = new LinkedList<>();
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                results.add(file.getName());
            }
        }
        return results;
    }

    public static class Record implements Serializable {

        static final long serialVersionUID = 42L;
        
        private String gameId;
        private UserModel player1;
        private League player1League;
        private UserModel player2;
        private List<MoveModel> moves;
        private Date createdAt;

        public Record() {
        }

        public String getGameId() {
            return gameId;
        }

        public void setGameId(String gameId) {
            this.gameId = gameId;
        }

        public UserModel getPlayer1() {
            return player1;
        }

        public void setPlayer1(UserModel player1) {
            this.player1 = player1;
        }

        public League getPlayer1League() {
            return player1League;
        }

        public void setPlayer1League(League player1League) {
            this.player1League = player1League;
        }
        
        public UserModel getPlayer2() {
            return player2;
        }

        public void setPlayer2(UserModel player2) {
            this.player2 = player2;
        }

        public List<MoveModel> getMoves() {
            return moves;
        }

        public void setMoves(List<MoveModel> moves) {
            this.moves = moves;
        }

        public Date getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }

    }
}
