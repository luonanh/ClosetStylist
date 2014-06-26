package com.example.closetstylist;

import java.io.Serializable;

/*
 * Copyright (C) 2013 Surviving with Android (http://www.survivingwithandroid.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class OpenWeatherMapWeather {

	public OpenWeatherMapLocation location = new OpenWeatherMapLocation();
	public CurrentCondition currentCondition = new CurrentCondition();
	public Temperature temperature = new Temperature();
	public Wind wind = new Wind();
	public Rain rain = new Rain();
	public Snow snow = new Snow()	;
	public Clouds clouds = new Clouds();

	public byte[] iconData;

	public class OpenWeatherMapLocation implements Serializable {

		private float longitude;
		private float latitude;
		private long sunset;
		private long sunrise;
		private String country;
		private String city;

		public float getLongitude() {
			return longitude;
		}
		public void setLongitude(float longitude) {
			this.longitude = longitude;
		}
		public float getLatitude() {
			return latitude;
		}
		public void setLatitude(float latitude) {
			this.latitude = latitude;
		}
		public long getSunset() {
			return sunset;
		}
		public void setSunset(long sunset) {
			this.sunset = sunset;
		}
		public long getSunrise() {
			return sunrise;
		}
		public void setSunrise(long sunrise) {
			this.sunrise = sunrise;
		}
		public String getCountry() {
			return country;
		}
		public void setCountry(String country) {
			this.country = country;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
	}
	
	public  class CurrentCondition {
		private int weatherId;
		private String condition;
		private String descr;
		private String icon;


		private float pressure;
		private float humidity;

		public int getWeatherId() {
			return weatherId;
		}
		public void setWeatherId(int weatherId) {
			this.weatherId = weatherId;
		}
		public String getCondition() {
			return condition;
		}
		public void setCondition(String condition) {
			this.condition = condition;
		}
		public String getDescr() {
			return descr;
		}
		public void setDescr(String descr) {
			this.descr = descr;
		}
		public String getIcon() {
			return icon;
		}
		public void setIcon(String icon) {
			this.icon = icon;
		}
		public float getPressure() {
			return pressure;
		}
		public void setPressure(float pressure) {
			this.pressure = pressure;
		}
		public float getHumidity() {
			return humidity;
		}
		public void setHumidity(float humidity) {
			this.humidity = humidity;
		}
	}

	public  class Temperature {
		private double temp;
		private double minTemp;
		private double maxTemp;

		public double getTemp() {
			return temp;
		}
		public void setTemp(double temp) {
			this.temp = temp;
		}
		public double getMinTemp() {
			return minTemp;
		}
		public void setMinTemp(double minTemp) {
			this.minTemp = minTemp;
		}
		public double getMaxTemp() {
			return maxTemp;
		}
		public void setMaxTemp(double maxTemp) {
			this.maxTemp = maxTemp;
		}
	}

	public  class Wind {
		private float speed;
		private float deg;
		public float getSpeed() {
			return speed;
		}
		public void setSpeed(float speed) {
			this.speed = speed;
		}
		public float getDeg() {
			return deg;
		}
		public void setDeg(float deg) {
			this.deg = deg;
		}
	}

	public  class Rain {
		private String time = null;
		private float ammount = 0;
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public float getAmmount() {
			return ammount;
		}
		public void setAmmount(float ammount) {
			this.ammount = ammount;
		}
	}

	public  class Snow {
		private String time;
		private float ammount;

		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public float getAmmount() {
			return ammount;
		}
		public void setAmmount(float ammount) {
			this.ammount = ammount;
		}
	}

	public  class Clouds {
		private int perc;

		public int getPerc() {
			return perc;
		}

		public void setPerc(int perc) {
			this.perc = perc;
		}
	}
}