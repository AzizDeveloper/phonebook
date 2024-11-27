package dev.aziz.phonebook;

import dev.aziz.phonebook.entity.Item;
import dev.aziz.phonebook.entity.User;
import dev.aziz.phonebook.repository.ItemRepository;
import dev.aziz.phonebook.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class PhonebookApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhonebookApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(UserRepository userRepository, ItemRepository itemRepository) {
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
			}
			if (itemRepository.count() == 0) {
				itemRepository.save(new Item("1", "Screw", 1000, "Units", BigDecimal.valueOf(150)));
				itemRepository.save(new Item("2", "Nail", 5000, "Units", BigDecimal.valueOf(50)));
				itemRepository.save(new Item("3", "Hammer", 200, "Units", BigDecimal.valueOf(1200)));
				itemRepository.save(new Item("4", "Drill Machine", 50, "Units", BigDecimal.valueOf(4500)));
				itemRepository.save(new Item("5", "Wood Plank", 100, "Units", BigDecimal.valueOf(800)));
				itemRepository.save(new Item("6", "Cement Bag", 300, "Units", BigDecimal.valueOf(350)));
				itemRepository.save(new Item("7", "Brick", 5000, "Units", BigDecimal.valueOf(5)));
				itemRepository.save(new Item("8", "Steel Rod", 100, "Units", BigDecimal.valueOf(1500)));
				itemRepository.save(new Item("9", "Sand", 2000, "Kilograms", BigDecimal.valueOf(50)));
				itemRepository.save(new Item("10", "Gravel", 1500, "Kilograms", BigDecimal.valueOf(60)));
				itemRepository.save(new Item("11", "Paint Bucket", 100, "Units", BigDecimal.valueOf(900)));
				itemRepository.save(new Item("12", "Ladder", 50, "Units", BigDecimal.valueOf(2500)));
				itemRepository.save(new Item("13", "Saw", 100, "Units", BigDecimal.valueOf(500)));
				itemRepository.save(new Item("14", "Shovel", 150, "Units", BigDecimal.valueOf(400)));
				itemRepository.save(new Item("15", "Trowel", 200, "Units", BigDecimal.valueOf(250)));
				itemRepository.save(new Item("16", "Plumbing Pipe", 300, "Meters", BigDecimal.valueOf(100)));
				itemRepository.save(new Item("17", "PVC Glue", 200, "Units", BigDecimal.valueOf(150)));
				itemRepository.save(new Item("18", "Tile", 400, "Square Meters", BigDecimal.valueOf(300)));
				itemRepository.save(new Item("19", "Tile Adhesive", 100, "Bags", BigDecimal.valueOf(350)));
				itemRepository.save(new Item("20", "Glass Sheet", 50, "Units", BigDecimal.valueOf(2000)));
				itemRepository.save(new Item("21", "Door Handle", 150, "Units", BigDecimal.valueOf(120)));
				itemRepository.save(new Item("22", "Hinges", 300, "Units", BigDecimal.valueOf(60)));
				itemRepository.save(new Item("23", "Plaster of Paris", 200, "Bags", BigDecimal.valueOf(250)));
				itemRepository.save(new Item("24", "Electrical Wire", 500, "Meters", BigDecimal.valueOf(15)));
				itemRepository.save(new Item("25", "Switchboard", 200, "Units", BigDecimal.valueOf(300)));
				itemRepository.save(new Item("26", "Breaker Panel", 50, "Units", BigDecimal.valueOf(4000)));
				itemRepository.save(new Item("27", "Roof Sheet", 100, "Units", BigDecimal.valueOf(2000)));
				itemRepository.save(new Item("28", "Concrete Mixer", 10, "Units", BigDecimal.valueOf(15000)));
				itemRepository.save(new Item("29", "Safety Helmet", 200, "Units", BigDecimal.valueOf(500)));
				itemRepository.save(new Item("30", "Safety Gloves", 300, "Pairs", BigDecimal.valueOf(100)));
				itemRepository.save(new Item("31", "Welding Rod", 400, "Units", BigDecimal.valueOf(75)));

			}
			else {
				System.out.println("Database already contains data. Skipping initialization.");
			}
		};
	}
}
