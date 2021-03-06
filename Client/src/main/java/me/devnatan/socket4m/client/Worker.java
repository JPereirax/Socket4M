package me.devnatan.socket4m.client;

import lombok.Data;
import me.devnatan.socket4m.client.io.Reader;
import me.devnatan.socket4m.client.io.Writer;

@Data
public class Worker implements Runnable {

    private boolean running;
    private Reader reader;
    private Writer writer;
    private Client client;

    @Override
    public void run() {
        try {
            while(running) {
                writer.proccess();
                reader.proccess();
            }
        } catch (Exception e) {
            if(client.getConnection().disconnect(true)) {
                if (client.getConnection().getErrorHandler() != null)
                    client.getConnection().getErrorHandler().handle(e);
                if(client.isAutoReconnect() && client.reconnect()) run();
            }
        }
    }

    /**
     * It makes the worker work.
     * After that he starts reading {@link Writer} messages
     * and receiving messages from {@link Reader}.
     *
     * It will only be interrupted if the connection connected to it is interrupted.
     * @throws IllegalStateException
     *         if the worker is already working.
     */
    public void work() throws IllegalStateException {
        if(running)
            throw new IllegalStateException("Worker is already running.");
        else {
            new Thread(this, "Socket4M-Client").start();
            running = true;
        }
    }

}
