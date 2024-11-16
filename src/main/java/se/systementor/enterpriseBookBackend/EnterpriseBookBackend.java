package se.systementor.enterpriseBookBackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
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

		// Sample coordinates for a location
		double lat = 37.7749; // Latitude for San Francisco
		double lon = -122.4194; // Longitude for San Francisco

		// Fetch weather data using the ForecastService
		//String weatherData = String.valueOf(forecastService.getWeatherForecast(lat, lon));

		// Print the weather data response

		//System.out.println("Weather Data: " + weatherData);

		}



	/*
	private void listPrediction(){
		int num = 1;
		for (var forecast : forecastService.getForecasts()){
			System.out.printf("%d %d %d %n",
					num,
					forecast.getCity(),
					forecast.getDescription(),
					forecast.getTemperature()
					);
			num++;
		}
	}

	private void updatePrediction(Scanner scan) {
		listPrediction();
		System.out.printf("Ange vilken du vill uppdatera");
		int num = scan.nextInt();
		var forecast = forecastService.getByIndex(num-1);
		System.out.printf("&d %d Nuvarande: %f %n",
				forecast.getCity(),
				forecast.getDescription(),
				forecast.getTemperature()
				);
		System.out.printf("Ange ny temp: ");
		float temp = scan.nextFloat();
		forecast.setTemperature(temp);
		forecastService.update(forecast);
	}

	private void addPredictions(Scanner scan) {
		//Input på dag, hour, temp
		//Anropa servicen - save
		System.out.println("*** CREATE PREDICTION ***");
		System.out.printf("Ange vilekn dag:");
		int dag = scan.nextInt();
		System.out.print("Hour:");
		int hour = scan.nextInt();
		System.out.print("Temperature:");
		float temp = scan.nextFloat();

	    var forecast = new Forecast();
		forecast.setId(forecast.getId());
		forecast.setCity("");
		forecast.setDescription("");
		forecast.setTemperature(temp);
		forecastService.add(forecast);

	}
	*/

}
