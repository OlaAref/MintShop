package com.olaaref.mintshop.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.olaaref.mintshop.common.entity.order.Order;
import com.olaaref.mintshop.dao.OrderRepository;
import com.olaaref.mintshop.report.ReportItem;
import com.olaaref.mintshop.report.ReportType;

@Service
public class MasterOrderReportService extends AbstractReportService{
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Override
	protected List<ReportItem> getReportDataByDateRangeInternal(LocalDateTime start, LocalDateTime end, ReportType reportType) {
		
		List<Order> orders = orderRepository.findByOrderTimeBetween(start, end);
		//printRawData(orders);
		
		List<ReportItem> reportData = createReportData(start, end, reportType);
		calculateSalesForReportData(orders, reportData);
		//printReportData(reportData);
		
		return reportData;
	}

	private List<ReportItem> createReportData(LocalDateTime start, LocalDateTime end, ReportType reportType) {
		List<ReportItem> reportItems = new ArrayList<>();
		
		LocalDateTime currentDate = start;
		String stringDate = currentDate.format(formatter);
		reportItems.add(new ReportItem(stringDate));

		do {
			if(reportType.equals(ReportType.DAY)) {
				//add one day
				currentDate = currentDate.plusDays(1);
			}
			else if(reportType.equals(ReportType.MONTH)) {
				//add one month
				currentDate = currentDate.plusMonths(1);
			}
			stringDate = currentDate.format(formatter);
			reportItems.add(new ReportItem(stringDate));

		}
		while(currentDate.isBefore(end));
		
		return reportItems;
	}
	
	private void calculateSalesForReportData(List<Order> orders, List<ReportItem> reportItems) {
		for(Order order : orders) {
			String identifier = order.getOrderTime().format(formatter);
			ReportItem item = new ReportItem(identifier);
			
			int itemIndex = reportItems.indexOf(item);
			if(itemIndex >= 0) {
				item = reportItems.get(itemIndex);
				
				item.addGrossSales(order.getTotal());
				item.addNetSales(order.getSubTotal() - order.getProductCost());
				item.increaseOrdersCount();
			}
			
		}
		
	}

	private void printRawData(List<Order> orders) {
		orders.forEach(order -> {
			System.out.printf("%-3d | %s | %10.2f | %10.2f \n", 
					order.getId(), order.getOrderTime(), order.getTotal(), order.getProductCost());
		});
		
	}
	
	private void printReportData(List<ReportItem> items) {
		items.forEach(item -> {
			System.out.printf("%s, %10.2f, %10.2f, %d \n", 
					item.getIdentifier(),
					item.getGrossSales(),
					item.getNetSales(),
					item.getOrderCounts());
		});
		
	}

	

	
	
	
}
