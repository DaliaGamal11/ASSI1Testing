package org.jfree.data.test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jfree.data.time.Quarter;
import org.jfree.data.time.Year;
import org.junit.Test;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.jfree.data.time.*;
public class QuarterClassTest {
    Quarter quarter;


    private void arrange() {
        quarter = new Quarter();
    }
    private void arrange(Integer quart, Integer year) {
        quarter = new Quarter(quart, year);
    }
   
    private void arrange(Integer q, Year year) {
        quarter = new Quarter(q,year);
    }
    private void arrange(Date date) {
        quarter = new Quarter(date);
    }
    private void arrange(Date date, TimeZone zone) {
        quarter = new Quarter(date, zone);
    }
    
    
    @Test
    public void testQuarterDefaultCtor() {
        arrange();
        assertEquals(2023, quarter.getYear().getYear());
        assertEquals(2, quarter.getQuarter());
    }
    
    
    @Test
    public void testQuarterParameterizedCtorOutOfRange()
    {

        arrange(-1, 9999);
        assertEquals(9999, quarter.getYear().getYear());
        assertEquals(-1, quarter.getQuarter());
    }
    
    @Test
    public void testQuarterParameterizedCtor()
    {

        arrange(1, 2022);
        assertEquals(2022, quarter.getYear().getYear());
        assertEquals(1, quarter.getQuarter());
    }

    @Test
    public void testQuartertYearCtor() {
        Year year = new Year(2002);
        arrange(2, year);
        assertEquals(year, quarter.getYear());
        assertEquals(2, quarter.getQuarter());

    }
    
