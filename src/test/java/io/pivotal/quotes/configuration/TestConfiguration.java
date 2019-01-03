package io.pivotal.quotes.configuration;

import io.pivotal.quotes.domain.CompanyInfo;
import io.pivotal.quotes.domain.IexBatchQuote;
import io.pivotal.quotes.domain.IexQuote;
import io.pivotal.quotes.domain.Quote;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
/**
 * Defaults to use for the tests.
 * 
 * @author David Ferreira Pinto
 *
 */
public class TestConfiguration {
	
	public static final String QUOTE_SYMBOL = "IBM";
	public static final String QUOTE_NAME = "International Business Machines Corporation";
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss zzzXXX yyyy", Locale.ENGLISH);
	public static final String QUOTE_DATE_STRING = "Wed May 6 00:00:00 UTC-04:00 2015";
	public static final BigDecimal QUOTE_LAST_PRICE = new BigDecimal(26.135);
	public static final BigDecimal QUOTE_CHANGE = new BigDecimal(0.00500000000000256d);
	public static final Float QUOTE_CHANGE_PERCENT = 0.0191350937619692f;
	public static final Float QUOTE_MSDATE = 42130f;
	
	public static final String COMPANY_EXCHANGE = "NASDAQ";
	public static final String NULL_QUOTE_SYMBOL = "LALALALA";
	
	public static final String QUOTE_SYMBOLS = "GOOGL,IBM";
	/*
	 * {"Status":"SUCCESS","Name":"EMC Corp","Symbol":"EMC","LastPrice":26.135,
	 * "Change":0.00500000000000256,"ChangePercent":0.0191350937619692,
	 * "Timestamp":"Wed May 6 00:00:00 UTC-04:00 2015","MSDate":42130,
	 * "MarketCap":50755764235,"Volume":15159291,"ChangeYTD":29.74,
	 * "ChangePercentYTD":-12.1217215870881,"High":0,"Low":0,"Open":26.52}
	 */
	public static Quote quote() {
		Quote quote = new Quote();
		quote.setName(QUOTE_NAME);
		quote.setSymbol(QUOTE_SYMBOL);
		quote.setLastPrice(QUOTE_LAST_PRICE);
		quote.setChange(QUOTE_CHANGE);
		quote.setChangePercent(QUOTE_CHANGE_PERCENT);
		try {
			quote.setTimestamp(dateFormat.parse(QUOTE_DATE_STRING));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		quote.setmSDate(QUOTE_MSDATE);
		quote.setMarketCap(50755764235.00f);
		quote.setVolume(15159291);
		quote.setChangeYTD(29.74f);
		quote.setChangePercentYTD(-12.1217215870881f);
		quote.setHigh(new BigDecimal(0.0));
		quote.setLow(new BigDecimal(0.0));
		quote.setOpen(new BigDecimal(26.52));
		return quote;
	}


	public static IexQuote iexQuote() throws Exception{
		IexQuote iexQuote = new IexQuote();
		iexQuote.setCompanyName(QUOTE_NAME);
		iexQuote.setSymbol(QUOTE_SYMBOL);
		iexQuote.setOpen(new BigDecimal(26.52));
		iexQuote.setHigh(new BigDecimal(0.0));
		iexQuote.setLow(new BigDecimal(0.0));
		iexQuote.setChange(new BigDecimal(29.74));
		iexQuote.setChangePercent(new BigDecimal(-12.1217215870881));
		iexQuote.setMarketCap(new BigDecimal(50755764235.00));
		iexQuote.setLatestSource("Previous close");
		iexQuote.setCloseTime(dateFormat.parse(QUOTE_DATE_STRING).getTime());
		iexQuote.setAvgTotalVolume(new BigDecimal(15159291));
		return iexQuote;
	}

	public static Quote quote2() {
		Quote quote = new Quote();
		quote.setName("International Business Machine");
		quote.setSymbol("IBM");
		quote.setLastPrice(QUOTE_LAST_PRICE);
		quote.setChange(QUOTE_CHANGE);
		quote.setChangePercent(QUOTE_CHANGE_PERCENT);
		try {
			quote.setTimestamp(dateFormat.parse(QUOTE_DATE_STRING));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		quote.setmSDate(QUOTE_MSDATE);
		quote.setMarketCap(50755764235.00f);
		quote.setVolume(15159291);
		quote.setChangeYTD(29.74f);
		quote.setChangePercentYTD(-12.1217215870881f);
		quote.setHigh(new BigDecimal(0.0));
		quote.setLow(new BigDecimal(0.0));
		quote.setOpen(new BigDecimal(26.52));
		return quote;
	}

	public static IexQuote iexQuote2() throws Exception{
		IexQuote iexQuote = new IexQuote();
		iexQuote.setCompanyName("International Business Machine");
		iexQuote.setSymbol("IBM");
		iexQuote.setOpen(new BigDecimal(26.52));
		iexQuote.setHigh(new BigDecimal(0.0));
		iexQuote.setLow(new BigDecimal(0.0));
		iexQuote.setChange(new BigDecimal(29.74));
		iexQuote.setChangePercent(new BigDecimal(-12.1217215870881));
		iexQuote.setMarketCap(new BigDecimal(50755764235.00));
		iexQuote.setLatestSource("Previous close");
		iexQuote.setCloseTime(dateFormat.parse(QUOTE_DATE_STRING).getTime());
		iexQuote.setAvgTotalVolume(new BigDecimal(15159291));
		return iexQuote;
	}


	public static IexBatchQuote iexBatchQuoteFor2Symbols() throws Exception {
		IexBatchQuote batchQuote = new IexBatchQuote();
		batchQuote.put(QUOTE_NAME, hashMapFor(QUOTE_NAME, iexQuote()));
		batchQuote.put("IBM", hashMapFor("IBM", iexQuote2()));
		return batchQuote;
	}

	public static IexBatchQuote iexBatchQuoteFor1Symbol() throws Exception {
		IexBatchQuote batchQuote = new IexBatchQuote();
		batchQuote.put(QUOTE_NAME, hashMapFor(QUOTE_NAME, iexQuote()));
		return batchQuote;
	}

	private static HashMap<String, IexQuote> hashMapFor(String key, IexQuote value) {
		HashMap<String, IexQuote> map = new HashMap<>();
		map.put(key, value);
		return map;
	}


	public static List<Quote> quotes() {
		List<Quote> quotes = new ArrayList<>();
		quotes.add(quote());
		quotes.add(quote2());
		return quotes;
	}
	
	public static CompanyInfo company() {
		CompanyInfo comp = new CompanyInfo();
		comp.setExchange(COMPANY_EXCHANGE);
		comp.setName(QUOTE_NAME);
		comp.setSymbol(QUOTE_SYMBOL);
		return comp;
	}
}
