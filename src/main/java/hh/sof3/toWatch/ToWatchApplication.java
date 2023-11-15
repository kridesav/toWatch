package hh.sof3.toWatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
       String adminUsername = System.getenv("ADMIN_USERNAME");
        String adminPassword = System.getenv("ADMIN_PASSWORD");
        String adminRole = "ADMIN";
        String hashedPassword = new BCryptPasswordEncoder().encode(adminPassword);
        User adminUser = new User(adminUsername, hashedPassword, adminRole);
        userRepository.save(adminUser);

        String userUsername = System.getenv("USER_USERNAME");
        String userPassword = System.getenv("USER_PASSWORD");
        String userRole = "USER";
        String hashedPassword2 = new BCryptPasswordEncoder().encode(userPassword);
        User userUser = new User(userUsername, hashedPassword2, userRole);
        userRepository.save(userUser);

        JobParameters params = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(job, params);
    }
}
