/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TicTacToeCommon;

import TicTacToeCommon.models.base.RemoteMessage;
import TicTacToeCommon.models.base.RemoteSendable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;

public class TicTacToeCommon {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        serialize();
        deserialize();
    }

    private static void deserialize() throws IOException, ClassNotFoundException {
        try (ObjectInputStream oin = new ObjectInputStream(new FileInputStream("remotemessage.bin"))) {
            RemoteMessage<RemoteSendable> message;
            while ((message = RemoteMessage.readFrom(oin)) != null) {
                OurData data = message.getMessage(OurData.class);
                if (data != null) {
                    System.out.println("got data " + data.i);
                }
            }
        }
    }

    private static void serialize() throws IOException {
        RemoteMessage<OurData> message = new RemoteMessage<>(new OurData(91823));
        try (ObjectOutputStream oout = new ObjectOutputStream(new PrintStream("remotemessage.bin"))) {
            message.writeInto(oout);
        }
    }

    static class OurData implements RemoteSendable {

        static final long serialVersionUID = 42L;

        private int i;

        public OurData() {
        }

        public OurData(int i) {
            this.i = i;
        }
    }
}
