import java.util.Random;

public class Main {
	
	public static void main(String[] args){
		testFEC();
	}

	private static void testFEC() {
		FEC forward_error_correction = new FEC(256, 223, 8);
		boolean[] test = new boolean[64];
		Random rand = new Random();
		int max = 1;
		int min = 0;

		System.out.println("Randomly generated data:");
		System.out.println("=====BEGIN=====");
		for (int i=0; i<test.length; i++) {
			if ((rand.nextInt((max - min) + 1) + min) == 1) 
				test[i] = true;
			else 
				test[i] = false;
//			if ((i%8) == 0)
//				System.out.print("\n");
//			System.out.print(test[i] + " ");
		}
		System.out.print("\n=====END=====\n");

		System.out.print("Encoded message to send:\n");
		System.out.println("=====BEGIN=====");
		boolean[][] encoded_data = forward_error_correction.encode(test);
		System.out.print("=====Interleaved=====\n");
		//***********************************//
		//*Prints out Pre Interleaved Data*//
		//***********************************//
		int total = encoded_data.length;
		int cols = encoded_data[0].length;
		for (int i=0; i<total; i++) {
			for (int j=0; j<cols; j++) {
				System.out.print(encoded_data[i][j] + " ");
			}
			System.out.print("\n");
		}
		System.out.print("=====END=====\n");
		encoded_data = forward_error_correction.transpose_matrix_data(encoded_data);
		System.out.print("=====Deinterleaved=====\n");
		// SEND encoded_data
		//***********************************//
		//*Prints out Newly Interleaved Data*//
		//***********************************//
		total = encoded_data.length;
		cols = encoded_data[0].length;
		for (int i=0; i<total; i++) {
			for (int j=0; j<cols; j++) {
				System.out.print(encoded_data[i][j] + " ");
			}
			System.out.print("\n");
		}
		encoded_data = forward_error_correction.transpose_matrix_data(encoded_data);
		boolean[] decode_test = forward_error_correction.decreate_matrix_data(encoded_data);
		boolean[][] decode_test_2d = forward_error_correction.decode(decode_test);
		int decode_total = decode_test_2d.length;
		int decode_cols = decode_test_2d[0].length;
		System.out.println("\nDecoding the message ...");
		System.out.println("=====BEGIN=====");
		for (int m=0; m<decode_total; m++) {
			for (int n=0; n<decode_cols; n++) {
//				System.out.print(decode_test_2d[m][n] + " ");
			}
//			System.out.print("\n");
		}
		System.out.print("=====END=====\n");
	}
}