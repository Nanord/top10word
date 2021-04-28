package com.stm.top10word.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@Slf4j
public class ExecutorConfiguration {
    @Value("${starter.thread.pool.size:6}")
    private Integer threadPoolSizeStarter;
    @Value("${worker.thread.pool.size:6}")
    private Integer threadPoolSizeWorker;
    @Value("${terminated.thread.pool.size:6}")
    private Integer threadPoolSizeTerminated;

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutorStarter() {
        return createExecutor("starter-", threadPoolSizeStarter);
    }


    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutorWorker() {
        return createExecutor("worker-", threadPoolSizeWorker);
    }

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutorTerminated() {
        return createExecutor("terminated-", threadPoolSizeTerminated);
    }

    private ThreadPoolTaskExecutor createExecutor(String prefix, Integer size) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskExecutor.setThreadNamePrefix(prefix);
        if(size == null || size < 1) {
            log.info("incorrect properties \"thread.pool.size\" for {}: {}", prefix, size);
            threadPoolTaskExecutor.setCorePoolSize(1);
            threadPoolTaskExecutor.setMaxPoolSize(1);
            return threadPoolTaskExecutor;
        }
        threadPoolTaskExecutor.setCorePoolSize(size);
        threadPoolTaskExecutor.setMaxPoolSize(size);
        threadPoolTaskExecutor.setDaemon(true);
        return threadPoolTaskExecutor;
    }
}
