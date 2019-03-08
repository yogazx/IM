package com.netty.im.serialize;

public interface Serializer {

    /**
     * 采用哪种序列化算法
     * @return
     */
    byte getSerializerAlgorithm();

    /**
     * Java对象转二进制
     * @param object
     * @return
     */
    byte[] serialize(Object object);

    /**
     * 二进制转Java对象
     * @param clazz
     * @param bytes
     * @param <T>
     * @return
     */
    <T> T deSerialize(Class<T> clazz, byte[] bytes);
}
