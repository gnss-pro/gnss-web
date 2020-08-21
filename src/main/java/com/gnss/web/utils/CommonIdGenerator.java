package com.gnss.web.utils;

import com.dangdang.ddframe.rdb.sharding.id.generator.IdGenerator;
import com.dangdang.ddframe.rdb.sharding.id.generator.self.CommonSelfIdGenerator;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>Description: ID生成器</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019/2/14
 */
public class CommonIdGenerator implements IdentifierGenerator {

    private static IdGenerator idGenerator = new CommonSelfIdGenerator();

    /**
     * Integer ID自增器
     */
    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    /**
     * 生成long型ID
     *
     * @return
     */
    public static long generate() {
        return idGenerator.generateId().longValue();
    }

    /**
     * 生成int型ID
     *
     * @return
     */
    public static int generateInt() {
        int current;
        int next;
        do {
            current = atomicInteger.get();
            next = current >= Integer.MAX_VALUE ? 1 : current + 1;
        } while (!atomicInteger.compareAndSet(current, next));
        return next;
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor s, Object obj) throws HibernateException {
        Serializable id = s.getEntityPersister(null, obj).getClassMetadata().getIdentifier(obj, s);
        if (id != null && Long.valueOf(id.toString()) > 0) {
            return id;
        }
        return idGenerator.generateId().longValue();
    }
}
