package hh.sof3.toWatch.utils;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("Job is starting...");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            System.out.println("Job completed successfully.");
        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            System.err.println("Job failed with exceptions.");
        }
    }
}