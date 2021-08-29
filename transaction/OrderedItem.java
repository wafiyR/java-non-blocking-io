package transaction;
import product.Product;
import java.io.Serializable;

public class OrderedItem implements Serializable{

	private Product product;
	private int quantity;
	private double subTotal;
	private int productCode;
	
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getSubTotal() {
		subTotal=quantity*product.getPrice();
		return subTotal;
	}
	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}
	public int getProductCode(){
		return productCode;
	}
	public void setProductCode(int productCode){
		this.productCode=productCode;
	}
}
