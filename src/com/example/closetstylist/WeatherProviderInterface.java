package com.example.closetstylist;

import java.io.InputStream;

public interface WeatherProviderInterface {

	public String getWeatherDataFromLatLong(double lat, double lon);
	public WeatherInfo getWeatherInfoFromWeatherData(String data);
}
