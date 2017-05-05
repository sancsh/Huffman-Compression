
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.BitSet;
import java.util.Scanner;

class DecoderNode {

	Integer data;
	boolean isEnd;
	DecoderNode leftChild,rightChild;
	
	public DecoderNode() {
		data = null;
		isEnd = false;
		leftChild = null;
		rightChild = null;
	}
	
	public DecoderNode(Integer data, boolean isLeaf, DecoderNode leftChild, DecoderNode rightChild) {
		this.data = data;
		this.isEnd = isLeaf;
		this.leftChild = null;
		this.rightChild = null;
	}
	
}

class DecoderTree {
	
	DecoderNode root;
	
	public DecoderTree() {
		root = new DecoderNode();
	}
	
	public DecoderNode getRoot(){
		return root;
	}
	
	public void insert(String code, DecoderNode node, Integer data){
		
		for(int i = 0;i < code.length() ; i++){
			
			if(code.charAt(i) == '0'){
				if(node.leftChild == null){
					node.leftChild = new DecoderNode();
				}
				node = node.leftChild;
			}
			else{
				if(node.rightChild == null){
					node.rightChild = new DecoderNode();
				}
				node = node.rightChild;
			}
		}
		node.data = data;
		node.isEnd = true;
	}
	
}


public class decoder {
	
	public static void main(String args[]) throws IOException{
		decoder dec = new decoder();
		File codeTableFile = new File(args[1]);
		File encodedBinaryFile = new File(args[0]);
		File decodedFile = new File("decoded.txt");

		FileInputStream fileinputStream=null;

		BufferedWriter decodeFileWriter=null;
		BufferedReader codeTableReader=null;
		try{
			
			long start = System.currentTimeMillis();
			codeTableReader = new BufferedReader(new FileReader(codeTableFile));
			
			DecoderTree dTree = new DecoderTree();
			dec.codeTableToDecoderTree(codeTableReader,dTree);
			
			decodeFileWriter = new BufferedWriter(new FileWriter(decodedFile));
			fileinputStream = new FileInputStream(encodedBinaryFile);
			
			long size = encodedBinaryFile.length();
			dec.writeDecodeDataToFile(decodeFileWriter,fileinputStream,dTree,(int)size);
			
			System.out.println("Decoding Time: "+(System.currentTimeMillis()-start));
			//compareFiles();
		}
		finally{
			if (fileinputStream != null) {
				fileinputStream.close();
			}
			if(decodeFileWriter != null){
				decodeFileWriter.close();
			}
			if(codeTableReader != null){
				codeTableReader.close();
			}
		}
	}
	private static void compareFiles() {
		// TODO Auto-generated method stub
		try {
			Scanner f1 = new Scanner(new File("decoded.txt"));
			Scanner f2 = new Scanner(new File("C:\\Users\\hboot\\Downloads\\Project\\sample2\\sample_input_large.txt"));
			int i = 0;
			String str1, str2;
			while(f1.hasNextLine()  && f2.hasNextLine()){
				str1 = f1.nextLine();
				str2 = f2.nextLine();
				if(str1.equals(str2) == false){
					System.out.println("NOT SAME " + str1 + " " + str2);
					break;
				}
				i++;
			}

			System.out.println(i);
			//while(f2.hasNextLine())
				System.out.println(f2.nextLine());
			f1.close();
			f2.close();
			
			System.out.println("SAME");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public void writeDecodeDataToFile(BufferedWriter decodeFileWriter, FileInputStream encodedByteStream, DecoderTree dTree, int fileSize) throws IOException{
		DecoderNode dNode = dTree.getRoot();		
		byte[] b = new byte[fileSize];
		
		encodedByteStream.read(b);
		BitSet bitset=BitSet.valueOf(b);
		bitset.set((b.length) * 8);
		
		for(int i = 0;i < bitset.length()-1; i++){
			if(bitset.get(i) == false){
				dNode=dNode.leftChild;
			}
			else{
				dNode=dNode.rightChild;
			}
			if(dNode.isEnd){	
				decodeFileWriter.write(""+dNode.data);
				decodeFileWriter.write("\n");
				dNode = dTree.getRoot();
			}
		}
	}
	public void codeTableToDecoderTree(BufferedReader codeTableReader, DecoderTree dTree) throws IOException{
		String line = null;
		String[] dataValue = null;
		while ((line = codeTableReader.readLine()) != null) {
			dataValue=line.split(" ");
			dTree.insert(dataValue[1], dTree.getRoot(), Integer.parseInt(dataValue[0]));
		}
	}
}
