package com.cep.streamer.dto;

import java.io.Serializable;
import java.util.Date;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

public class StockStatus implements Serializable {

	private static final long serialVersionUID = 1L;
	@QuerySqlField(index = true)
	private String symbol;
	@QuerySqlField(index = true)
	private Date timestamp;
	@QuerySqlField(index = true)
	private double open;
	@QuerySqlField(index = true)
	private double high;
	@QuerySqlField(index = true)
	private double low;
	@QuerySqlField(index = true)
	private double close;
	@QuerySqlField(index = true)
	private long volume;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public double getOpen() {
		return open;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public double getClose() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public long getVolume() {
		return volume;
	}

	public void setVolume(long volume) {
		this.volume = volume;
	}

	@Override
	public String toString() {
		return "Stock [symbol=" + symbol + ", timestamp=" + timestamp + ", open=" + open + ", high=" + high + ", low="
				+ low + ", close=" + close + ", volume=" + volume + "]";
	}

}
