package com.netty.im.serialize;

/**
 * 定义不同的序列化算法序号
 */
public interface SerializeAlgorithm {

    /**
     * 采用json序列化算法
     */
    byte JSON = 1;

    /**
     * 采用XML序列化
     */
    byte XML = 2;
}
