package love.wintrue.com.lovestaff.http;

import android.app.DownloadManager.Request;

public abstract class AbstractHttpResponseHandler<T> {
	/** Inform when start to handle this Request. */
	public void onStart() {
	}

	/** Inform when the {@link Request} is truly cancelled. */
	public void onCancel() {
	}

	/** Called when response success. */
	public abstract void onSuccess(T t);

	/**
	 * Callback method that an error has been occurred with the provided error
	 * code and optional user-readable message.
	 */
	public abstract void onFailure(String code,String error);

	/**
	 * Inform when {@link Request} execute is finish, whatever success or error
	 * or cancel, this callback method always invoke if request is done.
	 */
	public void onFinish() {
	}
}