package dev.aziz.phonebook;

import dev.aziz.phonebook.entity.User;
import dev.aziz.phonebook.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PhonebookApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhonebookApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(UserRepository userRepository) {
		return args -> {
			if (userRepository.count() == 0) {
				userRepository.save(new User("1", "John", "Doe", "1234567890", "johndoe@gmail.com"));
				userRepository.save(new User("2", "Jane", "Smith", "987654320", "jsmith@gmail.com"));
				userRepository.save(new User("3", "Alice", "Brown","555555555", "alicebrown@gmail.com"));
				System.out.println("Sample users added to the database.");
			} else {
				System.out.println("Database already contains data. Skipping initialization.");
			}
		};
	}
}
