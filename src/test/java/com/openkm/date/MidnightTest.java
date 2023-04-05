package com.openkm.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class MidnightTest {
	public static void main(String... args) throws ParseException {
		LocalDate today = LocalDate.now();
		LocalDateTime midnight = LocalDateTime.of(today, LocalTime.MIDNIGHT);
		Date date = Date.from(midnight.atZone(ZoneId.systemDefault()).toInstant());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		System.out.println(sdf.format(date));
	}
}
