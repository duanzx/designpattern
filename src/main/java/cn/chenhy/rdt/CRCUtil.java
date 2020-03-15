package cn.chenhy.rdt;

import java.util.zip.CRC32;

/**
 * CRC32原理：https://www.cnblogs.com/bugutian/p/6221783.html
 * 生成多项式： x^4 + x^3 +  1 ，求 10110011 的校验码
 * 根据多项式求出除数： 根据多项式系数得到二进制位 ， 1*2^4 + 1*2^3 + 0*2^2 + 0*2^1 + 1*2^0 , 二进制位=11001
 * 采用异或运算得出：101100110000 ^ 11001 , 的余数：0100
 * 0000 因为校验码最高项位4(x^4),所以10110011后加0000
 * 10110011 + 校验码 = 101100110100
 * CRC^101100110000得到101100110100。发送到接收端；
 * 接收端收到101100110100后除以11001(以“模2除法”方式去除),余数为0则无差错；
 */
public class CRCUtil {
    public static void main(String[] args) throws Exception {
        System.out.println(CRCUtil.getCheckCode("FCDA84DCE41E4A1D8939C789895A3233".getBytes()));
        System.out.println(CRCUtil.getCheckCode("FCDA84DCE41E4A1D8939C789895A3233".getBytes()));
        System.out.println(CRCUtil.getCheckCode("FCDA84DCE41E4A1D8939C789895A3233".getBytes()));
    }

    private static final CRC32 crc32;

    static {
        crc32 = new CRC32();
    }

    public static long getCheckCode(byte[] bytes) {
        crc32.reset();
        crc32.update(bytes);
        return crc32.getValue();
    }
}
