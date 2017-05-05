import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

class HuffmanNode {
	int data;
	int frequency;
	HuffmanNode leftChild, rightChild;
	
	HuffmanNode(){
		this.data = 0;
		this.frequency = 0;
		this.leftChild = null;
		this.rightChild = null;
	}
	
	HuffmanNode(int data, int frequency, HuffmanNode leftChild, HuffmanNode rightChild){
		this.data = data;
		this.frequency = frequency;
		this.leftChild = leftChild;
		this.rightChild = rightChild;
				
	}
}

/*class PairingHeap{
	class TreeNode{
		int frequency;
		int data;
		TreeNode child, prev, next,left,right;
		
		
		public TreeNode() {
			this.frequency = 0;
			this.data = -1;
			this.child = null;
			this.prev = null;
			this.next = null;
			this.left = null;
			this.right = null;
		}
		
		public TreeNode(int frequency, int data, TreeNode child, TreeNode prev, TreeNode next) {
			this.frequency = frequency;
			this.data = data;
			this.child = child;
			this.prev = prev;
			this.next = next;
		}
	}
	
	TreeNode root=null;
	
	private TreeNode meld(TreeNode t1, TreeNode t2){
		if(t1==null){
			return t2;
		}
		if(t2==null){
			return t1;
		}
		if(t1.frequency > t2.frequency){	
			TreeNode temp=t2.child;
			t2.child=t1;
			t1.prev=t2;
			t1.next=temp;
			if(temp!=null)
				temp.prev=t1;
			t2.prev=null;
			t2.next=null;
			return t2;
		}
		else{
			TreeNode temp=t1.child;
			t1.child=t2;
			t2.prev=t1;
			t2.next=temp;
			if(temp!=null) 
				temp.prev=t2;
			t1.prev=null;
			t1.next=null;
			return	t1;
		}
	
	}
	private TreeNode insert(TreeNode node){
		return meld(root,node);
	}
	public void buildHeap(int[] frequency) {
		for(int i=0;i<frequency.length;i++){
			if(frequency[i]>0){
				root = insert(new TreeNode(i,frequency[i],null,null,null));
				//System.out.println(root.data+" "+root.freq+" ");
			}
		}
	}

	public TreeNode extractMin() {
		LinkedList<TreeNode> multipassQueue=new LinkedList<>();
		
		if(root==null){
			return null;
		}
		TreeNode child=root.child;
		TreeNode min=new TreeNode(root.data,root.frequency, null,null,null);
		min.left = root.left;
		min.right = root.right;
		if(child!=null){
			child.prev=null;
			TreeNode temp=null;
			while(child!=null){
				temp=child.next;
				child.next=null;
				child.prev=null;
				multipassQueue.add(child);
				child=temp;
			}
			while(multipassQueue.size()>1){	
					multipassQueue.offer(meld(multipassQueue.poll(),multipassQueue.poll()));
			}
			root=multipassQueue.poll();
		}
		else{
			root=null;
		}
		return min;
	}

	public TreeNode getMin() {
		return extractMin();
	}

	public int getShift() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setRoot(TreeNode minNode) {
		// TODO Auto-generated method stub
		root = insert(minNode);
	}

	public int getSize() {
		// TODO Auto-generated method stub
		if(root!=null && root.child!=null){
			return 2;
		}
		return 0;
	}

	public TreeNode getRoot() {
		// TODO Auto-generated method stub
		return root;
	}
	
	public void createHuffmanTree(){
		while(getSize() > 0){
			TreeNode minNode = extractMin();
			TreeNode head = root; 
			TreeNode newNode = new TreeNode(-1, minNode.frequency + head.frequency, null, minNode, head);
			setRoot(newNode);
		}
	}
	 
}
*/
class PairingHeap{
	class Node{
		HuffmanNode data;
		Node child;
		Node leftSibling, rightSibling;
		
		public Node(HuffmanNode data, Node child, Node leftSibling, Node rightSibling){
			this.data = data;
			this.child = child;
			this.leftSibling = leftSibling;
			this.rightSibling = rightSibling;
		}
		public String toString(){
			return " "+ data +" ";
		}
	}
	
	public Node head = null;
	int size = 0;
	static HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
	
