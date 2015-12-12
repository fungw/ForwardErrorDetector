import java.util.Random;

public class Main {
	
	public static void main(String[] args){
		FEC forward_error_correction = new FEC(256, 32);
		boolean[] test = new boolean[32];
		Random rand = new Random();
		int max = 1;
		int min = 0;
		System.out.println("We are sending...");
		for (int i=0; i<test.length; i++) {
			if ((rand.nextInt((max - min) + 1) + min) == 1) 
				test[i] = true;
			else 
				test[i] = false;
			System.out.print(test[i] + " ");
		}
		System.out.print("\n");
		boolean[][] encoded_data = forward_error_correction.encode(test);
		// SEND encoded_data
	}
}
