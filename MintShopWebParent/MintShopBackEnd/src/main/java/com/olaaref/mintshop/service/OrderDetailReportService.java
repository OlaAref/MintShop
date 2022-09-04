package com.olaaref.mintshop.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.olaaref.mintshop.common.entity.order.OrderDetail;
import com.olaaref.mintshop.dao.OrderDetailRepository;
import com.olaaref.mintshop.report.ReportItem;
import com.olaaref.mintshop.report.ReportType;

@Service
public class OrderDetailReportService extends AbstractReportService{

	@Autowired
	private OrderDetailRepository detailRepository;
	
	@Override
	protected List<ReportItem> getReportDataByDateRangeInternal(LocalDateTime start, LocalDateTime end, ReportType reportType) {
		List<OrderDetail> orders = null;
		
		if(reportType.equals(ReportType.CATEGORY)) {
			orders = detailRepository.findWithCategoryAndTimeBetween(start, end);	
		}
		else if(reportType.equals(ReportType.PRODUCT)) {
			orders = detailRepository.findWithProductAndTimeBetween(start, end);
		}
		
		//printRawData(orders);
		
		List<ReportItem> report = createReportData(reportType, orders);
		
		//printReportData(report);
		
		return report;
	}
	
	private List<ReportItem> createReportData(ReportType reportType, List<OrderDetail> orders) {
		List<ReportItem> reportItems = new ArrayList<>();
		
		for (OrderDetail detail : orders) {
			String identifier = "";
			if(reportType.equals(ReportType.CATEGORY)) {
				identifier = detail.getProduct().getCategory().getName();
			}
			else if(reportType.equals(ReportType.PRODUCT)) {
				identifier = detail.getProduct().getShortName();
			}
			
			ReportItem reportItem = new ReportItem(identifier);
			int index = reportItems.indexOf(reportItem);
			
			//setup report item components
			float grossSales = detail.getSubTotal() + detail.getShippingCost();
			float netSales = detail.getSubTotal() - detail.getProductCost();
			int productCounts = detail.getQuantity();
			
			//if this item exist before in the list, increase mounts
			if(index >= 0) {
				reportItem = reportItems.get(index);
				reportItem.addGrossSales(grossSales);
				reportItem.addNetSales(netSales);
				reportItem.increaseProductCounts(productCounts);
				
			}
			//the item is not exist in the list (new item)
			else {
				reportItems.add(new ReportItem(identifier, grossSales, netSales, productCounts));
			}
		}
		
		return reportItems;
	}

	private void printRawData(List<OrderDetail> orders) {
		orders.forEach(order -> {
			System.out.printf("%d | %-20s | %10.2f | %10.2f | %10.2f \n", 
						order.getQuantity(), 
						order.getProduct().getCategory().getName(), 
						order.getSubTotal(), 
						order.getProductCost(),
						order.getShippingCost());
		});
		
	}
	
	private void printReportData(List<ReportItem> items) {
		items.forEach(item -> {
			System.out.printf("%s, %10.2f, %10.2f, %d \n", 
					item.getIdentifier(),
					item.getGrossSales(),
					item.getNetSales(),
					item.getProductCounts());
		});
		
	}
	
}
