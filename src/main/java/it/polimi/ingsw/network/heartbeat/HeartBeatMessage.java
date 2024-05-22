package it.polimi.ingsw.network.heartbeat;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class HeartBeatMessage extends NetworkMessage implements Serializable{
    private final int id;
    //private static final AtomicInteger incrementalId = new AtomicInteger();

    //private static int getIncrementedId() {
    //    return incrementalId.addAndGet(1);
    //}

    //public static int getIncrementalId() {
    //    return incrementalId.get();
    //}

    // default constructor needed by gson
  //  public HeartBeatMessage() {
  //      super(Type.HEARTBEAT, "uninitializedSender");
  //      //this.id = getIncrementedId();
  //  }

    public HeartBeatMessage(String sender, int id) {
        super(Type.HEARTBEAT, sender);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
