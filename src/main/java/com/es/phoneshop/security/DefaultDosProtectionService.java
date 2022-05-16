package com.es.phoneshop.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDosProtectionService implements DosProtectionService {
    private static final long THRESHOLD = 30;

    private Map<String, Long> countMap = new ConcurrentHashMap<>();

    private static class SingletonHelper {
        private static final DefaultDosProtectionService INSTANCE = new DefaultDosProtectionService();
    }

    public static DefaultDosProtectionService getInstance() {
        return DefaultDosProtectionService.SingletonHelper.INSTANCE;
    }

    @Override
    public boolean isAllowed(String ip) {
        Long count = countMap.get(ip);

        if(count == null) {
            count = 1L;
        } else {
            if(count > THRESHOLD){
                return false;
            }

            count++;
        }
        countMap.put(ip, count);

        return true;
    }
}