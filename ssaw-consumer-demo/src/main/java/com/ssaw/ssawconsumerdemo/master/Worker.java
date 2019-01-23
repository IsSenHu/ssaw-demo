package com.ssaw.ssawconsumerdemo.master;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author HuSen.
 * @date 2019/1/17 13:42.
 */
public class Worker implements Runnable {
    private Queue<Object> workQueue;
    private Map<String, Object> resultMap = new ConcurrentHashMap<>();

    public Queue<Object> getWorkQueue() {
        return workQueue;
    }

    public void setWorkQueue(Queue<Object> workQueue) {
        this.workQueue = workQueue;
    }

    public Map<String, Object> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<String, Object> resultMap) {
        this.resultMap = resultMap;
    }

    private Object handle(Object object) {
        return object;
    }

    @Override
    public void run() {
        while (true) {
            Object input = workQueue.poll();
            if(null == input) {
                break;
            }
            Object o = handle(input);
            resultMap.put(Integer.toString(o.hashCode()), o);
        }
    }
}
