package com.arjvik.arjmart.dialogflow;

public final class Contexts {
	
	private static final int   default_pagination_control_lifespan = 3;
	
	public static final String BROWSE_CATALOG_NEXT = "browse-catalog-next";
	public static final int    BROWSE_CATALOG_NEXT_LIFESPAN = default_pagination_control_lifespan;
	
	public static final String BROWSE_CATALOG_PREVIOUS = "browse-catalog-previous";
	public static final int    BROWSE_CATALOG_PREVIOUS_LIFESPAN = default_pagination_control_lifespan;
	
	public static final String SEARCH_CATALOG_NEXT = "search-catalog-next";
	public static final int    SEARCH_CATALOG_NEXT_LIFESPAN = default_pagination_control_lifespan;
	
	public static final String SEARCH_CATALOG_PREVIOUS = "search-catalog-previous";
	public static final int    SEARCH_CATALOG_PREVIOUS_LIFESPAN = default_pagination_control_lifespan;
	
	public static final String ITEM_LIST_INDEXES = "item-list-indexes";
	public static final int    ITEM_LIST_INDEXES_LIFESPAN = 5;
	
	public static final String ITEM_DETAILS_SKU = "item-details-sku";
	public static final int    ITEM_DETAILS_SKU_LIFESPAN = 1;

	public static final String AWAITING_CHECKOUT = "awaiting-checkout";
	public static final int    AWAITING_CHECKOUT_LIFESPAN = 1;
	
	public static final String AWAITING_CHECKOUT_CONFIRMATION = "awaiting-checkout-confirmation";
	public static final int    AWAITING_CHECKOUT_CONFIRMATION_LIFESPAN = 1;
	
	private Contexts() {
		throw new UnsupportedOperationException("This class can not be instantiated!");
	}

}
