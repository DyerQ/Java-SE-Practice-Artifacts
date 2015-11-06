package ru.ncedu.bestgroup.mailing.utils;

import ru.ncedu.bestgroup.mailing.parsing.BusinessCardParser;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by Илья on 06.11.2015.
 */
public  class BusinessCardParserRunnable implements Runnable {
    private final ScheduledThreadPoolExecutor executor;

    public BusinessCardParserRunnable(ScheduledThreadPoolExecutor executor) {
        this.executor = executor;
    }

    @Override
    public  void run() {
        Future<Integer> businessCardsFuture = executor.submit(new BusinessCardParser("./", "recipients.xml"));
        if (businessCardsFuture.isDone()) {
            int parsedCount = 0;
            try {
                parsedCount = businessCardsFuture.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (parsedCount != 0) {
                System.out.println("Parsed ...");
            }

        }
    }
}
