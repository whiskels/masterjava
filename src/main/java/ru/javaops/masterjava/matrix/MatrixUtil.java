package ru.javaops.masterjava.matrix;

import ru.javaops.masterjava.service.MailService;

import java.util.Random;
import java.util.concurrent.*;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        ExecutorService service = Executors.newFixedThreadPool(MainMatrix.THREAD_NUMBER);
        int q = matrixSize / MainMatrix.THREAD_NUMBER;

        for (int j = 0; j < matrixSize; j = j + q) {
            int start = j;
            int end = start + q;
            service.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        int thatColumn[] = new int[matrixSize];

                        for (int j = start; j < end; j++) {
                            calculate(matrixA, matrixB, matrixSize, matrixC, thatColumn, j);
                        }
                    } catch (Exception e) {
                        //NOP
                    }
                }
            });
        }
        service.shutdown();
        try {
            service.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return matrixC;
    }

    // https://habrahabr.ru/post/114797/
    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        int thatColumn[] = new int[matrixSize];

        for (int j = 0; j < matrixSize; j++) {
            calculate(matrixA, matrixB, matrixSize, matrixC, thatColumn, j);
        }

        return matrixC;
    }

    private static void calculate(int[][] matrixA, int[][] matrixB, int matrixSize, int[][] matrixC, int[] thatColumn, int j) {
        for (int k = 0; k < matrixSize; k++) {
            thatColumn[k] = matrixB[k][j];
        }
        for (int i = 0; i < matrixSize; i++) {
            int thisRow[] = matrixA[i];
            int sum = 0;
            for (int k = 0; k < matrixSize; k++) {
                sum += thisRow[k] * thatColumn[k];
            }
            matrixC[i][j] = sum;
        }
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
