package com.olaaref.mintshop.common;

public class Constants {
	  public static final String GCP_Base_URI;
	  
	  static {
		  String GCP = "https://storage.googleapis.com";
		  String bucketName = "mintshop-file";
		  GCP_Base_URI = GCP + "/" + bucketName;
	  }
}