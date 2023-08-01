package expenses.cli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.shell.command.annotation.CommandScan;

import java.time.LocalDate;
import java.util.Set;

@SpringBootApplication
@CommandScan
public class CliApplication {

	public static void main(String[] args) {
		SpringApplication.run(CliApplication.class, args);
	}

	@Bean
	public Converter<String, LocalDate> stringToLocalDate() {
		return new StringToLocalDate();
	}

	@Bean
	public Converter<String, Set<Tag>> stringToTagSet() {
		return new StringToTagSet();
	}
}
