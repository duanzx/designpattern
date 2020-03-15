package cn.chenhy.rdt.rdt1_0;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.UUID;

/**
 * 假定分组不会丢失，并且按序到达
 * */
public class Sender {
    public static void main(String[] args)throws Exception{
        Sender sender = new Sender();
        String message = UUID.randomUUID().toString().replace("-","").toUpperCase();
        sender.rdtSend(message);
    }
    public void rdtSend(String message){
        udtSend(makePacket(message));
    }
    public byte[] makePacket(String message){
        return message.getBytes();
    }
    public void udtSend(byte[] bytes){
        try (DatagramChannel datagramChannel = DatagramChannel.open();) {
//          datagramChannel.bind(new InetSocketAddress("localhost", 8882+j));
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            byteBuffer.put(bytes);
            byteBuffer.flip();
            datagramChannel.send(byteBuffer, new InetSocketAddress("localhost", 8882));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
