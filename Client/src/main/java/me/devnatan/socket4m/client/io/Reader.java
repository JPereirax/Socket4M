package me.devnatan.socket4m.client.io;

import me.devnatan.socket4m.client.connection.Connection;
import me.devnatan.socket4m.client.message.Message;

import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Reader extends IOProcessor<Message> {

    public Reader(Connection connection, BlockingQueue<Message> queue, int buffer) {
        super(connection, queue, buffer);
    }

    /**
     * Reads the message contained within the {@link java.io.InputStream} of the connection channel.
     * A ByteBuffer allocates a specific amount of read capability, this capacity is given as {@link #buffer}.
     * If a message is found, it will be treated and added to the read queue.
     * If a message handler exists, it handles the message.
     */
    public void proccess() throws ExecutionException, InterruptedException {
        ByteBuffer bb = ByteBuffer.allocate(buffer);
        Future<Integer> f = connection.getChannel().read(bb);
        f.get();

        String s = new String(bb.array());
        if (s.length() > 0) {
            Message m = Message.fromJson(s);
            if(connection.getMessageHandler() != null)
                connection.getMessageHandler().handle("read", m);
        } bb.clear();
    }

}
