package com.example.dut_health_app;

public interface ResponseCallBack {

    void onResponse(String response);

    void onError(Throwable throwable);
}