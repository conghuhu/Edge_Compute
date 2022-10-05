package me.mason.springbatch.example.increment.config;

import cn.hutool.core.collection.CollUtil;
import me.mason.springbatch.common.SyncConstants;
import me.mason.springbatch.entity.Prescription;
import me.mason.springbatch.example.increment.listener.IncrementPrescriptionEndListener;
import me.mason.springbatch.example.increment.step.*;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @description:
 * @author: WYS
 * @time: 2022/3/7 9:54
 */
@Configuration
@EnableBatchProcessing
public class IncrementPrescriptionBatchConfig {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job incrementPrescriptionJob(Step incrementPrescriptionStep, JobExecutionListener incrementPrescriptionListener){
        String funcName = Thread.currentThread().getStackTrace()[1].getMethodName();
        return jobBuilderFactory.get(funcName)
                .listener(incrementPrescriptionListener)
                .flow(incrementPrescriptionStep)
                .end().build();
    }
    @Bean
    public Step incrementPrescriptionStep(ItemReader incrementPrescriptionReader , ItemProcessor incrementPrescriptionProcessor
            , ItemWriter incrementPrescriptionSWriter){
        String funcName = Thread.currentThread().getStackTrace()[1].getMethodName();
        return stepBuilderFactory.get(funcName)
                .<Prescription,Prescription>chunk(10)
                .reader(incrementPrescriptionReader)
                .processor(incrementPrescriptionProcessor)
                .writer(incrementPrescriptionSWriter)
                .build();
    }

    /**
     * 根据传入的参数读取数据，直接使用字符串模板，注意需要添加{@link StepScope}注解,
     * @param lastUpdateTime sql参数，上次最后更新时间
     * @return
     */
    @Bean
    @StepScope
    public ItemReader incrementPrescriptionReader(@Value("#{jobParameters['lastUpdateTime']}") String lastUpdateTime) {
        IncrementPrescriptionItemReader incrementPrescriptionItemReader = new IncrementPrescriptionItemReader();
        //设置参数，当前示例可不设置参数
        Map<String,Object> params = CollUtil.newHashMap();
        params.put(SyncConstants.STR_LAST_UPDATE_TIME,lastUpdateTime);
        incrementPrescriptionItemReader.setParams(params);

        return incrementPrescriptionItemReader;
    }

    @Bean
    public ItemWriter incrementPrescriptionSWriter() {
        return new IncrementPrescriptionItemWriter();
    }

    @Bean
    public ItemProcessor incrementPrescriptionProcessor(){
        return new IncrementPrescriptionItemProcessor();
    }

    @Bean
    public JobExecutionListener incrementPrescriptionListener(){
        return new IncrementPrescriptionEndListener();
    }
}

