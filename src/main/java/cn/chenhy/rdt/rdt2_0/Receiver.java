package cn.chenhy.rdt.rdt2_0;

import cn.chenhy.rdt.CRCUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class Receiver {
    private static final Gson gson;
    static {
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }
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
                deliverData(serverChannel,senderAddress, byteBuffer);
            }
        }
    }

    public void deliverData(DatagramChannel serverChannel,SocketAddress senderAddress, ByteBuffer byteBuffer)throws Exception {
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes,0,byteBuffer.remaining());
        DataStructure dataStructure = gson.fromJson(new String(bytes),DataStructure.class);
        long checkCode = CRCUtil.getCheckCode(dataStructure.getBytes());
        System.out.println("sender: checkCode: "+dataStructure.getCheckCode()+", message: "+new String(dataStructure.getBytes()));
        System.out.println("receiver : checkCode: "+checkCode);
        if (checkCode != dataStructure.getCheckCode()){
            byteBuffer.clear();
            byteBuffer.put("0".getBytes());
            byteBuffer.flip();
            serverChannel.send(byteBuffer,senderAddress);
        }else {
//            System.out.println("receive: " + senderAddress.toString());
//            System.out.println("data: " + new String(dataStructure.getBytes()));
            byteBuffer.clear();
            byteBuffer.put("1".getBytes());
            byteBuffer.flip();
            serverChannel.send(byteBuffer,senderAddress);
        }
    }

}
