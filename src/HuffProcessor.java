import java.util.Arrays;
import java.util.PriorityQueue;

public class HuffProcessor implements Processor {

	public int[] myFreqs;
	public HuffNode myCompRoot;
	public String[] myCodeArr;
	public HuffNode myDecompRoot;

	public void getFrequencies(BitInputStream in){
		//creates an int array of all of the character frequencies 
		//in the read-in file, size of 256.
		myFreqs = new int[ALPH_SIZE];
		Arrays.fill(myFreqs, 0);
		int bits = in.readBits(BITS_PER_WORD);
		while(bits != -1){
			myFreqs[bits]++;
			bits = in.readBits(BITS_PER_WORD);
		}
		in.reset();
	}

	public void buildHuffTree(){
		//Builds the priority queue of HuffNodes including the EOF
		PriorityQueue<HuffNode> pq = new PriorityQueue<HuffNode>();
		for(int i=0; i<myFreqs.length; i++){
			if(myFreqs[i] > 0){
				HuffNode n = new HuffNode(i, myFreqs[i]);
				pq.add(n);
			}
		}
		pq.add(new HuffNode(PSEUDO_EOF, 0)); //MUST BE ZERO
		//Build up the tree
		while(pq.size()>1){
			HuffNode one = pq.poll();
			HuffNode two = pq.poll();
			HuffNode supTree = new HuffNode(-1, one.weight() + two.weight(), one, two);
			pq.add(supTree);
		}
		myCompRoot = pq.poll();
	}

	public void extractCodes(HuffNode t, String code){
		//Traverse the tree and create an int array of Huffman bit strings
		if(t.value() != -1){
			myCodeArr[t.value()] = code;
			return;
		}
		else{
			extractCodes(t.left(), code + "0");
			extractCodes(t.right(), code + "1");
		}
	}

	public void writeHeader(HuffNode t, BitOutputStream out){
		//Recursively writes the header, w/o the Huffman Number. 
		if(t.value() != -1){
			out.writeBits(1, 1);
			out.writeBits(9, t.value());
			return;
		}
		else{
			out.writeBits(1,0);
			writeHeader(t.left(), out);
			writeHeader(t.right(), out);
		}
	}

	public void writeBody(BitInputStream in, BitOutputStream out){
		//Writes the body of the file by reading in the bits of the file
		//and writing out the corresponding Huffman bits using the Huffman Code array
		int asciiCode = in.readBits(BITS_PER_WORD);
		while(asciiCode != -1){
			String huffCodeStr = myCodeArr[asciiCode];
			int huffCodeBits = Integer.parseInt(huffCodeStr, 2);
			out.writeBits(huffCodeStr.length(), huffCodeBits);
			asciiCode = in.readBits(BITS_PER_WORD);
		}

	}

	@Override
	public void compress(BitInputStream in, BitOutputStream out) {
		//Calls helper methods to compress
		
		getFrequencies(in);
		buildHuffTree();
		myCodeArr = new String[ALPH_SIZE+1];
		extractCodes(myCompRoot, "");
		//add HuffNumber
		out.writeBits(BITS_PER_INT, HUFF_NUMBER); 
		writeHeader(myCompRoot, out);
		writeBody(in, out);
		//write the EOF
		int eofCodeLength = myCodeArr[PSEUDO_EOF].length();
		if("".equals(myCodeArr[PSEUDO_EOF])){
			throw new HuffException("Empty File");
		}
		int eofCodeInt = Integer.parseInt(myCodeArr[PSEUDO_EOF], 2);
		out.writeBits(eofCodeLength, eofCodeInt);		
	}

	public HuffNode readTreeHead(BitInputStream in){
		//decodes the tree header
		int bit = in.readBits(1);
		if(bit == 1){
			int code = in.readBits(9);
			return new HuffNode(code, 1);
		}
		else{
			HuffNode left = readTreeHead(in);
			HuffNode right = readTreeHead(in);
			return new HuffNode(-1, 1, left, right);
		}
	}

	public void read(BitInputStream in, BitOutputStream out){
		//decompresses using the created tree
		HuffNode n = myDecompRoot;
		int bit = in.readBits(1);
		while(bit!=-1){
			if(bit==0){
				n = n.left();
			}
			else{
				n= n.right();
			}
			if(n.value() != -1){
				if(n.value()==PSEUDO_EOF){
					return;
				}
				else{
					out.writeBits(BITS_PER_WORD, n.value());
					n = myDecompRoot;
				}
			}
			bit = in.readBits(1);
		}
	}

	@Override
	public void decompress(BitInputStream in, BitOutputStream out) {
		int flag = in.readBits(BITS_PER_INT);
		if(flag!=HUFF_NUMBER){
			throw new HuffException("Not a compressed File");
		}
		myDecompRoot = readTreeHead(in);
		read(in, out);
	}

}


