package product;
import java.io.Serializable;

public class Product implements Serializable{

	private String name;
	private double price;
	private int code;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		if(name.length()>=20) {
			this.name=name.substring(0, 19);
		}
		else
		{
			this.name=name;
		}
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		if(price>=0)
		{
		this.price = price;
		}
		else 
		{
			System.out.println("Invalid Price");
			getPrice();
		}
	}
	public int getProductCode(){
		return code;
	}
	public void setProductCode(int code){
		this.code=code;
	}
	
}
