package com.es.phoneshop.security;

import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.TimerTask;

public class DefaultDosProtectionService implements DosProtectionService {
    private static final long THRESHOLD = 20;
    private static final DefaultDosProtectionService INSTANCE = new DefaultDosProtectionService();

    private Map<String, Long> countMap = new ConcurrentHashMap<>();

    public static DefaultDosProtectionService getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean isAllowed(String ip) {
        Long count = countMap.get(ip);
        final boolean[] temp = {true};

        if (count == null) {
            count = 1L;
        } else {
            Timer timer = new Timer();
            Long finalCount = count;

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (finalCount > THRESHOLD) {
                        temp[0] = false;
                    }
                }
            }, 0, 60 * 1000);

            count++;
        }
        countMap.put(ip, count);

        return temp[0];
    }
}
