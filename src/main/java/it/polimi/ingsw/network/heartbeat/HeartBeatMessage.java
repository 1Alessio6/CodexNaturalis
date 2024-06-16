package it.polimi.ingsw.network.heartbeat;

import it.polimi.ingsw.network.NetworkMessage;
import it.polimi.ingsw.network.Type;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * HeartBeatMessage represents the message containing the sender and the id number of the message.
 */
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

    /**
     * Constructs a <code>HeartBeatMessage</code> with the <code>sender</code> and the <code>id</code> provided.
     *
     * @param sender the sender of the heartBeat.
     * @param id     the identification of the heartBeat message.
     */
    public HeartBeatMessage(String sender, int id) {
        super(Type.HEARTBEAT, sender);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
