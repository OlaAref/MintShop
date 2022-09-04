package com.olaaref.mintshop.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.olaaref.mintshop.report.ReportItem;
import com.olaaref.mintshop.report.ReportType;

public abstract class AbstractReportService {
	protected DateTimeFormatter formatter;
	
	public List<ReportItem> getReportDataLast7Days(ReportType reportType){
		return getReportDataLastXDays(7, reportType);
	}
	
	public List<ReportItem> getReportDataLast28Days(ReportType reportType) {
		return getReportDataLastXDays(28, reportType);
	}
	
	public List<ReportItem> getReportDataLast6Months(ReportType reportType) {
		return getReportDataLastXMonths(6, reportType);
	}
	
	public List<ReportItem> getReportDataLastYear(ReportType reportType) {
		return getReportDataLastXMonths(12, reportType);
	}
	
	public List<ReportItem> getReportDataByDateRange(LocalDateTime start, LocalDateTime end, ReportType reportType) {
		formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return getReportDataByDateRangeInternal(start, end, reportType);
	}
	
	protected List<ReportItem> getReportDataLastXDays(int days, ReportType reportType) {
		LocalDateTime end = LocalDateTime.now();
		LocalDateTime start = end.minusDays(days);
		
		formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		return getReportDataByDateRangeInternal(start, end, reportType);
	}
	
	protected List<ReportItem> getReportDataLastXMonths(int months, ReportType reportType) {
		LocalDateTime end = LocalDateTime.now();
		LocalDateTime start = end.minusMonths(months);
		
		formatter = DateTimeFormatter.ofPattern("yyyy-MM");
		
		return getReportDataByDateRangeInternal(start, end, reportType);
	}
	
	protected abstract List<ReportItem> getReportDataByDateRangeInternal(LocalDateTime start, LocalDateTime end, ReportType reportType);

}
