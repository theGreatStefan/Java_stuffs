import java.util.Scanner;
import java.util.NoSuchElementException;
import java.io.*;

public class saveTheUniverseAgain {
	private static Scanner sc;
	private static File file;
	private static String T;
	private static int D;

	public static void main(String[] args) {
		String filename, testcase;
		int testnum = 0;

		filename = args[0];
		read_initial_input(filename);
		
		while (sc.hasNext()) {
			testnum++;
			testcase = read_next_test();
			String finaltest = start_mission(testnum);
			System.out.println("[+] Final hacked program: "+finaltest);
		}

	}

	private static String start_mission(int testnum) {
		String feedback = "IMPOSSIBLE", test;
		int currDamage, switches = 0;	

		test = T;
		currDamage = check_damage(test);

		while (!check_success(currDamage)) {
			test = swap_test(test);
			if (test.equals("IMPOSSIBLE")) {
				return "#"+testnum+": "+test;
			}
			switches++;
			currDamage = check_damage(test);
		}

		feedback = "#"+testnum+": "+switches+" ===> " + test;
		return feedback;
	}

	private static String swap_test(String test) {
		boolean done = false;
		int length = test.length(), i=0, j=1;
		char currChar, nextChar;
		char[] charArr;

		charArr = test.toCharArray();

		while (!done) {
			currChar = charArr[i];
			if (currChar == 'C') {
				nextChar = charArr[j];
				if (nextChar == 'S') {
					charArr[i] = 'S';
					charArr[j] = 'C';
					break;
				}
			}
			i++;
			j++;
			if (j >= length) {
				done = true;
			}
		}

		if (!done) {
			test = new String(charArr);
			return test;
		}
		return "IMPOSSIBLE";
	}

	private static int check_damage(String hackedTest) {
		int totalDamage = 0;
		int charge = 1;
		char currChar;

		for (int i=0; i< hackedTest.length(); i++) {
			currChar = hackedTest.charAt(i);
			if (currChar == 'S') {
				totalDamage += charge;
			} else if (currChar == 'C') {
				charge++;
			} else {
				System.out.println("Invalid testcase.");
			}
		}

		return totalDamage;
	}

	private static boolean check_success(int currDamage) {
		if (currDamage <= D) {
			return true;
		}
		return false;
	}

	/**
	 * Initialises the file and scanner for the test cases.
	 **/
	private static void read_initial_input(String filename) {
		file = new File(filename);
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFound!");
			System.exit(0);
		}
	}

	/**
	 * Splits the testcase into D and T, where D is the total damage
	 * that the shield can take and T is the program for the robot.
	 **/
	private static void disect_test_case(String testcase) {
		D = Integer.parseInt(testcase.substring(0,testcase.indexOf(":")));
		T = testcase.substring(testcase.indexOf(":")+2);
	}

	/**
	 * Reads the next test case from the specified file.
	 **/
	private static String read_next_test() {
		String testcase = "";
		try {
			testcase = sc.nextLine();
			disect_test_case(testcase);
		} catch (NoSuchElementException e) {
			System.out.println("EndOfFile");
			System.exit(0);
		}

		return testcase;
	}

}
