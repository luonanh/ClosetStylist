package com.example.closetstylist;

import java.io.InputStream;

public interface WeatherProviderInterface {

	public String getWeatherDataFromLatLong(PlaceRecord place);
	public WeatherInfo getWeatherInfoFromWeatherData(String data, PlaceRecord place);
}
