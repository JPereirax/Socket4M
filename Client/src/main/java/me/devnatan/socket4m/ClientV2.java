package me.devnatan.socket4m;

import lombok.Data;
import me.devnatan.socket4m.connection.Connection;

@Data
public class ClientV2 {

    private Connection connection;
    private WorkerV2 worker;

    public boolean connect() {
        if(connection.connect()) {
            worker.work();
            return true;
        } return false;
    }

    public boolean reconnect() {
        return connection.reconnect();
    }

    public boolean disconnect() {
        return connection.disconnect();
    }

}

