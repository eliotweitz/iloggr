/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.gwt.util.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.iloggr.gwt.util.functional.Closure;
import com.iloggr.gwt.util.lang.Objects;

/**
 * Creates {@link AsyncCallback callbacks} that handle failures with a configured {@link ErrorManager}.
 */
public class AsyncCallbackFactory {

	public interface ErrorManager {
		void onError(Throwable caught);
	}

	private final ErrorManager errorManager;

	@Inject
	public AsyncCallbackFactory(ErrorManager errorManager) {
		this.errorManager = errorManager;
	}

	private class PrivateException extends Exception {
		private static final long serialVersionUID = 1L;

		// guaranteed not to match the type of any thrown exception
	}

	/**
	 * Creates an {@code AsyncCallback} that handles failures with a configured {@link ErrorManager} and delegates
	 * {@link AsyncCallback#onSuccess(Object)} to the supplied {@code callback}.
	 */
	public <T> AsyncCallback<T> createAsyncCallback(final Closure<T> callback) {
		return createAsyncCallback(PrivateException.class, new AsyncHandler<T, PrivateException>(){
			public void execute(T item) {
				callback.execute(item);
			}

			public void onError(PrivateException exception) {
				throw new IllegalStateException("impossible: should never be catching a remote PrivateException");
			}
		});
	}

	public interface AsyncHandler<T, E extends Exception> extends Closure<T> {
		void onError(E exception);
	}

	/**
	 * Creates an {@code AsyncCallback} that delegates {@link AsyncCallback#onSuccess(Object)} to the supplied
	 * {@code handler}.  Failures that match the exception type {@code E} are delegated to the {@code handler} and all
	 * other exceptions are handed off to the configured {@link ErrorManager}.
	 */
	public <T, E extends Exception> AsyncCallback<T> createAsyncCallback(final Class<E> errorType,
			final AsyncHandler<T, E> handler) {

		return new AsyncCallback<T>(){
			public void onSuccess(T result) {
				handler.execute(result);
			}

			public void onFailure(Throwable caught) {
				if (Objects.isInstance(errorType, caught)) {
					@SuppressWarnings("unchecked")
					E error = (E) caught;
					handler.onError(error);
				} else {
					errorManager.onError(caught);
				}
			}
		};
	}

}
