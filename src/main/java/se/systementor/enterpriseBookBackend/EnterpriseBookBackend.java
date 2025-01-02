package se.systementor.enterpriseBookBackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import se.systementor.enterpriseBookBackend.services.BookService;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})



public class EnterpriseBookBackend implements CommandLineRunner {
    @Autowired
	private BookService bookService;

	public static void main(String[] args) {
		SpringApplication.run(EnterpriseBookBackend.class, args);
	}

	@Override
	public void run(String... args) throws Exception {


		}

}
