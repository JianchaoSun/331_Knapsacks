import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class Knapsack0_1 {
	private int size;
	private List<Integer> itemToSteal;
	private int[][] map;
	private List<obj> stealList;
	
	public static void main(String[] args) throws FileNotFoundException{
		Knapsack0_1 ks = new Knapsack0_1(0,0);
		Scanner sc = new Scanner(System.in);
		System.out.println("Do you want to read from file or write input(1 for read file, 2 for write input 3 for time test)");
		int c = sc.nextInt();
		//three choice for user to choose, 1 for reading file, 1 for reading from input, 1 for test speed
		if(c == 1){
			System.out.println("Enter file path:");
			String path = sc.next();
			System.out.println("Enter size:");
			int size = sc.nextInt();
			ks.testByReadingFile("C:\\Users\\clark\\Desktop\\3310.txt", size);
		}else if (c==2){
			System.out.println("Enter number of item:");
			int num = sc.nextInt();
			System.out.println("Enter size:");
			int size = sc.nextInt();
			ks.testByAskInput(num, size);
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
	
	public Knapsack0_1(int i,int w){
		//initiaze varibles, i is number item to choose, w is size of sack 
		stealList = new ArrayList<obj>();
		itemToSteal = new ArrayList<Integer>();
		size = w;
		map = new int[i+1][w+1];
	}
	
	public void reset(int i,int w){
		//reset varible, not needed
		stealList = new ArrayList<obj>();
		itemToSteal = new ArrayList<Integer>();
		size = w;
		map = new int[i+1][w+1];
	}
	
	public void testByReadingFile(String file,int size) throws FileNotFoundException{
		//read a file and operate basic operation
		Knapsack0_1 ks = new Knapsack0_1(0,size);
		ks.readFile(file);
		ks.init_Map();
		ks.put();
		ks.printM();
		ks.traceBack(ks.map.length-1,ks.map[0].length-1);
		ks.printNeedItem();
	}
	
	public void testByAskInput(int number_of_item,int size){
		//ask user for input
		Knapsack0_1 ks = new Knapsack0_1(number_of_item, size);
		ks.askUserForInput();
		ks.init_Map();
		ks.put();
		ks.printM();
		ks.traceBack(ks.map.length-1,ks.map[0].length-1);
		ks.printNeedItem();
	}
	
	public void testByRandom(int total){
		Knapsack0_1 ks = new Knapsack0_1(total, 100);
		ks.addRandom(total);
		long startTime = System.nanoTime();
		ks.init_Map();
		ks.put(); //just calculate the time to construct matrix
		long stopTime = System.nanoTime();
		long time = stopTime - startTime;
		System.out.println("Input: "+total+" used: "+ time+ " nanosecond");
  		
		}
	
	public void addRandom(int total){
		//add random number
		Random rd = new Random();
		for(int i =0;i<total;i++){
			stealList.add(new obj(rd.nextInt(300),rd.nextInt(50),i));
		}
	}
	
	public void traceBack(int i,int j){
		//trace from back to find which item to choose
		if(i>0&&j>0){//if i or j reach 0, that means we are done
			if(map[i][j]>map[i-1][j]){ //to see if current item give us the largest weight
				itemToSteal.add(i); //add if so
				traceBack(i-1,j-(int)stealList.get(i-1).weight);//keep tracking above item, subtract the weight of current item
			}else{
				traceBack(i-1,j);// if current item did not give us largest weight, check if it is the item above it
			}
			
		}
		
	}
	
	public void askUserForInput(){
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
			stealList.get(t).weight = Integer.parseInt(weights[t]);
		}
		
	}
	public void printNeedItem(){
		// print the needed item to max profit
		System.out.println("Need following item: ");
		for(int x:itemToSteal){
			System.out.print(x+",");
		}
	}
	

	public void init_Map(){
		//initiaze the map with 0s
		for(int i=0;i<map.length;i++){
			for(int j=0;j<map[0].length;j++){
				map[i][j]=0;
			}
		}
	}
    	
	
	public void put(){
		//create the 2d aarray that give us final answer
		for(int i =1;i<map.length;i++){
			for(int j=1;j<map[0].length;j++){ //use 2 for loop to traversal the 2d array
				if(j-(int) stealList.get(i-1).weight<0){ //check if w-wi < 0,if so, just put the price above it in its spot
					map[i][j]=map[i-1][j];
				}else{
				map[i][j] =(int) Math.max(map[i-1][j], map[i-1][j-(int) stealList.get(i-1).weight]+stealList.get(i-1).price);
				//if w-wi>=0, just follow the formula
				}
			}
		}
		
	}
	
	public void printM(){
		//print the matrix we construct
		for(int i=0;i<map.length;i++){
			for(int j=0;j<map[0].length;j++){
				System.out.print(map[i][j]+",");
			}
			System.out.println("");
		}
	}
	
	public void readFile(String fil) throws FileNotFoundException{
		//read file and store them in a list
		int count=1; //counter for number of element
		Scanner scanner = new Scanner(new FileReader(fil)); //path is changeable
		  try
		  {
		    while( scanner.hasNext() )
		    {
		    	int price = 0;
		        int weight = 0;       	
		        price = scanner.nextInt(); 
		        weight = scanner.nextInt();
		        stealList.add(new obj(price,weight,count)); //add to list
		        count++;
		    }
		  } finally
		  {
			    scanner.close();
			  }
		  map = new int[count][size+1];
	}
	
	public class obj {
		//just a class to represent a object that can be steal
		double weight; //to make code simple, I didn't make them private
		double price; 
		int rank;
		public obj(double p, double w,int r){
			price = p;
			weight = w;
			rank = r;
		}
		public obj(double p,int r){
			price = p;
			rank = r;
		}
		
	}

}
