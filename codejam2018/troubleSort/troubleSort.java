import java.io.*;
import java.util.Scanner;

public class troubleSort {
	static Scanner sc;
	static int totalSeqs = 0, seqLength = 0;

	public static void main(String[] args) {
		String filename;
		int[] seqArr;
		int index;

		filename = args[0];
		read_initial_file(filename);

		for (int i=0; i < totalSeqs; i++) {
			seqArr = read_next_seq();
			seqArr = trouble_sort(seqArr);
			
			for (int j=0; j < seqArr.length; j++) {
				System.out.print(seqArr[j]+" ");
			}
			System.out.print(" ");
			index = find_index(seqArr);
			if (index != -1) {
				System.out.println("\t [-] It all goes wrong at index: "+index);
			} else {
				System.out.println("");
			}
		}
		

	}

	public static int find_index(int[] seqArr) {
		int index = -1;
		for (int i=0; i < seqArr.length-1; i++) {
			if (seqArr[i] > seqArr[i+1]) {
				index = i;
				break;
			}
		}

		return index;

	}

	public static int[] trouble_sort(int[] seqArr) {
		boolean done = false;
		int temp;

		while (!done) {
			done = true;
			for (int i=0; i < (seqLength-2); i++) {
				if (seqArr[i] > seqArr[i+2]) {
					done = false;
					temp = seqArr[i];
					seqArr[i] = seqArr[i+2];
					seqArr[i+2] = temp;
				}

			}
		}
		return seqArr;
	}

	public static int[] read_next_seq() {
		String seq[];
		int[] seqArr;
		seqLength = Integer.parseInt(sc.nextLine());
		seq = sc.nextLine().split(" ");
		seqArr = new int[seqLength];
		for (int i=0; i < seqLength; i++) {
			seqArr[i] = Integer.parseInt(seq[i]);
		}

		return seqArr;
	}

	public static void read_initial_file(String filename) {
		File file = new File(filename);
		try {
			sc = new Scanner(file);
			totalSeqs = Integer.parseInt(sc.nextLine());
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
			System.exit(0);
		}
		
	}

}
