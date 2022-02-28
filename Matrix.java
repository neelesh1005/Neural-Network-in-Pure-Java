import java.util.*;

public class Matrix {
    double[][] data;
    int rows, cols;

    public Matrix(double[][] d) {
        this.data = d;
        this.rows = d.length;
        this.cols = d[0].length;
    }

    public Matrix(int r, int c) {
        this.rows = r;
        this.cols = c;
        this.data = new double[this.rows][this.cols];

        //He Uniform initialization
        double max = Math.sqrt((double)(6.0/rows));
        double min = -max;
        for(int i = 0; i < this.rows; i++) {
            for(int j = 0; j < this.cols; j++) {
                this.data[i][j] = Math.random()*(max-min)+min;
            }
        }
    }

    public String shape() {
        return "("+this.rows+", "+this.cols+")";
    }

    public double get(int r, int c) {
        return this.data[r][c];
    }

    public Matrix copy() {
        Matrix matrix = new Matrix(this.data);
        return matrix;
    }
    public void add(Matrix matrix) {
        if(this.rows % matrix.rows != 0 || this.cols != matrix.cols) {
            System.out.println("Shape (" + this.rows + ", " + this.cols + ") is not compatible with shape (" + matrix.rows + ", " + matrix.cols+")");
            return;
        } else {
            if (this.rows % matrix.rows == 0) {
                int numBroadCast = this.rows / matrix.rows;
                for(int k = 0; k < numBroadCast; k++) {
                    for(int i = 0; i < matrix.rows; i++) {
                        int f = i + (k*matrix.rows);
                        for(int j = 0; j < matrix.cols; j++) {
                           this.data[f][j] = (this.data[f][j] + matrix.data[i][j]);
                        }
                    }
                }
            }
        }
    }

    public static Matrix add(Matrix matrix, double scale) {
        Matrix newMatrix = matrix.copy();
        for(int i = 0; i < matrix.rows; i++) {
            for(int j = 0; j < matrix.cols; j++) {
                newMatrix.data[i][j] += scale;
            }
        }
        return newMatrix;
    }
    public static Matrix add(Matrix matrix1, Matrix matrix2) {
        if(matrix1.rows != matrix2.rows || matrix1.cols != matrix2.cols) {
            System.out.println("Shape (" + matrix1.rows + ", " + matrix1.cols + ") is not compatible with shape (" + matrix2.rows + ", " + matrix2.cols+")");
            return null;
        } else {
            Matrix newMatrix = new Matrix(matrix1.rows, matrix1.cols);
            for(int i = 0; i < matrix2.rows; i++) {
                for(int j = 0; j < matrix2.cols; j++) {
                     newMatrix.data[i][j] = (matrix1.data[i][j] + matrix2.data[i][j]);
                    }
                }
            return newMatrix;
            }
    }
        
    
    public static Matrix subtract(Matrix matrix1, Matrix matrix2) {
        Matrix newMatrix = new Matrix(matrix1.rows, matrix1.cols);
        for(int i = 0; i < matrix2.rows; i++) {
            for(int j = 0; j < matrix2.cols; j++) {
                newMatrix.data[i][j] = (matrix1.data[i][j] - matrix2.data[i][j]);
            }
        }
        return newMatrix;
    }
    public void subtract(Matrix matrix1) {
        for(int i = 0; i < this.rows; i++) {
            for(int j = 0; j < this.cols; j++) {
                this.data[i][j] -= matrix1.data[i][j];
            }
        }
    }

    public static Matrix multiply(Matrix matrix, double scale) {
        Matrix newMatrix = matrix.copy();
        for(int i = 0; i < matrix.rows; i++) {
            for(int j = 0; j < matrix.cols; j++) {
                newMatrix.data[i][j] *= scale;
            }
        }
        return newMatrix;
    }
    public void multiply(Matrix a) {
        for(int i=0;i<a.rows;i++)
        {
            for(int j=0;j<a.cols;j++)
            {
                this.data[i][j]*=a.data[i][j];
            }
        }
        
    }

    public void divide(double scale) {
        for(int i=0;i<this.rows;i++)
        {
            for(int j=0;j<this.cols;j++)
            {
                this.data[i][j] /= scale;
            }
        }
        
    }

