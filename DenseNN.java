import java.util.*;

public class DenseNN {

    int inpSize;
    int outSize;
    int depthLayers;
    int[] widthLayers;
    Matrix[] weights;
    Matrix[] biases;
    private static double TOLERANCE = 0.0001;
    Matrix[] velocities;
    double beta = 0.9;
    String hiddenActivation = "relu";
    String outputActivation = "sigmoid";
    double lr;
    int numParameters;
    int numUpdates;

    public static double [][] xorX= {
        {0,0},
        {1,0},
        {0,1},
        {1,1}
    };
    public static double [][] xorY= {{0},{1},{1},{0}};

    public DenseNN(int[] hiddenLayers, double learningRate, String hiddenAct, String outputAct) {
        this.numUpdates = 0;
        this.hiddenActivation = hiddenAct;
        this.outputActivation = outputAct;
        this.widthLayers = hiddenLayers;
        this.depthLayers = hiddenLayers.length-1;
        this.velocities = new Matrix[this.depthLayers];
        this.lr = learningRate;
        this.inpSize = hiddenLayers[0];
        this.outSize = hiddenLayers[hiddenLayers.length-1];
        this.weights = new Matrix[this.depthLayers];
        this.biases = new Matrix[this.depthLayers];
        this.numParameters = 0;

        for(int i = 0; i < depthLayers; i++) {
            Matrix layerWeights = new Matrix(hiddenLayers[i+1], hiddenLayers[i]);
            Matrix layerBiases = new Matrix(hiddenLayers[i+1], 1);

            numParameters += (hiddenLayers[i+1] * hiddenLayers[i]);
            numParameters += hiddenLayers[i+1];

            this.weights[i] = layerWeights;
            this.biases[i] = layerBiases;
        }

    }

    public void setWeights(DenseNN other) {
        for(int i = 0; i < this.weights.length; i++) {
            this.weights[i] = other.weights[i];
        }
    }

    public Matrix predict(Matrix x) {
        if (!(x.rows == this.inpSize)) {
            System.out.println("input has invalid shape");
            return null;
        }

        for(int i = 0; i < this.depthLayers-1; i++) {
            x = Matrix.dot(this.weights[i], x);
            x.add(this.biases[i]);
            if (this.hiddenActivation.toLowerCase().equals("relu")) {
                x = Matrix.relu(x);
            } else if (this.hiddenActivation.toLowerCase().equals("tanh")) {
                x = Matrix.tanh(x);
            } else if (this.hiddenActivation.toLowerCase().equals("sigmoid")) {
                x = Matrix.sigmoid(x);
            }
            
        }
        x = Matrix.dot(this.weights[this.depthLayers-1], x);
        x.add(this.biases[this.depthLayers-1]);
        if (this.outputActivation.toLowerCase().equals("relu")) {
            x = Matrix.relu(x);
        } else if (this.outputActivation.toLowerCase().equals("tanh")) {
            x = Matrix.tanh(x);
        } else if (this.outputActivation.toLowerCase().equals("sigmoid")) {
            x = Matrix.sigmoid(x);
        }

        return x;
    }
    public Matrix predict(double[] k) {
        Matrix x = Matrix.fromArray(k);
        if (!(x.rows == this.inpSize)) {
            System.out.println("input has invalid shape");
            return null;
        }

        for(int i = 0; i < this.depthLayers-1; i++) {
            x = Matrix.dot(this.weights[i], x);
            x.add(this.biases[i]);
            if (this.hiddenActivation.toLowerCase().equals("relu")) {
                x = Matrix.relu(x);
            } else if (this.hiddenActivation.toLowerCase().equals("tanh")) {
                x = Matrix.tanh(x);
            } else if (this.hiddenActivation.toLowerCase().equals("sigmoid")) {
                x = Matrix.sigmoid(x);
            }
            
        }
        x = Matrix.dot(this.weights[this.depthLayers-1], x);
        x.add(this.biases[this.depthLayers-1]);
        if (this.outputActivation.toLowerCase().equals("relu")) {
            x = Matrix.relu(x);
        } else if (this.outputActivation.toLowerCase().equals("tanh")) {
            x = Matrix.tanh(x);
        } else if (this.outputActivation.toLowerCase().equals("sigmoid")) {
            x = Matrix.sigmoid(x);
        }

        return x;
    }
 
    

    public Matrix[] feedforward(Matrix x) {
        Matrix[] outputs = new Matrix[this.depthLayers+1];
        outputs[0] = x;

        for(int i = 0; i < this.depthLayers-1; i++) {
            x = Matrix.dot(this.weights[i], x);
            x.add(this.biases[i]);
            if (this.hiddenActivation.toLowerCase().equals("relu")) {
                x = Matrix.relu(x);
            } else if (this.hiddenActivation.toLowerCase().equals("tanh")) {
                x = Matrix.tanh(x);
            } else if (this.hiddenActivation.toLowerCase().equals("sigmoid")) {
                x = Matrix.sigmoid(x);
            }
            outputs[i+1] = x;
        }
        x = Matrix.dot(this.weights[this.depthLayers-1], x);
        x.add(this.biases[this.depthLayers-1]);
        if (this.outputActivation.toLowerCase().equals("relu")) {
            x = Matrix.relu(x);
        } else if (this.outputActivation.toLowerCase().equals("tanh")) {
            x = Matrix.tanh(x);
        } else if (this.outputActivation.toLowerCase().equals("sigmoid")) {
            x = Matrix.sigmoid(x);
        }
        outputs[this.depthLayers] = x;

        return outputs;
    }

