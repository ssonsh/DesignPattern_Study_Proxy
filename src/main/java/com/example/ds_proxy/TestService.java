package com.example.ds_proxy;

import org.springframework.stereotype.Service;

@Service
public class TestService {

    public String read() {
        sleep(2000);
        return "Read Complete";
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
