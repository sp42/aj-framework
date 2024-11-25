package com.ajaxjs.data.util;

/**
 * 雪花生成器
 * <p>
 * 默认其生成的 Long 主键是 28 位；但 JS Long 最大值是 16 位（Java Long 没此问题） 这个版本则是生成 16 位的
 *
 * @author <a href="https://www.cnblogs.com/yangzhilong/p/10290862.html">...</a>
 */
public class SnowflakeId {
    /**
     * 时间起始标记点，作为基准，一般取系统的最近时间 此处以2018-01-01为基准时间
     */
    private final long epoch = 1514736000000L;

    /**
     * 机器标识位数
     */
    private final long workerIdBits = 4L;

    /**
     * 毫秒内自增位
     */
    private final long sequenceBits = 12L;

    /**
     * 机器ID最大值:16
     */
    private final long maxWorkerId = ~(-1L << workerIdBits);

    private final long workerIdShift = sequenceBits;
    private final long timestampLeftShift = sequenceBits + workerIdBits;
    private final long sequenceMask = ~(-1L << sequenceBits);

    private final long workerId;

    /**
     * 并发控制
     */
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    public SnowflakeId(long workerId) {
        if (workerId > maxWorkerId || workerId < 0)
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));

        this.workerId = workerId;
    }

    /**
     * 获得下一个 ID (该方法是线程安全的)
     *
     * @return 雪花 ID
     */
    public synchronized long nextId() {
        long timestamp = System.currentTimeMillis();

        if (lastTimestamp == timestamp) {
            // 如果上一个 timestamp 与新产生的相等，则 sequence 加一(0-4095 循环);
            // 对新的 timestamp，sequence 从0开始
            sequence = sequence + 1 & sequenceMask;

            if (sequence == 0)
                timestamp = tilNextMillis(lastTimestamp);// 重新生成 timestamp
        } else
            sequence = 0;

        if (timestamp < lastTimestamp)
            throw new RuntimeException(String.format("clock moved backwards.Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));

        lastTimestamp = timestamp;

        return timestamp - epoch << timestampLeftShift | workerId << workerIdShift | sequence;
    }

    /**
     * 获取时间戳，并与上次时间戳比较。
     * 等待下一个毫秒的到来， 保证返回的毫秒数在参数 lastTimestamp 之后
     *
     * @param lastTimestamp 上一个毫秒时间戳，作为比较的基准。
     * @return 当前时间戳，保证大于输入的lastTimestamp。
     */
    public static long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();

        while (timestamp <= lastTimestamp)
            timestamp = System.currentTimeMillis();

        return timestamp;
    }

    /**
     * 实例
     */
    private final static SnowflakeId INSTANCE = new SnowflakeId(1L);

    /**
     * 生成 id
     *
     * @return 雪花 id
     */
    public static synchronized long get() {
        return INSTANCE.nextId();
    }
}
