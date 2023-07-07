package TicTacToeCommon.models.base;

import java.io.Serializable;

public abstract class RemoteResponse<Data extends Serializable> implements RemoteSendable {

    protected boolean status;
    protected Data data;

    protected RemoteResponse() {
        this(false);
    }

    protected RemoteResponse(boolean status) {
        this(status, null);
    }

    protected RemoteResponse(boolean status, Data data) {
        this.status = status;
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
