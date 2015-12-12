import java.util.Random;

public class Main {
	
	public static void main(String[] args){
		FEC forward_error_correction = new FEC(256, 32);
		boolean[] test = new boolean[32];
		Random rand = new Random();
		int max = 1;
		int min = 0;
		System.out.println("Data:\n");
		for (int i=0; i<test.length; i++) {
			if ((rand.nextInt((max - min) + 1) + min) == 1) 
				test[i] = true;
			else 
				test[i] = false;
			if (i == 16)
				System.out.print("\n");
			System.out.print(test[i] + " ");
		}
		System.out.print("\n");
		System.out.print("Encoded message to send:\n");
		boolean[][] encoded_data = forward_error_correction.encode(test);
		int total = encoded_data.length;
		int cols = encoded_data[0].length;
		int rows = total / cols;
		for (int i=0; i<cols; i++) {
			for (int j=0; j<rows; j++) {
				System.out.print(encoded_data[i][j]);
			}
			System.out.print("\n");
		}
		// SEND encoded_data
	}
}
