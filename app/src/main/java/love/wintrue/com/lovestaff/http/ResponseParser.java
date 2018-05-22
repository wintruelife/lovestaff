package love.wintrue.com.lovestaff.http;

public interface ResponseParser<T> {
	
	T parse(String data);

}
