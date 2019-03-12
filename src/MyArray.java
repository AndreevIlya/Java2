public class MyArray {
    private static final int SIZE = 16777216;
    private float[] arr = new float[SIZE];

    private void fillArray(){
        for (int i = 0; i < SIZE;i++) {
            arr[i] = 1;
        }
    }

    private float[] calcFull(){
        long initCounter = System.currentTimeMillis();
        System.out.println("Started.");
        fillArray();
        for (int i = 0;i < SIZE;i++) {
            if(i % 1048576 == 0) System.out.println(i / 1048576);
            arr[i] = (float)(arr[i] * Math.sin(0.2f + (double)i / 5) * Math.cos(0.2f + (double)i / 5) * Math.cos(0.4f + (double)i / 2));
        }
        System.out.println(((System.currentTimeMillis() - initCounter) / 1000.0d) + " seconds.");
        return arr;
    }

    private float[] calcHalf(){
        long initCounter = System.currentTimeMillis();
        System.out.println("Started by half.");
        fillArray();
        divideThread(2);
        System.out.println(((System.currentTimeMillis() - initCounter) / 1000.0d) + " seconds.");
        return arr;
    }

    private float[] calcQuarter(){
        long initCounter = System.currentTimeMillis();
        System.out.println("Started by quarter.");
        fillArray();
        divideThread(4);
        System.out.println(((System.currentTimeMillis() - initCounter) / 1000.0d) + " seconds.");
        return arr;
    }

    private float[] calcEighth(){
        long initCounter = System.currentTimeMillis();
        System.out.println("Started by 8.");
        fillArray();
        divideThread(8);
        System.out.println(((System.currentTimeMillis() - initCounter) / 1000.0d) + " seconds.");
        return arr;
    }

    private void divideThread(int threadsNumber){
        ArrayComputer[] arrayComps = new ArrayComputer[threadsNumber];
        Thread[] threads = new Thread[threadsNumber];
        for(int i = 0;i < threadsNumber; i++){
            arrayComps[i] = new ArrayComputer(i + 1,i * SIZE / threadsNumber,SIZE / threadsNumber);
            threads[i] = new Thread(arrayComps[i]);
            threads[i].start();
        }
        try {
            for(int i = 0;i < threadsNumber; i++){
                threads[i].join();
            }
        }catch (InterruptedException e){
            e.getStackTrace();
        }
        for(int i = 0;i < threadsNumber; i++){
            System.arraycopy(arrayComps[i].arrComp,0,arr,i * SIZE / threadsNumber,SIZE / threadsNumber);
        }
    }

    private class ArrayComputer implements Runnable{
        float[] arrComp;
        int threadNumber;
        int begin;
        int step;

        ArrayComputer(int threadNumber, int begin, int step){
            this.threadNumber = threadNumber;
            this.begin = begin;
            this.step = step;
            this.arrComp = new float[step];
        }

        private float[] compute(int threadNumber, int begin, int step){
            float[] arrNew = new float[step];
            System.arraycopy(arr, begin, arrNew, 0, step);
            for (int i = begin;i < begin + step;i++) {
                if(i % 1048576 == 0) System.out.println("Thread " + threadNumber + " " + (i / 1048576));
                arrNew[i - begin] = (float)(arrNew[i - begin] * Math.sin(0.2f + (double)i / 5) * Math.cos(0.2f + (double)i / 5) * Math.cos(0.4f + (double)i / 2));
            }
            return arrNew;
        }

        @Override
        public void run() {
            arrComp = compute(threadNumber,begin,step);
        }
    }

    public static void main(String[] args){
        MyArray array1 = new MyArray();
        MyArray array2 = new MyArray();
        MyArray array3 = new MyArray();
        MyArray array4 = new MyArray();
        float[] arr1 = array1.calcFull();
        float[] arr2 = array2.calcHalf();
        float[] arr3 = array3.calcQuarter();
        float[] arr4 = array4.calcEighth();
        for (int i = 0; i < SIZE;i++) {//To check if result is the same.
            if(arr1[i] != arr2[i]) System.out.println(i);
            if(arr1[i] != arr3[i]) System.out.println(i);
            if(arr1[i] != arr4[i]) System.out.println(i);
        }
    }
}
