package com.shinemo.mpush.core.netty;

import com.shinemo.mpush.api.PacketReceiver;
import com.shinemo.mpush.api.protocol.Command;
import com.shinemo.mpush.common.MessageDispatcher;
import com.shinemo.mpush.core.ServerChannelHandler;
import com.shinemo.mpush.core.handler.BindUserHandler;
import com.shinemo.mpush.core.handler.HandShakeHandler;
import com.shinemo.mpush.netty.connection.NettyConnectionManager;
import com.shinemo.mpush.core.server.ConnectionServer;
import com.shinemo.mpush.netty.server.NettyServer;
import io.netty.channel.ChannelHandler;
import org.junit.Test;

/**
 * Created by ohun on 2015/12/24.
 */
public class NettyServerTest {

    @Test
    public void testStop() throws Exception {

    }

    @Test
    public void testStart() throws Exception {

        MessageDispatcher receiver = new MessageDispatcher();
        receiver.register(Command.HANDSHAKE, new HandShakeHandler());
        receiver.register(Command.HEARTBEAT, new BindUserHandler());
        receiver.register(Command.BIND, new BindUserHandler());
        NettyConnectionManager connectionManager = new NettyConnectionManager();
        connectionManager.registerEventBus();
        ChannelHandler handler = new ServerChannelHandler(connectionManager, receiver);

        final NettyServer server = new ConnectionServer(3000, handler);
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    server.stop();
                } catch (Exception e) {
                }
            }
        });
    }
}