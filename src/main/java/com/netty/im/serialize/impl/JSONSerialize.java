package com.netty.im.serialize.impl;

import com.alibaba.fastjson.JSON;
import com.netty.im.serialize.SerializeAlgorithm;
import com.netty.im.serialize.Serializer;

public class JSONSerialize implements Serializer {
    @Override
    public byte getSerializerAlgorithm() {
        return SerializeAlgorithm.JSON;
    }

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deSerialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);
    }
}
