package gauss.jordan.elimination;

// Imports
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

/**
 * Performs the Gauss Jordan Elimination on the given matrix.
 */
public class Algorithm {
    
    private static final double EPSILON = 1e-8;

    private final int N;      // N-by-N system
    private double[][] a;     // N-by-N+1 augmented matrix
    
    public static StringBuilder result;
    private static boolean infiniteSolutions = false;
    private static int columnsGiven = 0;
    private static int rowsGiven = 0;
    public Algorithm(double[][] A, double[] b) {
        N = b.length;

        a = new double[N][N+N+1];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                a[i][j] = A[i][j];

        for (int i = 0; i < N; i++)
            a[i][N+i] = 1.0;

        for (int i = 0; i < N; i++) a[i][N+N] = b[i];

        solve();

        assert check(A, b);
    }

    private void solve() {
        for (int p = 0; p < N; p++) {
            int max = p;
            for (int i = p+1; i < N; i++) {
                if (Math.abs(a[i][p]) > Math.abs(a[max][p])) {
                    max = i;
                }
            }

            swap(p, max);
            if (Math.abs(a[p][p]) <= EPSILON) {
                continue;
                // throw new RuntimeException("Matrix is singular or nearly singular");
            }

            pivot(p, p);
        }
    }

    private void swap(int row1, int row2) {
        double[] temp = a[row1];
        a[row1] = a[row2];
        a[row2] = temp;
    }

    private void pivot(int p, int q) {

        for (int i = 0; i < N; i++) {
            double alpha = a[i][q] / a[p][q];
            for (int j = 0; j <= N+N; j++) {
                if (i != p && j != q) a[i][j] -= alpha * a[p][j];
            }
        }

        for (int i = 0; i < N; i++)
            if (i != p) a[i][q] = 0.0;

        for (int j = 0; j <= N+N; j++)
            if (j != q) a[p][j] /= a[p][q];
        a[p][q] = 1.0;
    }

    public double[] primal() {
        double[] x = new double[N];
        for (int i = 0; i < N; i++) {
            if (Math.abs(a[i][i]) > EPSILON)
                x[i] = a[i][N+N] / a[i][i];
            else if (Math.abs(a[i][N+N]) > EPSILON)
                return null;
        }
        return x;
    }

    public double[] dual() {
        double[] y = new double[N];
        for (int i = 0; i < N; i++) {
            if ( (Math.abs(a[i][i]) <= EPSILON) && (Math.abs(a[i][N+N]) > EPSILON) ) {
                for (int j = 0; j < N; j++)
                    y[j] = a[i][N+j];
                return y;
            }
        }
        return null;
    }

    public boolean isFeasible() {
        return primal() != null;
    }
    
    private void show() {
        infiniteSolutions = false;
        double[] temp = new double[columnsGiven+2];
        int M = N;
        if(columnsGiven == rowsGiven)
            M--;
        for (int i = 0; i < N; i++) {
            result.append("|");
            for (int j = 0; j < M; j++) {
                result.append(String.format(" %8.3f ", a[i][j]));
                temp[j] = a[i][j];
            }
            result.append(String.format("| %8.3f |\n", a[i][N+N]));
            temp[columnsGiven] = a[i][N+N];
            if(infiniteSolutions == false)
                infiniteSolutions = testInfinite(temp);
        }
        result.append(String.format("\n\n"));
    }
    
    private boolean check(double[][] A, double[] b) {

        if (isFeasible()) {
            double[] x = primal();
            for (int i = 0; i < N; i++) {
                double sum = 0.0;
                for (int j = 0; j < N; j++) {
                     sum += A[i][j] * x[j];
                }
                if (Math.abs(sum - b[i]) > EPSILON) {
                    result.append(String.format("Not Feasible\n"));
                    result.append(String.format("b[%d] = %8.3f, sum = %8.3f\n\n", i, b[i], sum));
                    return false;
                }
            }
            return true;
        }
        else {
            double[] y = dual();
            for (int j = 0; j < N; j++) {
                double sum = 0.0;
                for (int i = 0; i < N; i++) {
                     sum += A[i][j] * y[i];
                }
                if (Math.abs(sum) > EPSILON) {
                    result.append(String.format("Invalid Certificate of Infeasibility\n\n"));
                    result.append(String.format("sum = %8.3f\n", sum));
                    return false;
                }
            }
            double sum = 0.0;
            for (int i = 0; i < N; i++) {
                sum += y[i] * b[i];
            }
            if (Math.abs(sum) < EPSILON) {
                result.append(String.format("Invalid Certificate of Infeasibility\n\n"));
                result.append(String.format("yb  = %8.3f\n", sum));
                return false;
            }
            return true;
        }
    }

    public static void test(double[][] A, double[] b) {
        Algorithm gaussian = new Algorithm(A, b);
        result.append("Final Form:\n\n");
        gaussian.show();
        if (gaussian.isFeasible()) {
            double[] x = gaussian.primal();
            if(infiniteSolutions)
                result.append(String.format("Infinitely Many Solutions to Ax = b\n\n"));
            else{
                result.append(String.format("Unique Solution to Ax = b\n\n"));              
                for (int i = 0; i < x.length; i++) {
                    result.append(String.format("%10.6f\n", x[i]));
                }
            }
            result.append(String.format("\n\n"));
        }
        else {
            result.append(String.format("No Solution to Ax = b\n\n"));
//            double[] y = gaussian.dual();
//            for (int j = 0; j < y.length; j++) {
//                result.append(String.format("%10.6f\n", y[j]));
//            }
        }        
    }
    
