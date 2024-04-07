import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import netscape.javascript.JSObject;

public class WeatherApp {
	private static JFrame frame;
	private static JTextField locationField;
	private static JButton fetchButton;
	private static JTextArea weatherDisplay;
	private static String apiKey = "af8212c7e50d839668dc3593fe27a8ba";
	
	private static String fecthWeatherData(String city) {
		try {
			URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String response = "";
			String line;
			while((line = reader.readLine()) != null) {
				response += line;
			}
			reader.close();
			
			JSONObject jsonObject = (JSONObject) JSONValue.parse(response.toString());
			JSONObject mainObject = (JSONObject) jsonObject.get("main");
			
			double temperatureK = (double) mainObject.get("temp");
			long humidity = (long) mainObject.get("humidity");
			
			double temperatureC = temperatureK - 273.15;
			
			JSONArray weatherArray = (JSONArray) jsonObject.get("weather");
			JSONObject weather = (JSONObject) weatherArray.get(0);
			
			String description = (String) weather.get("description");
					
			return "Description: " + description + "\n" + "Temperature: " + temperatureC + " Celcius\nHumiduty: " + humidity + "%";
			
		} catch (Exception e) {
			return "Failed to fetch weather data\nPlease check your city and connection...";
		}
	}
	
	public static void main(String[] args) {
		frame = new JFrame("Weather Forecast App");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 300);
		frame.setLayout(new FlowLayout());
		
		locationField = new JTextField(15);
		fetchButton = new JButton("Show Result");
		weatherDisplay = new JTextArea(10, 30);
		
		frame.add(new JLabel("Enter city name"));
		frame.add(locationField);
		frame.add(fetchButton);
		frame.add(weatherDisplay);
		
		fetchButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String city = locationField.getText();
				String weatherInfo = fecthWeatherData(city);
				weatherDisplay.setText(weatherInfo);
			}
		});
		
		frame.setVisible(true);
	}
}
