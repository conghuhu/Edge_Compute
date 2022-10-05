package me.mason.springbatch.example.incrementReverse.config;

import cn.hutool.core.collection.CollUtil;
import me.mason.springbatch.common.SyncConstants;
import me.mason.springbatch.entity.Prescription;
import me.mason.springbatch.example.incrementReverse.listenner.IncrementReversePrescriptionEndListener;
import me.mason.springbatch.example.incrementReverse.step.*;
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
 * @time: 2022/3/7 10:43
 */
@Configuration
@EnableBatchProcessing
public class IncrementReversePrescriptionBatchConfig {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job incrementReversePrescriptionJob(Step incrementReversePrescriptionStep, JobExecutionListener incrementReversePrescriptionListener) {
        String funcName = Thread.currentThread().getStackTrace()[1].getMethodName();
        return jobBuilderFactory.get(funcName)
                .listener(incrementReversePrescriptionListener)
                .flow(incrementReversePrescriptionStep)
                .end().build();
    }

    @Bean
    public Step incrementReversePrescriptionStep(ItemReader incrementReversePrescriptionReader, ItemProcessor incrementReversePrescriptionProcessor
            , ItemWriter incrementReversePrescriptionWriter) {
        String funcName = Thread.currentThread().getStackTrace()[1].getMethodName();
        return stepBuilderFactory.get(funcName)
                .<Prescription, Prescription>chunk(10)
                .reader(incrementReversePrescriptionReader)
                .processor(incrementReversePrescriptionProcessor)
                .writer(incrementReversePrescriptionWriter)
                .build();
    }

    /**
     * 根据传入的参数读取数据，直接使用字符串模板，注意需要添加{@link StepScope}注解,
     *
     * @param lastUpdateTime sql参数，上次最后更新时间
     * @return
     */
    @Bean
    @StepScope
    public ItemReader incrementReversePrescriptionReader(@Value("#{jobParameters['lastUpdateTime']}") String lastUpdateTime) {
        IncrementReversePrescriptionItemReader incrementReversePrescriptionItemReader = new IncrementReversePrescriptionItemReader();
        //设置参数，当前示例可不设置参数
        Map<String, Object> params = CollUtil.newHashMap();
        params.put(SyncConstants.STR_LAST_UPDATE_TIME, lastUpdateTime);
        incrementReversePrescriptionItemReader.setParams(params);

        return incrementReversePrescriptionItemReader;
    }

    @Bean
    public ItemWriter incrementReversePrescriptionWriter() {
        return new IncrementReversePrescriptionItemWriter();
    }

    @Bean
    public ItemProcessor incrementReversePrescriptionProcessor() {
        return new IncrementReversePrescriptionItemProcessor();
    }

    @Bean
    public JobExecutionListener incrementReversePrescriptionListener() {
        return new IncrementReversePrescriptionEndListener();
    }
}


