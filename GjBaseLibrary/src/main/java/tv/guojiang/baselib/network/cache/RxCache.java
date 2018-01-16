package tv.guojiang.baselib.network.cache;

import android.content.Context;
import io.reactivex.Observable;
import java.util.Map;
import tv.guojiang.baselib.network.annotation.Cache;
import tv.guojiang.baselib.network.request.BaseRequest;

/**
 * 接口缓存处理
 *
 * @author leo
 */
public class RxCache {

    private ICacheStore mStore;

    public RxCache(Context context) {
        mStore = new DiskLruCacheStore(context);
    }

    /**
     * 获取缓存的原始key。同一个接口地址GET/POST提交的参数相同时，key相同
     *
     * @param url 接口地址
     * @param params 当前接口的参数。
     */
    private static String getRealKey(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append(url);
        sb.append(params.toString());
        return sb.toString();
    }

    public static Cache getCacheAnnotation(Object obj) {
        return obj.getClass().getAnnotation(Cache.class);
    }

    /**
     * 获取缓存
     */
    public <T extends BaseRequest> Observable<String> getCache(String url, T request) {
        return Observable.create(e -> {
            String realKey = getRealKey(url, request.getParams());
            Cache cache = getCacheAnnotation(request);
            // 读取缓存
            String json = mStore.get(realKey, cache.time());
            // 缓存不为空时才触发下面的操作
            if (json != null) {
                e.onNext(json);
            }
            e.onComplete();
        });
    }

    /**
     * 缓存接口数据
     */
    private <T extends BaseRequest> void saveCache(String url, T request, String json) {
        String realKey = getRealKey(url, request.getParams());
        mStore.put(realKey, json);
    }

}
