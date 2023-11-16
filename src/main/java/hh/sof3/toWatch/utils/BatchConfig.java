package hh.sof3.toWatch.utils;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.classify.Classifier;
import hh.sof3.toWatch.components.MovieItemWriter;
import hh.sof3.toWatch.components.TVShowItemWriter;
import hh.sof3.toWatch.models.Media;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Value("${file.input}")
    private String fileInput;

    @Bean
    public FlatFileItemReader<Media> reader() {
        FlatFileItemReader<Media> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(fileInput));
        reader.setLinesToSkip(1);

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("showId", "type", "title", "director", "cast", "country", "dateAdded", "releaseYear",
                "rating", "duration", "listedIn", "description");
        tokenizer.setDelimiter(",");
        reader.setLineMapper(new DefaultLineMapper<Media>() {
            {
                setLineTokenizer(tokenizer);
                setFieldSetMapper(new BeanWrapperFieldSetMapper<Media>() {
                    {
                        setTargetType(Media.class);
                    }
                });
            }
        });
        return reader;
    }

    @Bean
    public Classifier<Media, ItemWriter<? super Media>> classifier() {
        return new Classifier<Media, ItemWriter<? super Media>>() {
            @Override
            public ItemWriter<? super Media> classify(Media media) {
                if ("Movie".equals(media.getType())) {
                    return movieWriter(dataSource());
                } else if ("TV Show".equals(media.getType())) {
                    return tvShowWriter(dataSource());
                }
                return null;
            }
        };
    }

    @Bean
    public ItemWriter<Media> movieWriter(DataSource dataSource) {
        return new MovieItemWriter(dataSource);
    }

    @Bean
    public ItemWriter<Media> tvShowWriter(DataSource dataSource) {
        return new TVShowItemWriter(dataSource);
    }

    @Bean
    public ClassifierCompositeItemWriter<Media> classifierCompositeWriter() {
        ClassifierCompositeItemWriter<Media> compositeItemWriter = new ClassifierCompositeItemWriter<>();
        compositeItemWriter.setClassifier(classifier());
        return compositeItemWriter;
    }

    @Bean
    public ItemProcessor<Media, Media> mediaItemProcessor() {
        return new MediaItemProcessor();
    }

    @Bean
    public Job importMediaJob(JobRepository jobRepository, JobCompletionNotificationListener listener, Step step1) {
        return new JobBuilder("importMediaJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
            ClassifierCompositeItemWriter<Media> classifierCompositeWriter) {
        return new StepBuilder("step1", jobRepository)
                .<Media, Media>chunk(10, transactionManager)
                .reader(reader())
                .processor(mediaItemProcessor())
                .writer(classifierCompositeWriter())
                .build();
    }

    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder embeddedDatabaseBuilder = new EmbeddedDatabaseBuilder();
        return embeddedDatabaseBuilder.addScript("classpath:org/springframework/batch/core/schema-drop-h2.sql")
                .addScript("classpath:org/springframework/batch/core/schema-h2.sql")
                .setType(EmbeddedDatabaseType.H2)
                .build();
    }
}
