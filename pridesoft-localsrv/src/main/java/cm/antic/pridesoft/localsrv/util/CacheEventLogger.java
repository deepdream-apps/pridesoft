package cm.antic.pridesoft.localsrv.util;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class CacheEventLogger implements CacheEventListener<Object, Object> {


  @Override
  public void onEvent(CacheEvent<? extends Object, ? extends Object> cacheEvent) {
      log.info(String.format("%s %s %s %s", cacheEvent.getType().toString(), cacheEvent.getKey(), cacheEvent.getOldValue(), cacheEvent.getNewValue()));
  }
}