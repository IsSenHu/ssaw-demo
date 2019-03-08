package com.ssaw.ssawauthenticatecenterservice.handler;


import com.ssaw.commons.util.json.jack.JsonUtils;
import com.ssaw.log.collect.handler.LogHandler;
import com.ssaw.log.collect.vo.Log;
import org.springframework.stereotype.Component;

/**
 * @author HuSen
 * @date 2019/3/8 10:00
 */
@Component
public class ClientLogHandler implements LogHandler {

    @Override
    public String formatLog(Log log) {
        return JsonUtils.object2JsonString(log);
    }

    @Override
    public Log setLogBaseInfo(String logType, Object o) {
        Log log = new Log();
        log.setType(logType);
        log.setLog(o);
        return log;
    }

    @Override
    public Log setLogBaseInfo(String logType, Object o, String message) {
        Log log = new Log();
        log.setType(logType);
        log.setLog(o);
        log.setMessage(message);
        return log;
    }
}