    public double max() {
        double max = Double.NEGATIVE_INFINITY;
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                if (this.data[i][j] > max) {
                    max = this.data[i][j];
                }
            }
        }
        return max;
    }
    public void multiply(double a) {
        for(int i=0;i<rows;i++)
        {
            for(int j=0;j<cols;j++)
            {
                this.data[i][j]*=a;
            }
        }
        
    }

    public static Matrix multiply(Matrix matrix1, Matrix matrix2) {
        if(matrix1.rows % matrix2.rows != 0 || matrix1.cols != matrix2.cols) {
            System.out.println("Shape (" + matrix1.rows + ", " + matrix1.cols + ") is not compatible with shape (" + matrix2.rows + ", " + matrix2.cols+")");
            return null;
        } else {
            Matrix newMatrix = new Matrix(matrix1.rows, matrix1.cols);
            if (matrix1.rows % matrix2.rows == 0) {
                int numBroadCast = matrix1.rows / matrix2.rows;
                for(int k = 0; k < numBroadCast; k++) {
                    for(int i = 0; i < matrix2.rows; i++) {

                        int f = i + (k*matrix2.rows);
                        for(int j = 0; j < matrix2.cols; j++) {

                           newMatrix.data[f][j] = (matrix1.data[f][j] * matrix2.data[i][j]);
                        }
                    }
                }
            }
            return newMatrix;
        }
    }
    public static Matrix dot(Matrix matrix1, Matrix matrix2) {
        if (matrix1.cols != matrix2.rows) {
            System.out.println("Shape (" + matrix1.rows + ", " + matrix1.cols + ") is not compatible with shape (" + matrix2.rows + ", " + matrix2.cols+")");
        }
        Matrix newMatrix = new Matrix(matrix1.rows, matrix2.cols);
        for(int i = 0; i < newMatrix.rows; i++) {
            for(int j = 0; j < newMatrix.cols; j++){
                double sum = 0;
                for(int k = 0;k<matrix1.cols;k++){
                    sum += matrix1.data[i][k]*matrix2.data[k][j];
                }
                newMatrix.data[i][j] = sum;
            }
        }
        return newMatrix;
    }

    public static Matrix relu(Matrix matrix) {
        Matrix newMatrix = matrix.copy();
        for(int i = 0; i < matrix.rows; i++) {
            for(int j = 0; j < matrix.cols; j++) {
                newMatrix.data[i][j] = Math.max(0.0, matrix.data[i][j]);
            }
        }
        return newMatrix;
    }
    public static Matrix sigmoid(Matrix matrix) {
        Matrix newMatrix = matrix.copy();
        for(int i = 0; i < matrix.rows; i++) {
            for(int j = 0; j < matrix.cols; j++) {
                newMatrix.data[i][j] = 1/(1+Math.exp(-matrix.data[i][j]));
            }
        }
        return newMatrix;
    }

    public static Matrix ones(int rows, int cols) {
        Matrix newMatrix = new Matrix(rows, cols);
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                newMatrix.data[i][j] = 1.0;
            }
        }
        return newMatrix;
    }
    public static Matrix zeros(int rows, int cols) {
        Matrix newMatrix = new Matrix(rows, cols);
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                newMatrix.data[i][j] = 0.0;
            }
        }
        return newMatrix;
    }

    public static Matrix sigmoidDer(Matrix matrix) {
        Matrix newMatrix = matrix.copy();
        for(int i = 0; i < matrix.rows; i++) {
            for(int j = 0; j < matrix.cols; j++) {
                newMatrix.data[i][j] = matrix.data[i][j] * (1-matrix.data[i][j]);
            }
        }
        return newMatrix;
    }

    public static Matrix tanh(Matrix matrix) {
        Matrix newMatrix = matrix.copy();
        for(int i = 0; i < matrix.rows; i++) {
            for(int j = 0; j < matrix.cols; j++) {
                newMatrix.data[i][j] = Math.tanh(matrix.data[i][j]);
            }
        }
        return newMatrix;
    }
    public static Matrix tanhDer(Matrix matrix) {
        Matrix newMatrix = matrix.copy();
        for(int i = 0; i < matrix.rows; i++) {
            for(int j = 0; j < matrix.cols; j++) {
                newMatrix.data[i][j] = 1.0 - Math.pow(matrix.data[i][j], 2);
            }
        }
        return newMatrix;
    }

    public static Matrix reluDer(Matrix matrix) {
        Matrix newMatrix = matrix.copy();
        for(int i = 0; i < matrix.rows; i++) {
            for(int j = 0; j < matrix.cols; j++) {
                if (matrix.data[i][j] > 0) {
                    newMatrix.data[i][j] = 1.0;
                } else {
                    newMatrix.data[i][j] = 0.0;
                }
            }
        }
        return newMatrix;
    }

    public Matrix transpose() {
        Matrix t = new Matrix(this.cols, this.rows);
        for(int i = 0; i < this.rows; i++) {
            for(int j = 0; j < this.cols; j++) {
                t.data[j][i] = this.data[i][j];
            }
        }
        return t;
    }

    public static Matrix transpose(Matrix a) {
        Matrix temp=new Matrix(a.cols,a.rows);
        for(int i=0;i<a.rows;i++)
        {
            for(int j=0;j<a.cols;j++)
            {
                temp.data[j][i]=a.data[i][j];
            }
        }
        return temp;
    }

    public Matrix abs() {
        Matrix newMatrix = new Matrix(this.rows, this.cols);
        for(int i = 0; i < this.rows; i++) {
            for(int j = 0; j < this.cols; j++) {
                newMatrix.data[i][j] = Math.abs(this.data[i][j]);
            }
        }
        return newMatrix;
    }

    public double sum() {
        double s = 0.0;
        for(int i = 0; i < this.rows; i++) {
            for(int j = 0; j < this.cols; j++) {
                s += this.data[i][j];
            }
        }
        return s;
    }

    public static Matrix fromArray(double[]x)
    {
        Matrix temp = new Matrix(x.length,1);
        for(int i =0;i<x.length;i++)
            temp.data[i][0]=x[i];
        return temp;
        
    }

    public static double[] toArray(Matrix x) {
        double[] temp = new double[x.rows];
        for(int i = 0; i < x.rows; i++) {
            temp[i] = x.data[i][0];
        }
        return temp;
    }

    public String toString() {
        String str = "{";
        for(int i = 0; i < this.rows; i++) {
            str += "{";
            for(int j = 0; j < this.cols; j++) {
                if (j != this.cols-1) {
                    str += this.data[i][j] + " ,";
                } else {
                    str += this.data[i][j];
                }
            }
            str += "}";
        }
        str += "}";
        return str;
    }

    public static void main(String[] args) {
        Matrix mat1 = new Matrix(new double[][]{{1.0,1.0,1.0,1.0}, {1.0,1.0,1.0,1.0}});
        Matrix mat2 = new Matrix(2, 4);
        Matrix multipled = Matrix.multiply(mat1, mat2);
        for(int i = 0; i < multipled.rows; i++) {
            System.out.print("[");
            for(int j = 0; j < multipled.cols; j++) {
                System.out.print(multipled.data[i][j] + ", ");
            }
            System.out.println("]");
        }

    }
}
