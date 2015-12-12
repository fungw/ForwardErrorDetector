import java.util.Random;
 
public class FEC {
    /*
     *	Description:  R(255,223) n=223 k=32 [k= 255-223]
     *
     *  n=no_data_bits & k= no_parity bits
     */
    private int k;
    private int n;
    private boolean [][] encoding;
    private boolean [][] inverse;

    public FEC (int n, int k)
    {
        /*
         * Robert McCaffrey
         */
        this.n = n;
        this.k = k; 
        this.encoding = create_encoding_matrix(n, k);
        this.inverse = inverse(this.encoding);
    }

    /**
     *	Wesley Fung
     *  Encoding data
     */
     public  boolean[][] encode(boolean[] data) {
     		boolean[][] matrix_2d = create_matrix_data(data); 
        return multiply(matrix_2d, this.encoding);
     }
  
     /**
     *  Wesley Fung
     *  Decoding data
     */
     public  boolean[][] decode(boolean[] data) {
             boolean[][] matrix_2d = create_matrix_data(data);
             return multiply(matrix_2d, this.inverse);
     }

    /**
     * Robert McCaffrey
     *
     * Convert 1D array to 2D array
     *
     */
    private boolean[][] create_matrix_data(boolean[] bits)
    {
            double sqrt=Math.sqrt(bits.length);
            int size=(int)sqrt;
            boolean[][] matrix= new boolean[size][size];
            int index_bits=0;
            for(int i=0;i<size;i++)
                    for(int j=0;j<size;j++)
                    matrix[i][j]=bits[index_bits++];
            return matrix;
    }
   
    /**
     * Robert McCaffrey
     *
     * Create the encoding matrix with an identity of size n and parity  of size n 
     *
     */
    private boolean[][] create_encoding_matrix(int n,int k)
    {
            int size = n-1;
            // Create Identity Matrix
            boolean[][] encoding_matrix = create_identity_matrix(size);
            //Create Random Number Generator between 0 and 1
            Random rand = new Random();
            int max=1;
            int min=0;
        int amount_parity_bits=k;
        //Add Parity Bits to identity matrix
            for(int i=size;i>0;i--)
            {
                    for(int j1=0; j1<size;j1++)
                    {
                            if((rand.nextInt((max - min) + 1) + min)==0 )
                            {
                                    encoding_matrix[i][j1]=false;

                            }
                            else
                            {
                                    encoding_matrix[i][j1]=true;
                            }
                    }
                    if(amount_parity_bits--==0)
                            break; // Create all parity bits
            }
            assert(amount_parity_bits<-1);
            return encoding_matrix;
    }

    /**
     * Robert McCaffrey
     *
     * where size is the dimension of your identity matrix I(size X size)
     */
    private boolean[][] create_identity_matrix(int size)
    {
            boolean[][] identity_matrix = new boolean[size][size];
            // Create Identity Matrix
            int j=0;
            for(int i=0; i<size;i++)
            {
                    identity_matrix[j++][i]=true;
            }
            return identity_matrix;
    }
 
    /**    
    *  Wesley Fung
    *  Generate an inverse of the encoding
    */
    private boolean[][] inverse(boolean[][] encoding) {
        boolean[][] identity = create_identity_matrix(encoding.length);
        return multiply(encoding, identity);
    }
 
    /**
    *  Wesley Fung
    *  Converts a 2D array into a 1D array
    */
    private boolean[] decreate_matrix_data(boolean[][] matrix) {
        int matrix_size = matrix[0].length;
        boolean[] data = new boolean[matrix_size * 2];
        int data_index_counter = 0;
        for (int i = 0; i < matrix_size; i++) {
                for (int j = 0; j < matrix_size; j++) {
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
                int A_total = A.length;
                int A_row_col = A[0].length;
                int B_total= B.length;
                int B_row_col = B[0].length;
                if (A_row_col != B_total) throw new RuntimeException("Matrix dimensions do not match!");
                    boolean[][] C = new boolean[A_total][B_row_col];
                for (int i = 0; i < A_total; i++)
                    for (int j = 0; j < B_row_col; j++)
                    for (int k = 0; k < A_row_col; k++)
                            C[i][j] = A[i][k] & B[k][j];
                return C;
        }
}
