package com.e4developer.webfluxservice;

import reactor.core.publisher.FluxSink;

public class SlowCounter {

    private SlowCounter(){}

    static void count(FluxSink<Integer> sink, int number) {
        SlowCounterRunnable runnable = new SlowCounterRunnable(sink, number);
        Thread t = new Thread(runnable);
        t.start();
    }

    public static class SlowCounterRunnable implements Runnable {

        FluxSink<Integer> sink;
        int number;

        public SlowCounterRunnable(FluxSink<Integer> sink, int number) {
            this.sink = sink;
            this.number = number;
        }

        public void run() {
            int count = 0;
            while (count < number) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sink.next(count);
                count++;
            }
            //Only on complete() is the result sent to the browser
            sink.complete();
        }
    }
}