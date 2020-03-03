package vn.edu.vnu.uet.dktadmin.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BaseController {
    @Autowired
    protected ExecutorService portalExecutorService;

    @Bean
    public ExecutorService portalExecutorService() {
        return Executors.newFixedThreadPool(5);
    }
}
