package gauss.jordan.elimination;

import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReadInMatrix {
	
    public static void readAndCalculate(String inputFile)
    {
            Scanner input = null;
            try {
                    input = new Scanner (new File(inputFile));
            } catch (FileNotFoundException e) {
                    Logger.getLogger(ReadInMatrix.class.getName(), null).log(Level.SEVERE, "File can't be opened", e);
            }

            int rowTrack = 0;
            int colTrack = 0;
            while(input.hasNextLine())
            {
                rowTrack++;
                Scanner colReader = new Scanner(input.nextLine());
                while(colReader.hasNextDouble())
                {
                    colTrack++;
                    colReader.nextDouble();
                }
                 colReader.close();
            }
            double[][] a = new double[rowTrack][colTrack];
            input.close();

            // read in the data
            try {
                    input = new Scanner (new File(inputFile));
            } catch (FileNotFoundException e) {
                    Logger.getLogger(ReadInMatrix.class.getName(), null).log(Level.SEVERE, "File can't be opened", e);
            }
            for(int i = 0; i < rowTrack; ++i)
            {
                for(int j = 0; j < colTrack/rowTrack; ++j)
                {
                    if(input.hasNextDouble())
                    {
                        a[i][j] = input.nextDouble();
                    }
                }
            }
            input.close();
            Algorithm.startCalculate(a, rowTrack, colTrack/rowTrack);
    }
    
}
