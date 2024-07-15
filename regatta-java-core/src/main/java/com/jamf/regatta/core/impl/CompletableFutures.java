package com.jamf.regatta.core.impl;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

class CompletableFutures {

	static <T> CompletableFuture<T> from(ListenableFuture<T> future, Executor executor) {
		var completableFuture = new CompletableFuture<T>() {
			@Override
			public boolean cancel(boolean mayInterruptIfRunning) {
				var cancelOutcome = future.cancel(mayInterruptIfRunning);
				super.cancel(mayInterruptIfRunning);
				return cancelOutcome;
			}
		};
		Futures.addCallback(future, new FutureCallback<>() {
			@Override
			public void onSuccess(T result) {
				completableFuture.complete(result);
			}

			@Override
			public void onFailure(Throwable error) {
				completableFuture.completeExceptionally(error);
			}
		}, executor);
		return completableFuture;
	}
}
