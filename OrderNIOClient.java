import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import transaction.OrderedItem;

public class OrderNIOClient{
	
	public void startClient() throws IOException,InterruptedException{
		
		InetSocketAddress hostAddress = new InetSocketAddress("localhost",9093);
		SocketChannel client = SocketChannel.open(hostAddress);
		
		OrderedItem order1 = new OrderedItem();
		order1.setProductCode(101);
		order1.setQuantity(3);
		
		System.out.println("Client...started");
		
		String threadName = Thread.currentThread().getName();
		
		//send messages to server
		String[] messages = new String[] { threadName + "Product code : "+ order1.getProductCode(), threadName + "Quantity : "+order1.getQuantity()};

		for (int i = 0; i < messages.length; i++) {
			ByteBuffer buffer = ByteBuffer.allocate(73);
			buffer.put(messages[i].getBytes());
			buffer.flip();
			client.write(buffer);
			System.out.println(messages[i]);
			buffer.clear();
			Thread.sleep(5000);
		}
		
		
		client.close();
	}
	
	public static void main(String[] args){
		Runnable client = new Runnable(){
			@Override
			public void run(){
				try{
					new OrderNIOClient().startClient();
				}catch(IOException e){
					e.printStackTrace();
				}catch(InterruptedException e){
					e.printStackTrace();
				}
				
			}
		};
		new Thread(client,"client 1 = ").start();
	}
}
	