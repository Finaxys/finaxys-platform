package com.finaxys.finaxysplatform.marketdatafetcher.jobmanager;

public class JobHelper {

	public static Long getSecondOfMinute(Long x) {

		return (x % 3600) % 60;

	}

	public static Long getMinuteOfHour(Long x) {

		return (x % 3600) / 60;

	}

	public static Long getHours(Long x) {

		return x / 3600;

	}
}
