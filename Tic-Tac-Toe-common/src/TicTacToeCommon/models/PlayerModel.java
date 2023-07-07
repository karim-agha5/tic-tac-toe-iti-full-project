package TicTacToeCommon.models;

import java.io.Serializable;

public class PlayerModel implements Serializable{

    static final long serialVersionUID = 42L;
    private String name;
    private String id;

    public PlayerModel() {

    }

    public PlayerModel(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
