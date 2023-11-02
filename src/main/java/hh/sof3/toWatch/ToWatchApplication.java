package hh.sof3.toWatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import hh.sof3.toWatch.models.User;
import hh.sof3.toWatch.repositories.UserRepository;

@SpringBootApplication
public class ToWatchApplication implements CommandLineRunner {

    private JobLauncher jobLauncher;
    private Job job;

    @Autowired
    private UserRepository userRepository;

    public ToWatchApplication(JobLauncher jobLauncher, Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    public static void main(String[] args) {
        SpringApplication.run(ToWatchApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Insert user data into the database
        User user1 = new User("user", "$2a$12$QkYGF6nkoi8D4UCiDKSPr.gr0Xw1h9boKeyky0goH3FXBecJS6TAq", "USER");
        User user2 = new User("admin", "$2a$12$Rg9G9n6KevzjGCW0wiDANuh0hoXoHFuhXyguk/PC4fFDGjouMA.8.", "ADMIN");
        userRepository.save(user1);
        userRepository.save(user2);

        // Run the Spring Batch job
        JobParameters params = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(job, params);
    }
}
