/*
package com.sky.utils;

*/
/**
 * @Description: 布隆过滤器
 * @Author: 刘东钦
 * @Date: 2023/4/29 9:02
 *//*

public class BooLong {
    //基于redisson的布隆过滤器
    private RBloomFilter<Object> bloomFilter;
    private RMap<Object, Object> map;
    private RLock lock;
    //实现布隆过滤器,初始化布隆过滤器
    public BooLong(RedissonClient redissonClient) {
        bloomFilter = redissonClient.getBloomFilter("bloomFilter");
        //初始化布隆过滤器，预计元素为100000000L,误差率为3%
        bloomFilter.tryInit(100000000L, 0.03);
        map = redissonClient.getMap("map");
        lock = redissonClient.getLock("lock");
    }
}
*/