    public Matrix[][] backprop(Matrix[] outputs, Matrix y) {

        int stepBackProp = this.depthLayers-1;

        Matrix[] weightsGrads = new Matrix[this.depthLayers];
        Matrix[] biasesGrads = new Matrix[this.depthLayers];

        Matrix y_pred = outputs[outputs.length-1];
        Matrix error = Matrix.subtract(y, y_pred);
        Matrix[] loss = {error.copy().abs()};

        Matrix d1;

        if (this.outputActivation.toLowerCase().equals("relu")) {
            d1 = Matrix.reluDer(y_pred);
        } else if (this.outputActivation.toLowerCase().equals("tanh")) {
            d1 = Matrix.tanhDer(y_pred);
        } else if (this.outputActivation.toLowerCase().equals("sigmoid")) {
            d1 = Matrix.sigmoidDer(y_pred);
        } else if(this.outputActivation.toLowerCase().equals("linear")) {
            d1 = Matrix.ones(y_pred.rows, y_pred.cols);
        } else {
            d1 = Matrix.ones(y_pred.rows, y_pred.cols);
        }

        error = Matrix.multiply(d1, error);

        biasesGrads[stepBackProp] = error;

        Matrix out_gradient = Matrix.dot(error, outputs[outputs.length-2].transpose());
        if (this.velocities[stepBackProp] == null) {
            this.velocities[stepBackProp] = Matrix.zeros(out_gradient.rows, out_gradient.cols);
        }
        this.velocities[stepBackProp] = Matrix.add(Matrix.multiply(this.velocities[stepBackProp], this.beta), Matrix.multiply(out_gradient, (1-this.beta)));

        weightsGrads[stepBackProp] = out_gradient;
        stepBackProp--;

        for(int layer = this.weights.length-2; layer >= 0; layer--) {
            Matrix d = outputs[layer+1];

            if (this.hiddenActivation.toLowerCase().equals("relu")) {
                d = Matrix.reluDer(d);
            } else if (this.hiddenActivation.toLowerCase().equals("tanh")) {
                d = Matrix.tanhDer(d);
            } else if (this.hiddenActivation.toLowerCase().equals("sigmoid")) {
                d = Matrix.sigmoidDer(d);
            } else if(this.hiddenActivation.toLowerCase().equals("linear")) {
                d = Matrix.ones(d.rows, d.cols);
            } else {
                d = Matrix.ones(d.rows, d.cols);
            }

            error = Matrix.dot(this.weights[layer+1].transpose(), error);
            error = Matrix.multiply(d, error);

            Matrix gradient = Matrix.dot(error, outputs[layer].transpose());
            if (this.velocities[stepBackProp] == null) {
                this.velocities[stepBackProp] = Matrix.zeros(gradient.rows, gradient.cols);
            }
            this.velocities[stepBackProp] = Matrix.add(Matrix.multiply(this.velocities[stepBackProp], this.beta), Matrix.multiply(gradient, (1-this.beta)));

            weightsGrads[stepBackProp] = gradient;
            biasesGrads[stepBackProp] = error;

            stepBackProp--;
        }
        return new Matrix[][] {weightsGrads, biasesGrads, loss};
    }

    public double update(Matrix x, Matrix y) {
        this.numUpdates++;
        Matrix[] outputs = feedforward(x);
        Matrix[][] gradients = backprop(outputs, y);

        Matrix[] weightGradients = gradients[0];
        Matrix[] biasGradients = gradients[1];
        Matrix loss = gradients[2][0];
        double lossVal = loss.sum();

        for(int i = 0; i < this.depthLayers; i++) {
            Matrix layerWeightGradients = weightGradients[i];
            layerWeightGradients.multiply(this.lr);

            Matrix layerBiasGradients = biasGradients[i];
            layerBiasGradients.multiply(this.lr);

            this.weights[i].add(layerWeightGradients);
            this.biases[i].add(layerBiasGradients);
        }

        return lossVal;
    }

    public double fit(double[][] X, double[][] Y, int epochs) {
        double sumLoss = 0;
        for(int i = 0; i < epochs; i++) {
            for(int j = 0; j < X.length; j++) {
                double l = this.update(Matrix.fromArray(X[j]), Matrix.fromArray(Y[j]));
                l /= (double)(X.length);
                sumLoss += l;
            }
            sumLoss /= (double)(X.length);
        }

        return sumLoss;
    }

    public double fit(List<double[]> X, List<double[]> Y, int epochs) {
        double sumLoss = 0;
        for(int i = 0; i < epochs; i++) {
            for(int j = 0; j < X.size(); j++) {
                double l = this.update(Matrix.fromArray(X.get(j)), Matrix.fromArray(Y.get(j)));
                sumLoss += l;
            }
            sumLoss /= (double)(X.size());
        }
        return sumLoss;
    }

    public static void main(String[] args) {
        DenseNN dnn = new DenseNN(new int[] {2,10,20,50,1}, 0.01, "relu", "sigmoid");
        dnn.fit(DenseNN.xorX, DenseNN.xorY, 500);
        Matrix prediction = dnn.predict(new Matrix(new double[][] {{0},{0}}));
        System.out.println(prediction.get(0, 0));
    }
    
}
