package com.rehoshi.simple.business.net.retrofit_2;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by hoshino on 2019/1/15.
 * 批量请求发送器
 */
public abstract class BatchRequester<S, T> {

    private Map<T, S> queryMapper = new HashMap<>();

    public void query(List<T> list) {
        if (list != null) {
            Iterator<T> iterator = list.iterator();
            int index = 0;
            while (iterator.hasNext()) {
                T next = iterator.next();
                S sender = createSender(next, index);
                sendQuery(sender, index);
                queryMapper.put(next, sender);
                index++;
            }
        }
    }

    protected abstract S createSender(T param, int index);

    protected abstract void sendQuery(S sender, int index);

}
