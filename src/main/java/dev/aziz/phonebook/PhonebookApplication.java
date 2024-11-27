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
				userRepository.save(new User("4", "Michael", "Johnson", "1231231234", "michael.johnson@gmail.com"));
				userRepository.save(new User("5", "Emily", "Davis", "2342342345", "emily.davis@yahoo.com"));
				userRepository.save(new User("6", "David", "Martinez", "3453453456", "david.martinez@outlook.com"));
				userRepository.save(new User("7", "Sophia", "Garcia", "4564564567", "sophia.garcia@aol.com"));
				userRepository.save(new User("8", "James", "Miller", "5675675678", "james.miller@hotmail.com"));
				userRepository.save(new User("9", "Olivia", "Taylor", "6786786789", "olivia.taylor@gmail.com"));
				userRepository.save(new User("10", "William", "Anderson", "7897897890", "william.anderson@yahoo.com"));
				userRepository.save(new User("11", "Isabella", "Thomas", "8908908901", "isabella.thomas@outlook.com"));
				userRepository.save(new User("12", "Benjamin", "Jackson", "9019019012", "benjamin.jackson@aol.com"));
				userRepository.save(new User("13", "Charlotte", "White", "1021021023", "charlotte.white@hotmail.com"));
				userRepository.save(new User("14", "Ethan", "Harris", "2132132134", "ethan.harris@gmail.com"));
				userRepository.save(new User("15", "Amelia", "Clark", "3243243245", "amelia.clark@yahoo.com"));
				userRepository.save(new User("16", "Daniel", "Lewis", "4354354356", "daniel.lewis@outlook.com"));
				userRepository.save(new User("17", "Mia", "Young", "5465465467", "mia.young@aol.com"));
				userRepository.save(new User("18", "Matthew", "Walker", "6576576578", "matthew.walker@hotmail.com"));
				userRepository.save(new User("19", "Harper", "Allen", "7687687689", "harper.allen@gmail.com"));
				userRepository.save(new User("20", "Lucas", "Scott", "8798798790", "lucas.scott@yahoo.com"));
				userRepository.save(new User("21", "Chloe", "King", "9809809801", "chloe.king@outlook.com"));
				userRepository.save(new User("22", "Alexander", "Wright", "0910910912", "alexander.wright@aol.com"));
				userRepository.save(new User("23", "Grace", "Lopez", "2022022023", "grace.lopez@hotmail.com"));

				System.out.println("Sample users added to the database.");
			} else {
				System.out.println("Database already contains data. Skipping initialization.");
			}
		};
	}
}
