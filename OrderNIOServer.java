import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import product.Product;
import java.util.StringTokenizer;  

public class OrderNIOServer {
	
	private Selector selector;
	
	private InetSocketAddress listenAddress;
	private final static int PORT = 9093;
	
	public static void main(String []args) throws Exception{
		try{
			new OrderNIOServer("localhost",9093).startServer();
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public OrderNIOServer(String address, int port) throws IOException{
		listenAddress = new InetSocketAddress(address, PORT);
	}
	
	// Start server
	private void startServer() throws IOException{
		this.selector = Selector.open();
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		serverChannel.configureBlocking(false);
		
		//bind server socket channel to port
		serverChannel.socket().bind(listenAddress);
		serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);
		
		System.out.println("Server started on port >> "+PORT);
		
		while(true){
			//wait for events until channels are ready
			int readyCount = selector.select();
			if(readyCount == 0){
				continue;
			}
			
			//process selected keys
			Set<SelectionKey> readyKeys = selector.selectedKeys();
			Iterator iterator = readyKeys.iterator();
			while(iterator.hasNext()){
				SelectionKey key = (SelectionKey) iterator.next();
				
				//remove key so don't process it twice
				iterator.remove();
				
				if(!key.isValid()){
					continue;
				}
				
				//Accept client connections, read from client or write data to client depend on the key
				if(key.isAcceptable()){
					this.accept(key);
				}else if(key.isReadable()){
					this.read(key);
				}else if(key.isWritable()){
					
				}
			}
		}
	}
	
	//accept client connections
	private void accept(SelectionKey key) throws IOException{
		ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
		SocketChannel channel = serverChannel.accept();
		channel.configureBlocking(false);
		Socket socket = channel.socket();
		SocketAddress remoteAddr = socket.getRemoteSocketAddress();
		System.out.println("Connected to" + remoteAddr);
		
		//Register channel
		channel.register(this.selector, SelectionKey.OP_READ);
	}
	
	//read from socket channel
	private void read(SelectionKey key) throws IOException{
		SocketChannel channel = (SocketChannel) key.channel();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		int numRead = -1;
		numRead = channel.read(buffer);
		
		/*PrintWriter output = new PrintWriter(socket.getOutputStream(),true);
		output.println("Code \t Name \t Price \t Quantity \t Subtotal");
		for(Product p:products){
			if(p.getProductCode()==orderedItem.getProductCode()){
				totalPrice+=p.getPrice()*orderedItem.getQuantity();
				output.print(p.getProductCode()+"\t"+p.getName()+"\t"+p.getPrice()+"\t "+orderedItem.getQuantity()+"\t\t");
			}

		}*/
		
		if(numRead == -1){
			Socket socket = channel.socket();
			SocketAddress remoteAddr = socket.getRemoteSocketAddress();
			System.out.println("Connection closed by client: " + remoteAddr);
			channel.close();
			key.cancel();
			return;
		}
		
		byte[] data = new byte[numRead];
		System.arraycopy(buffer.array(), 0 ,data, 0 ,numRead);
		System.out.println("Got: " + new String(data));
		StringTokenizer st = new StringTokenizer(new String(data)," ");
		 while (st.hasMoreTokens()) {  
         System.out.println(st.nextToken());  
     }  
		
		double totalprice=0;
		
		ArrayList<Product> products = new ArrayList<Product>();
		
		Product product1 = new Product();
		Product product2 = new Product();
		Product product3 = new Product();
		
		product1.setName("Whopper");
		product2.setName("Whopper with cheese");
		product3.setName("BK Double Mushroom Swiss");
		
		product1.setPrice(11.95);
		product2.setPrice(14.15);
		product3.setPrice(11.45);
		
		product1.setProductCode(101);
		product2.setProductCode(102);
		product3.setProductCode(103);
		
		products.add(product1);
		products.add(product2);
		products.add(product3);
		
		/*for(Product p:products){
			if(data.equals(p.getProductCode())){
				System.out.println(data[1]);
				//totalprice+=p.getPrice()*data[1];
				}
			}*/	
		System.out.println("total price is "+data[0]);
		}
}
				