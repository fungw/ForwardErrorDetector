import java.util.Random;

public class FEC {
    /*
     *	Description:  R(255,223) n=223 k=32 [k= 255-223]
     *
     *  n=no_data_bits & k= no_parity bits
     */
    private int n;
    private int k;
    private int s;
    private boolean [][] encoding;
    private boolean [][] inverse;

    /*
    *   Polynomial used to encode our message
    *   x^8 = x^4 + x^3 + x^2 + 1
    *   s = 8
    */
    private boolean[] ORIGINAL_POLYNOMIAL = { true, false, true, true, true, false, false, false };
    private boolean[][] IDENTITY_MATRIX;
    private boolean[][] PARITY_MATRIX;

    public FEC (int n, int k, int s)
    {
        /*
         * Robert McCaffrey
         */
        this.n = n; // 256
        this.k = k; // 223
        this.s = s; // 8
        this.encoding = create_encoding_matrix(n, k);
        this.inverse = inverse(this.encoding);
    }

    /**
     *	Wesley Fung
     *  Encoding data
     */
    public boolean[][] encode(boolean[] data) {
        boolean[][] matrix_2d = create_matrix_data(data); 
        boolean[][] result = multiply(this.encoding, matrix_2d);
        System.arraycopy(this.PARITY_MATRIX, 0, result, this.IDENTITY_MATRIX.length-this.PARITY_MATRIX.length, this.PARITY_MATRIX.length);

        // System.out.println("\nENCODING:");
        // for (int i=0; i<encoding.length; i++) {
        //     for (int j=0; j<encoding[0].length; j++) {
        //         System.out.print(encoding[i][j] + " ");
        //     }
        //     System.out.print("\n");
        // }

        // System.out.println("\nMatrix2D:");
        // for (int i=0; i<matrix_2d.length; i++) {
        //     for (int j=0; j<matrix_2d[0].length; j++) {
        //         System.out.print(matrix_2d[i][j] + " ");
        //     }
        //     System.out.print("\n");
        // }

        // System.out.println("\nRESULT:");
        // for (int i=0; i<result.length; i++) {
        //     for (int j=0; j<result[0].length; j++) {
        //         System.out.print(result[i][j] + " ");
        //     }
        //     System.out.print("\n");
        // }
        return result;
   }

     /**
     *  Wesley Fung
     *  Decoding data
     */
     public  boolean[][] decode(boolean[] data) {
         boolean[][] matrix_2d = create_matrix_data(data);
         return multiply(this.encoding, matrix_2d);
     }

    /**
     * Robert McCaffrey
     *
     * Convert 1D array to 2D array
     */
    private boolean[][] create_matrix_data(boolean[] bits)
    {
        double sqrt = Math.sqrt(bits.length);
        int size = (int)sqrt;
        boolean[][] matrix= new boolean[size][this.s];
        int index_bits = 0;
        for(int i=0;i<size;i++) {
            for(int j=0;j<this.s;j++) {
                matrix[i][j]=bits[index_bits];
                index_bits++;
            }
        }
        return matrix;
    }

