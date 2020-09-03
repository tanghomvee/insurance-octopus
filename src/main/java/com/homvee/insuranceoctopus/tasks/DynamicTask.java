package com.homvee.insuranceoctopus.tasks;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.homvee.insuranceoctopus.dao.entities.ReqCfg;
import com.homvee.insuranceoctopus.enums.StateEnum;
import com.homvee.insuranceoctopus.service.DataService;
import com.homvee.insuranceoctopus.service.ReqCfgService;
import com.homvee.insuranceoctopus.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.joda.time.DateTime;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

//@Configuration      //1.主要用于标记配置类，兼备Component的效果。
//@EnableScheduling   // 2.开启定时任务
@Slf4j
@Component
//public class DynamicTask  implements SchedulingConfigurer {
public class DynamicTask  implements InitializingBean {

    @Value("${task.cron}")
    private String cron;

    private String domain = "http://157.122.153.67:9000/";
    @Resource
    private ReqCfgService reqCfgService;
    @Resource
    private DataService[] dataServices;

    static Map<Long , AtomicInteger> ERROR_CNT = Maps.newConcurrentMap();
 /*   @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(() -> {



        }, triggerContext -> new CronTrigger(cron).nextExecutionTime(triggerContext));

    }*/

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int processors = Runtime.getRuntime().availableProcessors();
                ArrayBlockingQueue queue = new ArrayBlockingQueue<>(50);
                ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(processors/2, processors*2,
                        5,TimeUnit.MINUTES,queue,
                        new ThreadPoolExecutor.CallerRunsPolicy()
                );
                execTask(threadPoolExecutor);
                while(!queue.isEmpty()){
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                threadPoolExecutor.shutdown();
            }
        }).start();

    }


    private void execTask(ThreadPoolExecutor threadPoolExecutor){
        int start = 0,pageSize = 100,page = 0;
        while (true){
            start = pageSize * page;
            List<ReqCfg> cfgs = reqCfgService.findByState(StateEnum.INIT,start);
            if (CollectionUtils.isEmpty(cfgs)){
                return;
            }
            page++;
            threadPoolExecutor.execute(() -> {
                for (ReqCfg reqCfg : cfgs){
                    Long st = System.currentTimeMillis();
                    DataService dataService = getDataService(reqCfg);
                    if (dataService == null){
                        log.warn("URL对应的数据处理类不存在:{}" , reqCfg.getUri());
                        continue;
                    }
                    String uri = reqCfg.getUri();
                    String param = reqCfg.getParam();
                    String url = domain + uri;
                    CookieStore cookieStore = new BasicCookieStore();
                    BasicClientCookie cookie = new BasicClientCookie("JSESSIONID",reqCfg.getCookie());
                    cookie.setDomain("157.122.153.67");
                    cookie.setPath("/");
                    cookieStore.addCookie(cookie);

                    String resp = null;
                    boolean isForm = MediaType.APPLICATION_FORM_URLENCODED_VALUE.equalsIgnoreCase(reqCfg.getContentType());

                    try{
                        resp = isForm ? HttpUtils.postForm(url + "?" + param,cookieStore) : HttpUtils.postJSON(url ,param,cookieStore);
                        ERROR_CNT.remove(reqCfg.getId());
                    }catch (Exception exception){
                        log.error("请求数据异常：cfg={}" , JSON.toJSONString(reqCfg));
                        doError(reqCfg);
                        continue;
                    }


                    dataService.exec(reqCfg, resp);
                }
            });

        }
    }


    private void doError(ReqCfg reqCfg) {
        AtomicInteger cnt = ERROR_CNT.get(reqCfg.getId());
        if (cnt == null){
            cnt = new AtomicInteger(1);
            ERROR_CNT.put(reqCfg.getId() , cnt);
        }
        if (cnt.incrementAndGet() < 10){
            return;
        }
        ERROR_CNT.remove(reqCfg.getId());
        reqCfgService.updateStateById(StateEnum.FAIL.name() , reqCfg.getId());
    }

    public DataService getDataService(ReqCfg cfg){
        for (DataService dataService : dataServices){
            if (dataService.me(cfg)){
                return dataService;
            }
        }
        return null;
    }


}
