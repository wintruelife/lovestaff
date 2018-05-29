package love.wintrue.com.lovestaff.widget.circularcontactview;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

class ForceQueuePolicy implements RejectedExecutionHandler
  {
  @Override
  public void rejectedExecution(final Runnable r,final ThreadPoolExecutor executor)
    {
    try
      {
      executor.getQueue().put(r);
      }
    catch(final InterruptedException e)
      {
      // should never happen since we never wait
      throw new RejectedExecutionException(e);
      }
    }
  }
