package me.mason.springbatch.example.increment.config;

import cn.hutool.core.collection.CollUtil;
import me.mason.springbatch.common.SyncConstants;
import me.mason.springbatch.entity.WMedicalS;
import me.mason.springbatch.example.increment.listener.IncrementWMedicalSEndListener;
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
 * @time: 2022/3/6 14:31
 */
@Configuration
@EnableBatchProcessing
public class IncrementWMedicalSBatchConfig {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job incrementWMedicalSJob(Step incrementWMedicalSStep, JobExecutionListener incrementWMedicalSListener){
        String funcName = Thread.currentThread().getStackTrace()[1].getMethodName();
        return jobBuilderFactory.get(funcName)
                .listener(incrementWMedicalSListener)
                .flow(incrementWMedicalSStep)
                .end().build();
    }
    @Bean
    public Step incrementWMedicalSStep(ItemReader incrementWMedicalSReader , ItemProcessor incrementWMedicalSProcessor
            , ItemWriter incrementWMedicalSWriter){
        String funcName = Thread.currentThread().getStackTrace()[1].getMethodName();
        return stepBuilderFactory.get(funcName)
                .<WMedicalS,WMedicalS>chunk(10)
                .reader(incrementWMedicalSReader)
                .processor(incrementWMedicalSProcessor)
                .writer(incrementWMedicalSWriter)
                .build();
    }

    /**
     * ????????????????????????????????????????????????????????????????????????????????????{@link StepScope}??????,
     * @param lastUpdateTime sql?????????????????????????????????
     * @return
     */
    @Bean
    @StepScope
    public ItemReader incrementWMedicalSReader(@Value("#{jobParameters['lastUpdateTime']}") String lastUpdateTime) {
        IncrementWMedicalSItemReader incrementWMedicalSItemReader = new IncrementWMedicalSItemReader();
        //?????????????????????????????????????????????
        Map<String,Object> params = CollUtil.newHashMap();
        params.put(SyncConstants.STR_LAST_UPDATE_TIME,lastUpdateTime);
        incrementWMedicalSItemReader.setParams(params);

        return incrementWMedicalSItemReader;
    }

    @Bean
    public ItemWriter incrementWMedicalSWriter() {
        return new IncrementWMedicalSItemWriter();
    }

    @Bean
    public ItemProcessor incrementWMedicalSProcessor(){
        return new IncrementWMedicalSItemProcessor();
    }

    @Bean
    public JobExecutionListener incrementWMedicalSListener(){
        return new IncrementWMedicalSEndListener();
    }
}