    private static boolean testInfinite(double[] x){
        double sum = 0.0;
        for(double temp : x){
            sum += Math.abs(temp);
        }
        if(sum == 0.0)
            return true;
        else
            return false;
    }
    
    /**
     * @author Sai Kiran Vadlamudi
     * 
     * Converts given TableView to a matrix to be solved by the Algorithm.
     * 
     * @param matrix
     * @param rows
     * @param columns 
     */
    public static void startCalculate(TableView<String[]> matrix, int rows, int columns){
        double[][] matrixArray = findMatrix(matrix, rows, columns);
        double[] matrixConstant = findConstants(matrix, rows, columns);
        result = new StringBuilder();
        columnsGiven = columns;
        rowsGiven = rows;
        result.append("\nInitial Matrix:\n\n");        
        for(int i = 0; i < rows; i++){
            result.append("|");
            for(int j = 0; j < columns; j++){
                if(j == columns-1)
                    result.append(String.format(" | %10.6f ", matrixConstant[i]));
                else
                    result.append(String.format(" %10.6f ", matrixArray[i][j]));
            }
            result.append(" |\n");
        }
        result.append("\n\n");
        test(matrixArray, matrixConstant);
        StringBuilder temp = new StringBuilder();
        temp.append(MainController.resultsField.getText());
        temp.append("-------------------------------------------------Manual--------------------------------------------------");
        temp.append(result);
        MainController.resultsField.setText(temp.toString());
        MainController.resultsField.setScrollTop(Double.MIN_VALUE);
        MainController.resultsField.appendText("");
    }
    
    /**
     * @author Sai Kiran Vadlamudi
     * 
     * Returns the variables in the given TableView as an array, matrix.
     * 
     * @param matrixTable
     * @param rows
     * @param columns
     * @return double[][]
     */
    public static double[][] findMatrix(TableView<String[]> matrixTable, int rows, int columns){
        ObservableList<String[]> tempList = matrixTable.getItems();
        double[][] matrixArray = new double[rows][columns-1];
        int j = 0;
        if(rows == columns)
            matrixArray = new double[rows][columns];
        
        for(int i = 0; i < rows; i++){
            for(String node : tempList.get(i)){
                try{
                    if(j+1 == columns){ 
                        if(rows == columns){
                            matrixArray[i][j] = 0;
                        }else
                            continue;
                    }
                    else{
                        matrixArray[i][j] = Double.parseDouble(node);
                    }
                }catch(NumberFormatException e){
                    if(j+1 == columns){ 
                        matrixArray[i][j] = 0;
                        continue;
                    }else
                        matrixArray[i][j] = 0;
                }
                j++;
            }
            j = 0;
    	}
        return matrixArray;
    }
    
    /**
     * @author Sai Kiran Vadlamudi
     * 
     * Return the constants in the given TableView as an array.
     * 
     * @param matrixTable
     * @param rows
     * @param columns
     * @return double[]
     */
    public static double[] findConstants(TableView<String[]> matrixTable, int rows, int columns){
        ObservableList<String[]> tempList = matrixTable.getItems();
        double[] constantArray = new double[rows];
        int j = 0;
        
        for(int i = 0; i < rows; i++){
            for(String node : tempList.get(i)){
                try{
                    if(j+1 == columns){ 
                        constantArray[i] = Double.parseDouble(node);
                    }
                }catch(NumberFormatException e){
                    if(j+1 == columns) 
                        constantArray[i] = 0;
                }
                j++;
            }
            j = 0;
    	}
        
        return constantArray;
    }
    
    /**
     * @author Sai Kiran Vadlamudi
     * 
     * Solves the given matrix using the Algorithm
     * 
     * @param matrix
     * @param rows
     * @param columns 
     */
    public static void startCalculate(double[][] matrix, int rows, int columns){
        double[][] matrixArray = new double[rows][columns-1];
        double[] matrixConstant = new double[rows];
        columnsGiven = columns;
        rowsGiven = rows;
        result = new StringBuilder();        
        result.append("\nInitial Matrix:\n\n");
        
        for(int i = 0; i < rows; i++){
            result.append("|");
            for(int j = 0; j < columns; j++){
                if(j == columns-1){
                    matrixConstant[i] = matrix[i][j];
                    result.append(String.format(" | %10.6f ", matrixConstant[i]));
                }else{
                    matrixArray[i][j] = matrix[i][j];
                    result.append(String.format(" %10.6f ", matrixArray[i][j]));
                }
            }
            result.append(" |\n");
        }
        
        result.append("\n\n");
        test(matrixArray, matrixConstant);
        StringBuilder temp = new StringBuilder();
        temp.append(MainController.resultsField.getText());
        temp.append("----------------------------------------------------File----------------------------------------------------");
        temp.append(result);
        MainController.resultsField.setText(temp.toString());
        MainController.resultsField.setScrollTop(Double.MIN_VALUE);
        MainController.resultsField.appendText("");
    }
}