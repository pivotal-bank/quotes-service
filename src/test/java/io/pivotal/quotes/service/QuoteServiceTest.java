package io.pivotal.quotes.service;

import io.pivotal.quotes.configuration.TestConfiguration;
import io.pivotal.quotes.domain.CompanyInfo;
import io.pivotal.quotes.domain.IexBatchQuote;
import io.pivotal.quotes.domain.IexQuote;
import io.pivotal.quotes.domain.Quote;
import io.pivotal.quotes.exception.SymbolNotFoundException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.mockito.BDDMockito.*;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Tests the QuoteService.
 *
 * @author David Ferreira Pinto
 */
@RunWith(MockitoJUnitRunner.class)
public class QuoteServiceTest {

    private static final String QUOTES_URL = "quotesUrl";
    private static final String QUOTE_URL = "quoteUrl";
    private static final String COMPANY_URL = "companyUrl";

    /**
     * @Value("${pivotal.quotes.quote_url}") protected String quote_url;
     * @Value("${pivotal.quotes.quotes_url}") protected String quotes_url;
     * @Value("${pivotal.quotes.companies_url}") protected String company_url;
     */


    @InjectMocks
    private QuoteService service;

    @Mock
    private RestTemplate restTemplate;


    @Before
    public void setup() {
        ReflectionTestUtils.setField(service, "quote_url", QUOTE_URL);
        ReflectionTestUtils.setField(service, "quotes_url", QUOTES_URL);
        ReflectionTestUtils.setField(service, "company_url", COMPANY_URL);
    }

    /**
     * Tests retrieving a quote from the external service.
     *
     * @throws Exception
     */
    @Test
    public void getQuote() throws Exception {
        ArgumentCaptor<Map<String,String>> argumentCaptor = ArgumentCaptor.forClass(Map.class);
        given(restTemplate.getForObject(eq(QUOTE_URL), eq(IexQuote.class), argumentCaptor.capture())).willReturn(TestConfiguration.iexQuote());
        Quote quote = service.getQuote(TestConfiguration.QUOTE_SYMBOL);
        assertEquals(TestConfiguration.QUOTE_SYMBOL, quote.getSymbol());
        assertEquals(TestConfiguration.QUOTE_NAME, quote.getName());
        assertEquals(TestConfiguration.QUOTE_SYMBOL, argumentCaptor.getValue().get("symbol"));
    }



    @Test(expected = SymbolNotFoundException.class)
    public void getNullQuote() throws Exception {
        ArgumentCaptor<Map<String,String>> argumentCaptor = ArgumentCaptor.forClass(Map.class);
        given(restTemplate.getForObject(eq(QUOTE_URL), eq(IexQuote.class), argumentCaptor.capture())).willReturn(null);
        Quote quote = service.getQuote(TestConfiguration.NULL_QUOTE_SYMBOL);
    }

    /**
     * tests retrieving company information from external service.
     *
     * @throws Exception
     */
    @Test
    public void getCompanyInfo() throws Exception {
        ArgumentCaptor<Map<String,String>> argumentCaptor = ArgumentCaptor.forClass(Map.class);
        given(restTemplate.getForObject(eq(COMPANY_URL),
                eq(CompanyInfo[].class), argumentCaptor.capture())).willReturn(new CompanyInfo[] {TestConfiguration.company()});
        List<CompanyInfo> comps = service.getCompanyInfo(TestConfiguration.QUOTE_SYMBOL);
        assertFalse(comps.isEmpty());
        boolean pass = false;
        for (CompanyInfo info : comps) {
            if (info.getSymbol().equals(TestConfiguration.QUOTE_SYMBOL)) {
                pass = true;
            }
        }
        assertEquals(TestConfiguration.QUOTE_SYMBOL, argumentCaptor.getValue().get("name"));
        assertTrue(pass);
    }

    /**
     * tests retrieving company information from external service with unkown company.
     *
     * @throws Exception
     */
    @Test
    public void getNullCompanyInfo() throws Exception {
        ArgumentCaptor<Map<String,String>> argumentCaptor = ArgumentCaptor.forClass(Map.class);
        given(restTemplate.getForObject(eq(COMPANY_URL),
                eq(CompanyInfo[].class), argumentCaptor.capture())).willReturn(new CompanyInfo[] {});
        List<CompanyInfo> comps = service.getCompanyInfo(TestConfiguration.NULL_QUOTE_SYMBOL);
        assertEquals(TestConfiguration.NULL_QUOTE_SYMBOL, argumentCaptor.getValue().get("name"));
        assertTrue(comps.isEmpty());
    }

    /**
     * test yahoo service with multiple quotes
     *
     * @throws Exception
     */
    @Test
    public void getQuotes() throws Exception {
        given(restTemplate.getForObject(QUOTES_URL, IexBatchQuote.class, TestConfiguration.QUOTE_SYMBOLS)).willReturn(TestConfiguration.iexBatchQuoteFor2Symbols());
        List<Quote> quotes = service.getQuotes(TestConfiguration.QUOTE_SYMBOLS);
        assertNotNull("should have 2 quotes", quotes);
        assertEquals("should have 2 quotes", quotes.size(), 2);
    }

    @Test
    public void getQuotesOneQuote() throws Exception {
        given(restTemplate.getForObject(QUOTES_URL, IexBatchQuote.class, TestConfiguration.QUOTE_SYMBOL)).willReturn(TestConfiguration.iexBatchQuoteFor1Symbol());
        List<Quote> quotes = service.getQuotes(TestConfiguration.QUOTE_SYMBOL);
        assertNotNull("should have 1 quotes", quotes);
        assertEquals("should have 1 quotes", quotes.size(), 1);
    }
}

