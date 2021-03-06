package tv.guojiang.core.network.response;

import com.google.gson.annotations.SerializedName;

/**
 * // {"errno":0, "msg":"", "data":{}}
 * // {"errno":0, "msg":"", "data":[]}
 *
 * @author leo
 */
public class Response {

    @SerializedName("errno")
    public int code;

    @SerializedName("msg")
    public String msg;

}
