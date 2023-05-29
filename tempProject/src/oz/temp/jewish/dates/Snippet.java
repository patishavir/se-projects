package oz.temp.jewish.dates;

import java.util.Vector;

import sun.util.calendar.CalendarDate;

public class Snippet {
	/*
	Calculating Jewish holidays
	A list of the holidays with their dates and remarks for calculation is available here.
	The following function getHolidayForDate calculates the holidays for a gregorian date gdate. The holidays are returned in a Vector because Shabbat Hagadol and Erev Pesach can fall together.
	*/
	
	  private static int getWeekdayOfHebrewDate(int hebDay, int hebMonth, int hebYear, CalendarImpl i) {
	    int absDate = i.absoluteFromJewishDate(new CalendarDate(hebDay, hebMonth, hebYear));
	    return absDate % 7;
	  }
	  public static Vector getHolidayForDate(CalendarDate gdate, CalendarImpl i,
	                                  boolean diaspora) {
	    int absDate = i.absoluteFromGregorianDate(gdate);
	    CalendarDate jewishDate = i.jewishDateFromAbsolute(absDate);
	    int hebDay = jewishDate.getDay();
	    int hebMonth = jewishDate.getMonth();
	    int hebYear = jewishDate.getYear();
	    Vector listHolidays = new Vector();
	    // Holidays in Nisan
	    int hagadolDay = 14;
	    while (getWeekdayOfHebrewDate(hagadolDay, 1, hebYear, i) != 6)
	      hagadolDay -= 1;
	    if (hebDay == hagadolDay && hebMonth == 1)
	      listHolidays.addElement("Shabat Hagadol");
	    if (hebDay == 14 && hebMonth == 1)
	      listHolidays.addElement("Erev Pesach");
	    if (hebDay == 15 && hebMonth == 1)
	      listHolidays.addElement("Pesach I");
	    if (hebDay == 16 && hebMonth == 1) {
	      if (diaspora) {
	        listHolidays.addElement("Pesach II");
	      } else {
	        listHolidays.addElement("Chol Hamoed");
	      }
	    }
	    if (hebDay == 17 && hebMonth == 1)
	      listHolidays.addElement("Chol Hamoed");
	    if (hebDay == 18 && hebMonth == 1)
	      listHolidays.addElement("Chol Hamoed");
	    if (hebDay == 19 && hebMonth == 1)
	      listHolidays.addElement("Chol Hamoed");
	    if (hebDay == 20 && hebMonth == 1)
	      listHolidays.addElement("Chol Hamoed");
	    if (hebDay == 21 && hebMonth == 1) {
	      if (!diaspora)
	        listHolidays.addElement("Pesach VII (Yizkor)");
	      else
	        listHolidays.addElement("Pesach VII");
	    }
	    if (hebDay == 22 && hebMonth == 1) {
	      if (diaspora)
	        listHolidays.addElement("Pesach VIII (Yizkor)");
	    }
	    // Yom Hashoah
	    if (getWeekdayOfHebrewDate(27, 1, hebYear, i) == 5) {
	      if (hebDay == 26 && hebMonth == 1)
	        listHolidays.addElement("Yom Hashoah");
	    } else if (hebYear >= 5757 && getWeekdayOfHebrewDate(27, 1, hebYear, i) == 0) {
	      if (hebDay == 28 && hebMonth == 1)
	        listHolidays.addElement("Yom Hashoah");
	    } else {
	      if (hebDay == 27 && hebMonth == 1)
	        listHolidays.addElement("Yom Hashoah");
	    }
	    // Holidays in Iyar
	    // Yom Hazikaron
	    if (getWeekdayOfHebrewDate(4, 2, hebYear, i) == 5) { // If 4th of Iyar is a Thursday ...
	      if (hebDay == 2 && hebMonth == 2) // ... then Yom Hazicaron is on 2th of Iyar
	        listHolidays.addElement("Yom Hazikaron");
	    } else if (getWeekdayOfHebrewDate(4, 2, hebYear, i) == 4) {
	      if (hebDay == 3 && hebMonth == 2)
	          listHolidays.addElement("Yom Hazikaron");
	    } else if (hebYear >= 5764 && getWeekdayOfHebrewDate(4, 2, hebYear, i) == 0) {
	      if (hebDay == 5 && hebMonth == 2)
	        listHolidays.addElement("Yom Hazikaron");
	    } else {
	      if (hebDay == 4 && hebMonth == 2)
	        listHolidays.addElement("Yom Hazikaron");
	    }
	    // Yom Ha'Azmaut
	    if (getWeekdayOfHebrewDate(5, 2, hebYear, i) == 6) {
	      if (hebDay == 3 && hebMonth == 2)
	        listHolidays.addElement("Yom Ha'Atzmaut");
	    } else if (getWeekdayOfHebrewDate(5, 2, hebYear, i) == 5) {
	      if (hebDay == 4 && hebMonth == 2)
	        listHolidays.addElement("Yom Ha'Atzmaut");
	    } else if (hebYear >= 5764 && getWeekdayOfHebrewDate(4, 2, hebYear, i) == 0) {
	      if (hebDay == 6 && hebMonth == 2)
	        listHolidays.addElement("Yom Ha'Atzmaut");
	    } else {
	      if (hebDay == 5 && hebMonth == 2)
	        listHolidays.addElement("Yom Ha'Atzmaut");
	    }
	    if (hebDay == 14 && hebMonth == 2)
	      listHolidays.addElement("Pesach Sheni");
	    if(hebDay == 18 && hebMonth == 2)
	      listHolidays.addElement("Lag BaOmer");
	    if(hebDay == 28 && hebMonth == 2)
	      listHolidays.addElement("Yom Yerushalayim");
	    // Holidays in Sivan
	    if (hebDay == 5 && hebMonth == 3)
	      listHolidays.addElement("Erev Shavuot");
	    if (hebDay == 6 && hebMonth == 3) {
	      if (diaspora)
	        listHolidays.addElement("Shavuot I");
	      else
	        listHolidays.addElement("Shavuot (Yizkor)");
	    }
	    if (hebDay == 7 && hebMonth == 3) {
	      if (diaspora)
	        listHolidays.addElement("Shavuot II (Yizkor)");
	    }
	    // Holidays in Tammuz
	    if (getWeekdayOfHebrewDate(17, 4, hebYear, i) == 6) {
	      if (hebDay == 18 && hebMonth == 4)
	        listHolidays.addElement("Fast of Tammuz");
	    } else {
	      if (hebDay == 17 && hebMonth == 4)
	        listHolidays.addElement("Fast of Tammuz");
	    }
	    // Holidays in Av
	    if (getWeekdayOfHebrewDate(9, 5, hebYear, i) == 6) {
	      if (hebDay == 10 && hebMonth == 5)
	        listHolidays.addElement("Fast of Av");
	    } else {
	      if (hebDay == 9 && hebMonth == 5)
	        listHolidays.addElement("Fast of Av");
	    }
	    if (hebDay == 15 && hebMonth == 5)
	      listHolidays.addElement("Tu B'Av");
	    // Holidays in Elul
	    if (hebDay == 29 && hebMonth == 6)
	      listHolidays.addElement("Erev Rosh Hashana");
	    // Holidays in Tishri
	    if (hebDay == 1 && hebMonth == 7)
	      listHolidays.addElement("Rosh Hashana I");
	    if (hebDay == 2 && hebMonth == 7)
	      listHolidays.addElement("Rosh Hashana II");
	    if (getWeekdayOfHebrewDate(3, 7, hebYear, i) == 6) {
	      if (hebDay == 4 && hebMonth == 7)
	        listHolidays.addElement("Tzom Gedaliah");
	    } else {
	      if (hebDay == 3 && hebMonth == 7)
	        listHolidays.addElement("Tzom Gedaliah");
	    }
	    if (hebDay == 9 && hebMonth == 7)
	      listHolidays.addElement("Erev Yom Kippur");
	    if (hebDay == 10 && hebMonth == 7)
	      listHolidays.addElement("Yom Kippur (Yizkor)");
	    if (hebDay == 14 && hebMonth == 7)
	      listHolidays.addElement("Erev Sukkot");
	    if (hebDay == 15 && hebMonth == 7) {
	      if (diaspora)
	        listHolidays.addElement("Sukkot I");
	      else
	        listHolidays.addElement("Sukkot");
	    }
	    if (hebDay == 16 && hebMonth == 7) {
	      if (diaspora)
	        listHolidays.addElement("Sukkot II");
	      else
	        listHolidays.addElement("Chol Hamoed");
	    }
	    if (hebDay == 17 && hebMonth == 7)
	      listHolidays.addElement("Chol Hamoed");
	    if (hebDay == 18 && hebMonth == 7)
	      listHolidays.addElement("Chol Hamoed");
	    if (hebDay == 19 && hebMonth == 7)
	      listHolidays.addElement("Chol Hamoed");
	    if (hebDay == 20 && hebMonth == 7)
	      listHolidays.addElement("Chol Hamoed");
	    if (hebDay == 21 && hebMonth == 7)
	      listHolidays.addElement("Hoshana Raba");
	    if (hebDay == 22 && hebMonth == 7) {
	      if (!diaspora) {
	        listHolidays.addElement("Shemini Atzereth (Yizkor)");
	        listHolidays.addElement("Simchat Torah");
	      } else {
	        listHolidays.addElement("Shemini Atzereth (Yizkor)");
	      }
	    }
	    if (hebDay == 23 && hebMonth == 7) {
	      if (diaspora)
	        listHolidays.addElement("Simchat Torah");
	    }
	    // Holidays in Kislev
	    if (hebDay == 25 && hebMonth == 9)
	      listHolidays.addElement("Chanukka I");
	    if (hebDay == 26 && hebMonth == 9)
	      listHolidays.addElement("Chanukka II");
	    if (hebDay == 27 && hebMonth == 9)
	      listHolidays.addElement("Chanukka III");
	    if (hebDay == 28 && hebMonth == 9)
	      listHolidays.addElement("Chanukka IV");
	    if (hebDay == 29 && hebMonth == 9)
	      listHolidays.addElement("Chanukka V");
	    // Holidays in Tevet
	    if (hebDay == 10 && hebMonth == 10)
	      listHolidays.addElement("Fast of Tevet");
	    if (i.getLastDayOfJewishMonth(9, hebYear) == 30) {
	      if (hebDay == 30 && hebMonth == 9)
	        listHolidays.addElement("Chanukka VI");
	      if (hebDay == 1 && hebMonth == 10)
	        listHolidays.addElement("Chanukka VII");
	      if (hebDay == 2 && hebMonth == 10)
	        listHolidays.addElement("Chanukka VIII");
	    }
	    if (i.getLastDayOfJewishMonth(9, hebYear) == 29) {
	      if (hebDay == 1 && hebMonth == 10)
	        listHolidays.addElement("Chanukka VI");
	      if (hebDay == 2 && hebMonth == 10)
	        listHolidays.addElement("Chanukka VII");
	      if (hebDay == 3 && hebMonth == 10)
	        listHolidays.addElement("Chanukka VIII");
	    }
	    // Holidays in Shevat
	    if (hebDay == 15 && hebMonth == 11)
	      listHolidays.addElement("Tu B'Shevat");
	    // Holidays in Adar (I)/Adar II
	    int monthEsther;
	    if (i.hebrewLeapYear(hebYear))
	      monthEsther = 13;
	    else
	      monthEsther = 12;
	    
	    if (getWeekdayOfHebrewDate(13, monthEsther, hebYear, i) == 6) {
	      if (hebDay == 11 && hebMonth == monthEsther)
	        listHolidays.addElement("Fast of Esther");
	    } else {
	      if (hebDay == 13 && hebMonth == monthEsther)
	        listHolidays.addElement("Fast of Esther");
	    }
	    if (hebDay == 14 && hebMonth == monthEsther)
	      listHolidays.addElement("Purim");
	    if (hebDay == 15 && hebMonth == monthEsther)
	      listHolidays.addElement("Shushan Purim");
	    if (i.hebrewLeapYear(hebYear)) {
	      if (hebDay == 14 && hebMonth == 12)
	        listHolidays.addElement("Purim Katan");
	      if (hebDay == 15 && hebMonth == 12)
	        listHolidays.addElement("Shushan Purim Katan");
	    }
	    return listHolidays;
	  }
}

