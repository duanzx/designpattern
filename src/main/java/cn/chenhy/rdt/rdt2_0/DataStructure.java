package cn.chenhy.rdt.rdt2_0;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 定义发送的数据结构
 * */
@Data
@Builder
public class DataStructure implements Serializable {
    private static final long serialVersionUID = 896127972882129488L;
    private byte[] bytes;
    private long checkCode;
}