    @Test
    public void testQuarterDateCtor() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = format.parse("2022-01-01");
        arrange(date1);
        assertEquals(2022, quarter.getYear().getYear());
        assertEquals(1, quarter.getQuarter());
    }
    
    @Test
    public void testQuarterDateZoneCtor() {
        Date date1 = new Date(1234567890L); // arbitrary date/time
        TimeZone timeZone1 = TimeZone.getTimeZone("GMT+1"); // arbitrary time zone
        Quarter quarter1 = new Quarter(date1, timeZone1);
        
        
        Date date2 = new Date(1234567890L); // same date/time as above
        TimeZone timeZone2 = TimeZone.getTimeZone("GMT+2"); // different time zone
        Quarter quarter2 = new Quarter(date2, timeZone2);
        
        assertEquals(quarter1, quarter2);
        
        Date date3 = new Date(2345678901L); // different date/time
        TimeZone timeZone3 = TimeZone.getTimeZone("GMT+1"); // same time zone
        Quarter quarter3 = new Quarter(date3, timeZone3);
        
        assertNotEquals(quarter1, quarter3);
    }
    

    @Test
    public void testPrevious() {
        RegularTimePeriod q1 = new Quarter(1, 2023);
        RegularTimePeriod q2 = new Quarter(2, 2023);
        RegularTimePeriod q3 = new Quarter(3, 2023);
        RegularTimePeriod q4 = new Quarter(4, 2023);
        RegularTimePeriod q5= new Quarter(1, 1900);
        
        assertNull(q5.previous());
        
        assertEquals(q1, q2.previous());
        assertEquals(q2, q3.previous());
        assertEquals(q3, q4.previous());
    }
    
    @Test
    public void testNext() {
        RegularTimePeriod q1 = new Quarter(1, 2023);
        RegularTimePeriod q2 = new Quarter(2, 2023);
        RegularTimePeriod q3 = new Quarter(3, 2023);
        RegularTimePeriod q4 = new Quarter(4, 2023);
        RegularTimePeriod q5= new Quarter(4,9999);

        
        assertEquals(q2, q1.next());
        assertEquals(q3, q2.next());
        assertEquals(q4, q3.next());
        assertNull(q5.next());

    }
    
    @Test
    public void testGetSerialIndex() {
        Year year = new Year(2022);
        arrange(1, year);
        assertEquals(2022* 4 + 1, quarter.getSerialIndex());
    }
    
    @Test
    public void testEquals() {
        Quarter quarter1 = new Quarter(1, 2021);
        Quarter quarter2 = new Quarter(1, 2021);
        Quarter quarter3 = new Quarter(2, 2021);
        Quarter quarter4 = new Quarter(1, 2022);
        Year year = new Year(2021);
       
        // Test equality with same quarter and year
        assertTrue(quarter1.equals(quarter2));

        // Test inequality with different quarter and same year
        assertFalse(quarter3.equals(quarter1));

        // Test inequality with same quarter and different year
        assertFalse(quarter1.equals(quarter4));

        // Test inequality with different object type
        assertFalse(quarter1.equals(year));
    }

    

    @Test
    public void testHashCodeFormula() {
        Quarter quarter1 = new Quarter(1, 2021);

        //this formula described in "Effective Java" by Joshua Bloch: 31 * hashcodeOfQuarter + hashcodeOfYear 
        int result=31+1;
        int expectedHashCode1 = ((31* result) + 2021);

        // Test hash code for each quarter
        assertEquals(expectedHashCode1, quarter1.hashCode());

    }
    
    @Test
    public void testHashCodeEquality() {
        Quarter quarter1 = new Quarter(1, 2021);
        Quarter quarter2 = new Quarter(1, 2021);
        Quarter quarter3 = new Quarter(2, 2021);
        Quarter quarter4 = new Quarter(1, 2022);

        // Test that equal quarters have the same hash code
        assertEquals(quarter1.hashCode(), quarter2.hashCode());

        // Test that unequal quarters likely have different hash codes
        assertNotEquals(quarter1.hashCode(), quarter3.hashCode());
        assertNotEquals(quarter1.hashCode(), quarter4.hashCode());
    }
    
    @Test
    public void testCompareTo() {
        Quarter quarter1 = new Quarter(1, 2021);
        Quarter quarter2 = new Quarter(1, 2021);


        // Test comparison with same quarter and year
        assertEquals(0, quarter1.compareTo(quarter2));


        // Test comparison with different object type
        try {
            quarter1.compareTo("not a quarter");
        } catch (ClassCastException e) {
            // Expected exception
        }
    }
    @Test
    public void testCompareTo2() {
        Quarter quarter1 = new Quarter(1, 2021);
        Quarter quarter3 = new Quarter(2, 2021);
        
        // Test comparison with different quarter and same year
        assertTrue(quarter1.compareTo(quarter3) < 0); // neagtive which means Q1 is before Q3
        assertTrue(quarter3.compareTo(quarter1) > 0); // positive which means Q3 is after Q1
    
    }
    
    @Test
    public void testCompareTo3() 
    {
        Quarter quarter1 = new Quarter(1, 2021);
        Quarter quarter4 = new Quarter(1, 2022);
        // Test comparison with same quarter and different year
        assertTrue(quarter1.compareTo(quarter4) < 0);// negative which mean Q1 us before Q4
        assertTrue(quarter4.compareTo(quarter1) > 0); // positive which means Q4 is after Q1
    }
    

    
    @Test
    public void testToString() 
    {
        Quarter quarter1 = new Quarter(1, 2021);
        // Test string representation of quarter
        assertEquals("Q1/2021", quarter1.toString());
    }
    
    @Test
    public void testGetFirstMillisecondExpected() {
    	arrange(1,2023);
    	Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        // Test last millisecond of quarter
        assertEquals(1672531200000L, quarter.getFirstMillisecond(calendar));
    }   


    @Test
    public void testGetLastMillisecondExpected() {
    	arrange(1,2023);
    	Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        // Test last millisecond of quarter
        assertEquals(1680307199999L, quarter.getLastMillisecond(calendar));
    }
    

    @Test
    public void testGetFirstMillisecondDifferent()
    {
    	arrange(1,2021);
	    Calendar utcCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
	    long utcMillis = quarter.getFirstMillisecond(utcCal);
	    Calendar nyCal = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
	    long nyMillis = quarter.getFirstMillisecond(nyCal);
	    assertNotEquals(utcMillis, nyMillis);
    }
    
    @Test
    public void testGetLastMillisecondDifferent()
    {
    	arrange(1,2021);
    	Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    	long lastMillis = quarter.getLastMillisecond(cal);

    	Calendar cal2 = Calendar.getInstance(TimeZone.getTimeZone("America/New_York")); 
    	long lastMillis2 = quarter.getLastMillisecond(cal2);
    	assertNotEquals(lastMillis, lastMillis2) ;  // Should be different due to time zone
    }
    @Test
    public void testGetLastMillisecondForNull() {
    	arrange(1,2023);
    	Calendar calendar = null;
        // Test last millisecond of quarter
        assertEquals(1680307199999L, quarter.getLastMillisecond(calendar));
    }
    @Test
    public void testGetFirstMillisecondForNull() {
    	arrange(1,2023);
    	Calendar calendar = null;
        // Test last millisecond of quarter
        assertEquals(1672531200000L, quarter.getFirstMillisecond(calendar));
    }  
    
    @Test
    public void testParseQuarter() {
        // Test valid formats
        assertEquals(new Quarter(1, 2023), Quarter.parseQuarter("2023-Q1"));
        assertEquals(new Quarter(1, 2023), Quarter.parseQuarter("Q1-2023"));
        assertEquals(new Quarter(2, 2023), Quarter.parseQuarter("2023/Q2"));
        assertEquals(new Quarter(3, 2023), Quarter.parseQuarter("Q3,2023"));
        assertEquals(new Quarter(4, 2023), Quarter.parseQuarter("2023  - Q4"));
    }
    
    @Test
    public void testParseQuarterFOrNull() {
        // Test invalid formats
        assertNull(Quarter.parseQuarter("2023-Q5"));
        assertNull(Quarter.parseQuarter("Q5-2023"));
        assertNull(Quarter.parseQuarter("2023/4"));
        assertNull(Quarter.parseQuarter("Q3 2023"));
        assertNull(Quarter.parseQuarter("2023 Q4"));
        
    }

    
    
}

