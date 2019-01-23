package com.ssaw.ssawconsumerdemo.master;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author HuSen.
 * @date 2019/1/17 13:36.
 */
public class Master {
    /** 任务队列 */
    private Queue<Object> workQueue = new ConcurrentLinkedDeque<>();
    /** worker */
    private Map<String, Thread> workers = new ConcurrentHashMap<>();
    /** 结果集 */
    private Map<String, Object> resultMap = new ConcurrentHashMap<>();

    /**
     * 是否所有子任务都结束
     * @return 是否所有子任务都结束
     */
    private boolean isComplete() {
        for(Map.Entry<String, Thread> entry : workers.entrySet()) {
            if(entry.getValue().getState() != Thread.State.TERMINATED) {
                return false;
            }
        }
        return true;
    }

    private Master(Worker worker, int count) {
        worker.setResultMap(resultMap);
        worker.setWorkQueue(workQueue);
        for (int i = 0; i < count; i++) {
            workers.put(Integer.toString(i), new Thread(worker, Integer.toString(i)));
        }
    }

    private void submit(Object obj) {
        workQueue.add(obj);
    }

    private Map<String, Object> getResultMap() {
        return resultMap;
    }

    private void execute() {
        for(Map.Entry<String, Thread> entry : workers.entrySet()) {
            entry.getValue().start();
        }
    }

    public static void main(String[] args) {
        Master master = new Master(new Worker(), 100);
        for(int i = 1; i <= 10000000; i++) {
            master.submit(i);
        }
        master.execute();
        int i = 0;
        while (true) {
            if(master.isComplete()) {
                Map<String, Object> resultMap = master.getResultMap();
                for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
                    i += (Integer) entry.getValue();
                }
                if (i > 0) {
                    break;
                }
            }
        }
        System.out.println(i);
    }
}
