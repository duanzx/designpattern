package cn.chenhy.rdt.rdt1_0;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

public class Receiver {
    public static void main(String[] args) throws Exception {
        Receiver receiver = new Receiver();
        receiver.rdtReceive();
    }

    public void rdtReceive() throws Exception {
        try (DatagramChannel serverChannel = DatagramChannel.open();) {
            serverChannel.socket().bind(new InetSocketAddress("localhost", 8882));
            while (serverChannel.isOpen()) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                SocketAddress senderAddress = serverChannel.receive(byteBuffer);
                byteBuffer.flip();
                deliverData(senderAddress, byteBuffer);
            }
        }
    }

    public void deliverData(SocketAddress senderAddress, ByteBuffer byteBuffer) {
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes,0,byteBuffer.remaining());
        System.out.println("receive: " + senderAddress.toString());
        System.out.println("data: " + new String(bytes));
    }

}