	private Node meldPairingHeap(Node p1, Node p2){
		Node smallHeap = p1.data.frequency < p2.data.frequency ? p1 : p2;
		Node bigHeap = p1.data.frequency >= p2.data.frequency? p1 : p2;
		Node tempChild = smallHeap.child;
		smallHeap.child = bigHeap;
		bigHeap.rightSibling = tempChild;
		bigHeap.leftSibling = smallHeap; 
		if(tempChild != null)
			tempChild.leftSibling = bigHeap;
		return smallHeap;
	}
	
	public void insert(HuffmanNode data){
		Node newTree = new Node(data, null, null, null);
		size++;
		if(head == null){
			head = newTree;
		}
		else
			head = meldPairingHeap(head, newTree);
	}
	
	public Node removeMin(){
		Node headTemp = head;
		Node tempChild = head.child;
		head.child = null;
		ArrayList<Node> vector = new ArrayList<Node>();
		if(tempChild == null)
		{
			size--;
			head = null;
			return headTemp;
			
		}
		while(tempChild != null){
			tempChild.leftSibling = null;
			Node nextSibling = tempChild.rightSibling;
			tempChild.rightSibling = null;
			vector.add(tempChild);
			tempChild = nextSibling;
		}
		
		while(vector.size() > 1){
			Node p1 = vector.remove(0);
			Node p2 = vector.remove(0);
			Node merged = this.meldPairingHeap(p1, p2);
			vector.add(merged);
		}
		size--;
		head = vector.remove(0);
		return headTemp;
	}
	
	public void buildHeap(int[] frequency){
		for(int i = 0 ; i < frequency.length; i++){
			if(frequency[i] > 0){
				insert(new HuffmanNode(i, frequency[i], null, null));
			}
		}
	}
	
	public void createHuffmanTree() {
		// TODO Auto-generated method stub
		while(size >= 2){
			Node small = removeMin();
			Node big = removeMin();
			HuffmanNode huffmanNode = new HuffmanNode(-1, small.data.frequency + big.data.frequency, small.data, big.data);
			insert(huffmanNode);
		}
		head = removeMin();
	}
	
	private void generateCodes(HuffmanNode head, String code){
		if(head != null){
			if(head.leftChild != null)
				generateCodes(head.leftChild, code + "0");
			if(head.leftChild == null && head.rightChild == null){
				hashMap.put(head.data, code);
			}
			if(head.rightChild != null)
				generateCodes(head.rightChild, code + "1");
		}
	}
	
	public void display(){
		System.out.println("\nPairing Heap: ");
		LinkedList<Node> queue = new LinkedList<Node>();
		Node temp = head;
		queue.add(temp);
		while(queue.isEmpty() == false){
			temp = queue.remove();
			while(temp!=null){
				if(temp.child != null)
					queue.add(temp.child);
				System.out.print(" " + temp.toString() + " ");
				temp = temp.rightSibling;
			}
		}
	}

	public void printCodes() {
		// TODO Auto-generated method stub
		generateCodes(head.data, "");
		System.out.println(hashMap);
	}
	
}

class BinaryHeap{

	int shift,size;
	HuffmanNode[] heap;
	
	public BinaryHeap() {
		shift = 0;
		heap = new HuffmanNode[1000004];
		size = shift;
	}

	public void buildHeap(int[] frequencyArray) {
		if(frequencyArray == null)
			return;

		for(int i = 0; i < frequencyArray.length; i++){
			
			if(frequencyArray[i]>0){
				heap[size++]=new HuffmanNode(i, frequencyArray[i], null, null);
			}
		}

		if(size>shift)
			size--;

		for(int i = parent(size); i>=shift; i--){
			minHeapify(i);
		}
		
	}

	public HuffmanNode extractMin() {
		if(size<shift)
			return null;
		HuffmanNode temp = heap[shift];
		heap[shift] = heap[size];
		heap[size] = null;
		size--;
		minHeapify(shift);
		return temp;
	}

	public void minHeapify(int index) {
		int minChild = peekChild(index);
		if(minChild == index)
			return;
		else {
			HuffmanNode temp = heap[index];
			heap[index] = heap[minChild];
			heap[minChild] = temp;
			minHeapify(minChild);
		}
		
	}

	public void decreaseKey(int index, int newfrequency) {
		// TODO Auto-generated method stub
		if(heap[index].frequency < newfrequency)
			return;
		int parent = parent(index);
		heap[index].frequency = newfrequency;
		HuffmanNode temp;
		while(parent >= shift && heap[parent].frequency > heap[index].frequency){
			temp=heap[parent];
			heap[parent]=heap[index];
			heap[index]=temp;
			index=parent;
			parent = parent(index);
		}
		
	}

