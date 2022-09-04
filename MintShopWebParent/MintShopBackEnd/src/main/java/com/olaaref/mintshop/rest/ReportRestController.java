package com.olaaref.mintshop.rest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.olaaref.mintshop.report.ReportItem;
import com.olaaref.mintshop.report.ReportType;
import com.olaaref.mintshop.service.MasterOrderReportService;
import com.olaaref.mintshop.service.OrderDetailReportService;

@RestController
@RequestMapping("/reports")
public class ReportRestController {
	
	@Autowired
	private MasterOrderReportService reportService;
	
	@Autowired
	private OrderDetailReportService detailReportService;
	
	@GetMapping("/sales_by_date/{period}")
	public List<ReportItem> getReportDataByDatePeriod(@PathVariable("period") String period){
		
		switch (period) {
			case "last_7_days":
				return reportService.getReportDataLast7Days(ReportType.DAY);
				
			case "last_28_days":
				return reportService.getReportDataLast28Days(ReportType.DAY);
				
			case "last_6_months":
				return reportService.getReportDataLast6Months(ReportType.MONTH);
				
			case "last_year":
				return reportService.getReportDataLastYear(ReportType.MONTH);
				
			default:
				return reportService.getReportDataLast7Days(ReportType.DAY);
		}
		
	}
	
	@GetMapping("/sales_by_date/{startDate}/{endDate}")
	public List<ReportItem> getReportDataByDatePeriod(@PathVariable("startDate") String startDate,
													  @PathVariable("endDate") String endDate){
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime start = LocalDate.parse(startDate, formatter).atStartOfDay();
		LocalDateTime end = LocalDate.parse(endDate, formatter).atStartOfDay();
		
		
		return reportService.getReportDataByDateRange(start, end, ReportType.DAY);
	}
	
	@GetMapping("/{groupBy}/{period}")
	public List<ReportItem> getReportDataByCategoryOrProduct(@PathVariable("groupBy") String groupBy,
															@PathVariable("period") String period){
		
		ReportType reportType = ReportType.valueOf(groupBy.toUpperCase());
				
		switch (period) {
			case "last_7_days":
				return detailReportService.getReportDataLast7Days(reportType);
				
			case "last_28_days":
				return detailReportService.getReportDataLast28Days(reportType);
				
			case "last_6_months":
				return detailReportService.getReportDataLast6Months(reportType);
				
			case "last_year":
				return detailReportService.getReportDataLastYear(reportType);
				
			default:
				return detailReportService.getReportDataLast7Days(reportType);
		}
		
	}
	
	@GetMapping("/{groupBy}/{startDate}/{endDate}")
	public List<ReportItem> getReportDataByCategoryOrProductByDatePeriod(@PathVariable("groupBy") String groupBy,
													  @PathVariable("startDate") String startDate,
													  @PathVariable("endDate") String endDate){
		ReportType reportType = ReportType.valueOf(groupBy.toUpperCase());
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime start = LocalDate.parse(startDate, formatter).atStartOfDay();
		LocalDateTime end = LocalDate.parse(endDate, formatter).atStartOfDay();
		
		
		return detailReportService.getReportDataByDateRange(start, end, reportType);
	}
}
