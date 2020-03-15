package cn.chenhy.rdt.rdt2_0;

import cn.chenhy.rdt.CRCUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.UUID;

/**
 * 引入ARQ
 * 差错检测
 * 接收方反馈
 * 重传
 * */
public class Sender {
    private static final Gson gson;
    static {
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }

    public static void main(String[] args)throws Exception{
        Sender sender = new Sender();
        String message = UUID.randomUUID().toString().replace("-","").toUpperCase();
        long checkCode = CRCUtil.getCheckCode(message.getBytes());
        System.out.println("checkCode: "+checkCode);
        System.out.println("message: "+message);
        sender.rdtSend(message,checkCode);
    }
    public void rdtSend(String message,long checkCode){
        DataStructure dataStructure = DataStructure.builder()
                .bytes(message.getBytes())
                .checkCode(checkCode)
                .build();
        udtSend(makePacket(gson.toJson(dataStructure)));
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
            while (datagramChannel.isOpen()) {
                byteBuffer.clear();
                SocketAddress receiveAddress = datagramChannel.receive(byteBuffer);
                byteBuffer.flip();
                byte[] arr = new byte[byteBuffer.remaining()];
                byteBuffer.get(arr);
                String result = new String(arr);
                if ("1".equals(result)){
                    System.out.println("ACK");
                }else {
                    System.out.println("NAK");
                    byteBuffer.clear();
                    byteBuffer.put(bytes);
                    byteBuffer.flip();
                    datagramChannel.send(byteBuffer,receiveAddress);
                }
                datagramChannel.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
