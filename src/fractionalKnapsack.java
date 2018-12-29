import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class fractionalKnapsack {
	private double gain ;
	private double size;
	private List<Integer>itemToSteal;
	private List<obj>stealList;
	
	public static void main(String[] args) throws FileNotFoundException{
		fractionalKnapsack ks =new fractionalKnapsack(3);
		Scanner sc = new Scanner(System.in);
		System.out.println("Do you want to read from file or write input(1 for read file, 2 for write input 3 for time test)");
		int c = sc.nextInt();
		if(c == 1){ //3 choices, 1 for reading file, 1 for read user input, last one for test of speed
			System.out.println("Enter file path:");
			String path = sc.next();
			System.out.println("Enter size:");
			int size = sc.nextInt();
			ks.testByReadingFile(path, size);
		}else if (c==2){
			System.out.println("Enter size:");
			int size = sc.nextInt();
			ks.testByAskInput(size);
		}else{
			ks.testByRandom(5);
			ks.testByRandom(10);
			ks.testByRandom(20);
			ks.testByRandom(50);
			ks.testByRandom(100);
			ks.testByRandom(200);
			ks.testByRandom(500);
			ks.testByRandom(1000);
			ks.testByRandom(5000);
			ks.testByRandom(10000);
			ks.testByRandom(50000);
			ks.testByRandom(100000);
			ks.testByRandom(500000);
			ks.testByRandom(1000000);
			ks.testByRandom(2000000);
			ks.testByRandom(3000000);
		}
	}
	
	public fractionalKnapsack(double s){
		//simple constructor, set the size of knapsack 
		size = s;
		stealList = new ArrayList<obj>();
		itemToSteal = new ArrayList<Integer>();
	}
	
	public fractionalKnapsack(double s,int numOfObj){
		//set both size and number of item to steal
		size = s;
		stealList = new ArrayList<obj>();
		itemToSteal = new ArrayList<Integer>();
	}
	
	public void testByReadingFile(String file, int size) throws FileNotFoundException{
		fractionalKnapsack ks =new fractionalKnapsack(size);
		ks.readFile(file);
		//after read file, perform basic operation of fractional knapsack algorthm
		ks.sort();
		ks.put();
		ks.getPrice();
		ks.printNeedItem();
	}
	
	public void testByAskInput(int size){
		//ask user for input and perform basic operation
		fractionalKnapsack ks = new fractionalKnapsack(size);
		ks.askUserForInput();
		ks.sort();
		ks.put();
		ks.getPrice();
		ks.printNeedItem();
	}
	
	public void testByRandom(int total){
		//to test speed of different input size
		fractionalKnapsack ks = new fractionalKnapsack(100,total);
		ks.addRandom(total);
		long startTime = System.nanoTime();
		ks.sort();//need calculate time to sort and too add sorted item into array
		ks.put(); 
		long stopTime = System.nanoTime();
		long time = stopTime - startTime;
		System.out.println("Input: "+total+" used: "+ time+ " nano 3"
				+ "time");
  		
		}
	
	public void addRandom(int total){
		//add random number to stealble item
		Random rd = new Random();
		for(int i =0;i<total;i++){
			stealList.add(new obj(rd.nextInt(300),rd.nextInt(50),i));
		}
	}
	
	private void printNeedItem() {
		//print which we need to steal
		System.out.print("Need following items: ");
		for(int x:itemToSteal){
			System.out.print(x+",");
		}
		
	}

	public void calculateMax(double si,double sack) throws FileNotFoundException{
		size = si;
		//simple method to print the max profit
		fractionalKnapsack ks =new fractionalKnapsack(sack);
		ks.readFile("");
		ks.sort();
		ks.put();
		System.out.println("The maximum profit is" +ks.getPrice());
	}
	
	public double getPrice(){
		System.out.println("Max profit is "+gain);
		return gain;
	}
	
	public void put(){
		// after sorting, determine how large the portion we want to take from each item
		for(int i=0;i<stealList.size();i++){
			//traverse through sorted list
			if(stealList.get(i).weight<size){
				/*the size is changing every time we add new item, if we can fit 
				*the whole current item in, we do so, and update the remaining size and the price we can get
				*and update the item we need to steal.
				*/
				itemToSteal.add(stealList.get(i).rank);
				gain += stealList.get(i).price;
				size = size-stealList.get(i).weight;
			}else{
				/*If we cant fit the whole current item in, we just calculate the percentage of item we can fit
				 * and we put the portion in and update item to steal list, and stop the traversal.
				 */
				gain += stealList.get(i).price*(size/stealList.get(i).weight);
				itemToSteal.add(stealList.get(i).rank);
				i=stealList.size();//to end this iteration
			}
		}
	}
	
	public void sort(){
		//since i implements Comparable to my object class, i can use java's sorting method to sort my list
		Collections.sort(stealList);
		//System.out.println("Rank after sorting");
		//for(obj x: stealList){
			//System.out.println("Item "+x.rank+": "+x.ratio);
	//	}
	}
	
	public void readFile(String fil) throws FileNotFoundException{
		int i=1;
		Scanner scanner = new Scanner(new FileReader(fil)); //path is changeable
		  try
		  {
		    while( scanner.hasNext() )
		    {
		    	//just read file and store value to list
		    	int price = 0;
		        int weight = 0;       	
		        price = scanner.nextInt(); 
		        weight = scanner.nextInt();
		        stealList.add(new obj(price,weight,i));
		        i++;
		    }
		  } finally
		  {
			    scanner.close();
			  }
	}
	
	public void askUserForInput(){
		//method for reading users input
		// just scan 2 list of number and store them
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter a list of price separated by ',' ");
		String lis = sc.nextLine();
		String[]price = lis.trim().split(",");
		int i=1;
		// i is just the label or number of the item 
		for(String p: price){
			stealList.add(new obj(Integer.parseInt(p),i));
			i++;
		}
		System.out.println("Enter a list of weight separated by ','(In same order with your price list)");
		String w = sc.nextLine();
		String[]weights = w.trim().split(",");
		for(int t=0;t<stealList.size();t++){
			stealList.get(t).addW(Integer.parseInt(weights[t]));
		}
		
	}
	
	public class obj implements Comparable<obj>{
		//simple class to hold 3 value we need, 
		//implements comparable interface so we can sort it easily
		double weight;
		double price;
		double ratio;
		int rank;
		public obj(double p, double w,int r){
			price = p;
			weight = w;
			rank = r;
			ratio = p/w;
			//calculate p/w when we build new object
		}
		public obj(double p,int r){
			price = p;
			rank = r;
		}
		public void addW(double w){
			weight = w;
			ratio = price/w;
		}
		public int compareTo(obj o) {
			//compare method,can be used to sort from large to small
			return Double.compare(o.ratio,ratio);
		}
		
	}
	
}
