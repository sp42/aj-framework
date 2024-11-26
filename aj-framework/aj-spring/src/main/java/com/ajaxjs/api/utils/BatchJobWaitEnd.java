package com.ajaxjs.api.utils;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

@AllArgsConstructor
@NoArgsConstructor
public class BatchJobWaitEnd {
    private Executor executor;

    @SafeVarargs
    public final void batch(Supplier<Boolean>... tasks) {
        if (executor == null)
            executor = Executors.newFixedThreadPool(tasks.length); // 10个线程

        List<CompletableFuture<Boolean>> list = new ArrayList<>();

        for (Supplier<Boolean> task : tasks) {
            CompletableFuture<Boolean> cf = CompletableFuture.supplyAsync(task, executor);
            list.add(cf);
        }

        list.forEach(CompletableFuture::join);

        list.forEach(cf -> {
            try {
                cf.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

    }
}
