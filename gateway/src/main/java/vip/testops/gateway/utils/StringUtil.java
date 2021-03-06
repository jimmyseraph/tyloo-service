package vip.testops.gateway.utils;

public class StringUtil {

    public static boolean isEmptyOrNull(String content){
        if(content == null){
            return true;
        }else {
            return content.trim().equals("");
        }
    }

    public static String lpadding(String content, int len, char pad){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < len - content.length(); i++){
            sb.append(pad);
        }
        sb.append(content);
        return sb.toString();
    }
}
