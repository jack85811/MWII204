package application;
	
public class Product {
	private String productId;
	private String productName;
	private String productCode;
	private double price;
	private String description;
	private double starRating;
	
	public Product() {
		
	}
	
	public Product( String productId, String productName, String productCode,
			 String description, double starRating, double price) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.productCode = productCode;
		this.description = description;
		this.starRating = starRating;
		this.price = price;
	}
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public double getStarRating() {
		return starRating;
	}

	public void setStarRating(double starRating) {
		this.starRating = starRating;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
	

	
	
	
}