	public HuffmanNode peek() {
		if(size>=shift){
			return heap[shift];
		}
		return null;
	}

	public int parent(int index) {
		// TODO Auto-generated method stub
		return ((index+1)/2)-1;
	}

	public int child(int i, int j) {
		// ith child of j
		return 2*(j+1)+i-1;
	}

	public int peekChild(int index) {
		int smallest = index;
		for(int i=0; i < 2; i++){
			if(child(i,index) <= size){
					if(heap[child(i,index)].frequency < heap[smallest].frequency){
						smallest = child(i, index);
					}
					
			}
		}
		return smallest;
	}

	public void setRoot(HuffmanNode minNode) {
		// TODO Auto-generated method stub
		heap[shift]=minNode;
		minHeapify(shift);
	}
	
	public void createHuffmanTree(){
		while(size > shift){
			HuffmanNode minNode = extractMin();
			HuffmanNode head = peek(); 
			HuffmanNode newNode = new HuffmanNode(-1, minNode.frequency + head.frequency, minNode, head);
			setRoot(newNode);
		}
	}
}

class FourWayHeap {

	int shift, size;
	HuffmanNode[] heap;
	
	public FourWayHeap() {
		shift = 3;
		size = 3;
		heap = new HuffmanNode[1000004];
	}
	
	public void buildHeap(int[] frequency) {
		if(frequency == null)
			return;
		
		for(int i = 0; i < frequency.length; i++){
			if(frequency[i] > 0){
				heap[size++] = new HuffmanNode(i, frequency[i], null, null);
			}
		}
		
		if(size > shift)
			size--;
		
		for(int i = parent(size); i >= shift; i--){
			minHeapify(i);
		}
	}

	public void minHeapify(int index) {
		int minChild = peekChild(index);
		if(minChild == index)
			return;
		else {
			HuffmanNode temp = heap[index];
			heap[index] = heap[minChild];
			heap[minChild] = temp;
			minHeapify(minChild);
		}
	}
	
	public HuffmanNode extractMin() {
		if(size < shift)
			return null;
		HuffmanNode temp = heap[shift];
		heap[shift] = heap[size];
		heap[size] = null;
		size--;
		minHeapify(shift);
		return temp;
	}

	public HuffmanNode peek() {
		if(size >= shift){
			return heap[shift];
		}
		return null;
	}

	public int parent(int index) {
		if(index > shift)
			return ((index - shift- 1)/4) + shift;
		return -1;
	}

	public int child(int i, int j) {
	 	return ((j - shift) * 4) + i + shift;
	}
	
	public int peekChild(int index) {
		int smallest = index;
		for(int i = 1; i <= 4; i++){
			if(child(i,index) <= size){
					if(heap[child(i,index)].frequency < heap[smallest].frequency){
						smallest = child(i, index);
					}
			}
		}
		return smallest;
	}

	public void setRoot(HuffmanNode minNode) {
		heap[shift] = minNode;
		minHeapify(shift);
	}
	
	public void createHuffmanTree(){
		while(size > shift){
			HuffmanNode minNode = extractMin();
			HuffmanNode head = peek(); 
			HuffmanNode newNode = new HuffmanNode(-1, minNode.frequency + head.frequency, minNode, head);
			setRoot(newNode);
		}
	}
}


public class encoder {

	HashMap<Integer, String> codeTable = new HashMap<Integer,String>();
	
	
	
