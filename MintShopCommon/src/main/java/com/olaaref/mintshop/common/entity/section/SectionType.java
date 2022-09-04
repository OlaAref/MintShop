package com.olaaref.mintshop.common.entity.section;

public enum SectionType {
	TEXT{
		@Override
		public String toString() {
			return "Text";
		}
	}, 
	PRODUCT{
		@Override
		public String toString() {
			return "Product";
		}
	}, 
	CATEGORY{
		@Override
		public String toString() {
			return "Category";
		}
	}, 
	ALL_CATEGORIES{
		@Override
		public String toString() {
			return "All_Categories";
		}
	}, 
	ARTICLE{
		@Override
		public String toString() {
			return "Article";
		}
	}, 
	BRAND{
		@Override
		public String toString() {
			return "Brand";
		}
	}
}
