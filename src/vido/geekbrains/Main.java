package vido.geekbrains;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        //работает для любого количества потоков, если количество потоков не привышает размер массива
        int size = 10000000;
        int numberOfThreads = 2;

        singleThreaded(size);
        multiThreaded(size, numberOfThreads);
    }

    public static void singleThreaded(int size){
        float[] arr = new float[size];

        for (int i = 0; i < arr.length; i++) {
            arr[i] = 1;
        }

        long a = System.currentTimeMillis();

        for (int i = 0; i < arr.length; i++) {
            arr[i] = (float) (arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }

        System.out.println("Однопоточный метод: " + (System.currentTimeMillis() - a));
    }

    public static void multiThreaded(int size, int numberOfThreads){

        if (size < numberOfThreads) {
            System.out.println("Размер массива не должен привышать количество потоков!");
            return;
        }

        int step = size/numberOfThreads;
        float[] arr = new float[size];

        for (int i = 0; i < arr.length; i++) {
            arr[i] = 1;
        }

        long a = System.currentTimeMillis();

        ArrayList<float[]> listMas = new ArrayList<>();

        for (int i = 0; i < numberOfThreads; i++) {
            float[] a1;
            if (i < numberOfThreads-1) {
                a1 = new float[step];
            } else {
                a1 = new float[size-step*i];
            }
            System.arraycopy(arr, i*step, a1, 0, a1.length);
            listMas.add(a1);
        }

        ArrayList<Thread> listThread = new ArrayList<>();

        for (float[] mas: listMas) {
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < mas.length; i++) {
                        mas[i] = (float) (mas[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
                    }
                }
            });
            t1.start();

            listThread.add(t1);
        }

        try {
            for (Thread thread: listThread) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < listMas.size(); i++) {
            System.arraycopy(listMas.get(i), 0, arr, i*step, listMas.get(i).length);
        }

        System.out.println("Многопоточный метод: " + (System.currentTimeMillis() - a));
    }
}