    /**
     * Robert McCaffrey
     * Wesley Fung
     * Create the encoding matrix with an identity of size n and parity of size k
     */
    private boolean[][] create_encoding_matrix(int n, int k)
    {
        int size = (int) Math.sqrt(n+1); // sqrt 256
        // Create Identity Matrix
        boolean[][] identity_matrix = create_identity_matrix(n);
        int parity_bytes = (size * 2) / this.s;
        boolean[][] parity_matrix = new boolean[parity_bytes][this.s];
        boolean[] polynomial = this.ORIGINAL_POLYNOMIAL;
        // Add Parity Bits to Identity Matrix
        for (int i=0; i<parity_bytes; i++) {
            boolean last = polynomial[polynomial.length-1];
            // Insert the polynomial values into Parity array
            for (int j=0; j<this.s; j++) {
                parity_matrix[i][j] = polynomial[j];
            }
            // Shift the polynomial to the right by one
            for (int u=polynomial.length-2; u>=0; u--) {
                polynomial[u+1] = polynomial[u];
            }
            polynomial[0] = last;
        }
        // Adding the parity bits to the identity matrix to form encoding matrix
        boolean[][] encoding_matrix = new boolean[identity_matrix.length][this.s];
        this.IDENTITY_MATRIX = identity_matrix;
        this.PARITY_MATRIX = parity_matrix;
        System.arraycopy(identity_matrix, 0, encoding_matrix, 0, identity_matrix.length);
        System.arraycopy(parity_matrix, 0, encoding_matrix, identity_matrix.length-parity_matrix.length, parity_matrix.length);

        // System.out.println("Encoding MATRIX");
        // System.out.println("=====BEGIN=====");
        // for (int p=0; p<n; p++) {
        //     for (int l=0; l<this.s; l++) {
        //         System.out.print(encoding_matrix[p][l] + " ");
        //     }
        //     System.out.print("\n");
        // }
        // System.out.println("=====END=====");
        return encoding_matrix;
    }

    /**
     * Robert McCaffrey
     * Wesley Fung
     */
    private boolean[][] create_identity_matrix(int size)
    {
    	boolean[][] identity_matrix = new boolean[size][this.s];
        // Create Identity Matrix
        for (int i=0; i<size; i++) {
            for (int j=0; j<this.s; j++) {
                if (i == j) {
                    identity_matrix[i][j] = true;
                } else {
                    identity_matrix[i][j] = false;
                }
            }
        }

        // System.out.println("IDENTITY MATRIX");
        // System.out.println("=====BEGIN=====");
        // for (int k=0; k<size; k++) {
        //     for (int l=0; l<this.s; l++) {
        //         System.out.print(identity_matrix[k][l] + " ");
        //     }
        //     System.out.print("\n");
        // }
        // System.out.println("=====END=====");
        return identity_matrix;
    }

    /**    
    *  Wesley Fung
    *  Generate an inverse of the encoding
    */
    private boolean[][] inverse(boolean[][] encoding) {
        boolean[][] identity = create_identity_matrix(encoding.length);
        // for (int i=0; i<encoding.length; i++) {
        //     for (int j=0; j<encoding[0].length; j++) {
        //         System.out.print(encoding[i][j] + " ");
        //     }
        //     System.out.print("\n");
        // }
        return multiply(encoding, identity);
    }

    /**
    *  Wesley Fung
    *  Converts a 2D array into a 1D array
    */
    public boolean[] decreate_matrix_data(boolean[][] matrix) {
        int matrix_size = matrix.length;
        boolean[] data = new boolean[matrix_size * matrix[0].length];
        int data_index_counter = 0;
        for (int i = 0; i < matrix_size; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                data[data_index_counter] = matrix[i][j];
                data_index_counter++;
            }
        }
        return data;
    }

    /**
    *  Wesley Fung
    *  Multiples two 2D boolean array together
    *  C = A * B
    */
    private  boolean[][] multiply(boolean[][] A, boolean[][] B) {
        int A_total = A.length; //256
        int A_row = A[0].length; //8
        int A_col = A_total / A_row; //32
        int A_dimen = A_total;
        int B_total= B.length; //256, 14, 45
        int B_row = B[0].length; //8
        int B_col = B_total / B_row; //32, 1, 5
        int B_dimen = B_row;
        // if (B.length != A_row) throw new RuntimeException("Illegal matrix dimensions.");
        boolean[][] C = new boolean[A_dimen][B_dimen];
        for (int i = 0; i < A_total; i++)
            for (int j = 0; j < B_row; j++)
                for (int k = 0; k < A_row; k++) {
                    C[i][j] = C[i][j] || (A[i][k] && B[k][j]);
                }
        return C;
    }
}
