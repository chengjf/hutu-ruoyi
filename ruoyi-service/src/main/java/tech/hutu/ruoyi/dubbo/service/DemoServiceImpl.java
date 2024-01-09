package tech.hutu.ruoyi.dubbo.service;

import tech.hutu.ruoyi.dubbo.api.DemoService;

import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class DemoServiceImpl implements DemoService {

    @Override
    public String sayHello(String name) {
        return "Hello haha" + name;
    }
}