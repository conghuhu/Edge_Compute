package me.mason.springbatch.example.incrementReverse.config;

import cn.hutool.core.collection.CollUtil;
import me.mason.springbatch.common.SyncConstants;
import me.mason.springbatch.entity.WMedicalS;
import me.mason.springbatch.example.incrementReverse.listenner.IncrementReverseWMedicalSEndListener;
import me.mason.springbatch.example.incrementReverse.step.IncrementReverseWMedicalSItemProcessor;
import me.mason.springbatch.example.incrementReverse.step.IncrementReverseWMedicalSItemReader;
import me.mason.springbatch.example.incrementReverse.step.IncrementReverseWMedicalSItemWriter;
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
 * @time: 2022/3/6 16:37
 */
@Configuration
@EnableBatchProcessing
public class IncrementReverseWMedicalSBatchConfig {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job incrementReverseWMedicalSJob(Step incrementReverseWMedicalSStep, JobExecutionListener incrementReverseWMedicalSListener) {
        String funcName = Thread.currentThread().getStackTrace()[1].getMethodName();
        return jobBuilderFactory.get(funcName)
                .listener(incrementReverseWMedicalSListener)
                .flow(incrementReverseWMedicalSStep)
                .end().build();
    }

    @Bean
    public Step incrementReverseWMedicalSStep(ItemReader incrementReverseWMedicalSReader, ItemProcessor incrementReverseWMedicalSProcessor
            , ItemWriter incrementReverseWMedicalSWriter) {
        String funcName = Thread.currentThread().getStackTrace()[1].getMethodName();
        return stepBuilderFactory.get(funcName)
                .<WMedicalS, WMedicalS>chunk(10)
                .reader(incrementReverseWMedicalSReader)
                .processor(incrementReverseWMedicalSProcessor)
                .writer(incrementReverseWMedicalSWriter)
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
    public ItemReader incrementReverseWMedicalSReader(@Value("#{jobParameters['lastUpdateTime']}") String lastUpdateTime) {
        IncrementReverseWMedicalSItemReader incrementReverseWMedicalSItemReader = new IncrementReverseWMedicalSItemReader();
        //设置参数，当前示例可不设置参数
        Map<String, Object> params = CollUtil.newHashMap();
        params.put(SyncConstants.STR_LAST_UPDATE_TIME, lastUpdateTime);
        incrementReverseWMedicalSItemReader.setParams(params);

        return incrementReverseWMedicalSItemReader;
    }

    @Bean
    public ItemWriter incrementReverseWMedicalSWriter() {
        return new IncrementReverseWMedicalSItemWriter();
    }

    @Bean
    public ItemProcessor incrementReverseWMedicalSProcessor() {
        return new IncrementReverseWMedicalSItemProcessor();
    }

    @Bean
    public JobExecutionListener incrementReverseWMedicalSListener() {
        return new IncrementReverseWMedicalSEndListener();
    }
}