	public void generateCode(HuffmanNode root, StringBuilder strBuf){
		if(root != null && root.leftChild == null && root.rightChild == null){
			codeTable.put(root.data, strBuf.toString());
			return;
		}
		if(root.leftChild != null){
			strBuf.append('0');
			generateCode(root.leftChild, strBuf);
			strBuf.deleteCharAt(strBuf.length() - 1);
		}
		if(root.rightChild != null){
			strBuf.append('1');
			generateCode(root.rightChild, strBuf);
			strBuf.deleteCharAt(strBuf.length() - 1);
		}
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		int[] frequencyTable = new int[1000000];
		File codeFile = new File("code_table.txt");
		File inputFile = new File(args[0]);
		
		BufferedWriter codeFileWriter = null;
		BufferedReader inputReader = null;
		BufferedOutputStream encodedBinOutput = null;
		
		encoder enc = new encoder();
		FourWayHeap tree = null;
		try {
		    
			
			inputReader = new BufferedReader(new FileReader(inputFile));
		    readDataFromFile(inputReader, frequencyTable);
		    System.out.println("Reading Time: " + (System.currentTimeMillis()-start));
		    start = System.currentTimeMillis();
		    

			testBinaryHeap(frequencyTable);
			testFourWayHeap(frequencyTable);
			testPairingHeap(frequencyTable);
			
		    tree = new FourWayHeap(); 
		    tree.buildHeap(frequencyTable);
		    System.out.println("Time to Build the heap: " + (System.currentTimeMillis()-start));
		    
		    tree.createHuffmanTree();
			
		    enc.generateCode(tree.peek(), new StringBuilder());
		    codeFileWriter = new BufferedWriter(new FileWriter(codeFile));
		    enc.writeCodeTableOnFile(codeFileWriter);
			codeFileWriter.close();
			
	    	//encode the data in a binary file
	    	start = System.currentTimeMillis();
	    	encodedBinOutput = new BufferedOutputStream(new FileOutputStream("encoded.bin"));
	    	inputReader = new BufferedReader(new FileReader(inputFile));
	    	enc.encodeInput(inputReader, encodedBinOutput);
	    	System.out.println("Encoding Time: " +(System.currentTimeMillis()-start));
		}
		catch(IOException ioE){
			System.out.println("Exception occured  " + ioE.getMessage());
			ioE.printStackTrace();
		}
		finally{
			if(encodedBinOutput != null){
				encodedBinOutput.close();
		    }
		    if (codeFileWriter != null){
		        codeFileWriter.close();
		    }		    
		}
	}
	private static void testPairingHeap(int[] frequencyTable) {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		for(int i = 0 ;i < 10; i++){
			PairingHeap pairingHeap = new PairingHeap();
			pairingHeap.buildHeap(frequencyTable);
			pairingHeap.createHuffmanTree();
		}
		long end = System.currentTimeMillis();
		System.out.println("Pairing Heap Average Performance: " + ((end - start)/10.0f));
	}

	private static void testFourWayHeap(int[] frequencyTable) {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		for(int i = 0 ;i < 10; i++){
			FourWayHeap fourWayHeap = new FourWayHeap();
			fourWayHeap.buildHeap(frequencyTable);
			fourWayHeap.createHuffmanTree();
		}
		long end = System.currentTimeMillis();
		System.out.println("Four Way Heap Average Performance: " + ((end - start)/10.0f));
	}

	private static void testBinaryHeap(int[] frequencyTable) {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		for(int i = 0 ;i < 10; i++){
			BinaryHeap binaryHeap = new BinaryHeap();
			binaryHeap.buildHeap(frequencyTable);
			binaryHeap.createHuffmanTree();
		}
		long end = System.currentTimeMillis();
		System.out.println("Binary Heap Average Performance: " + ((end - start)/10.0f));
		
	}

	public void writeCodeTableOnFile(BufferedWriter codeFileWriter) throws IOException{
		Iterator<Integer> it = codeTable.keySet().iterator();
		while(it.hasNext()){
			Integer temp = it.next();
			codeFileWriter.write(temp + " " + codeTable.get(temp));
			codeFileWriter.newLine();
		}
	}
	
	public static void readDataFromFile(BufferedReader inputReader, int[] frequencyTable) throws IOException{
		String str; 
		while((str = inputReader.readLine()) != null){
			if(str.length() > 0){
		        frequencyTable[(Integer.parseInt(str))]++;
			}
		}
	}
	
	public void encodeInput(BufferedReader inputReader, BufferedOutputStream encodeFileWriter) throws IOException{
			StringBuilder strBuf = new StringBuilder(); 
			String line;
			BitSet bitSet = new BitSet();
			int length = 0;
			while((line = inputReader.readLine()) != null){
				if(line.length() > 0){
				strBuf.append(codeTable.get(Integer.parseInt(line)));
				
				for(int i = 0 ; i < strBuf.length(); i++){
					if(strBuf.charAt(i) == '1'){
						bitSet.set(length + i);
					}
				}
				
				length += strBuf.length();
				strBuf.setLength(0);
				}
			}	
			encodeFileWriter.write(bitSet.toByteArray());	
		}
}
