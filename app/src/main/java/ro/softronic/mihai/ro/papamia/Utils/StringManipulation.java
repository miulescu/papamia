package ro.softronic.mihai.ro.papamia.Utils;

import java.util.Set;

public class StringManipulation {
    public static String getExceptLast4Chars(String str) {
        return str.substring(0, str.length()- 4);
    }
    public static String getExceptLast2Chars(String str) {
        return str.substring(0, str.length()- 2);
    }
    public static boolean isSetContainedInAnother(Set<String> s1, Set<String> s2){
        int k = 0;
        for(String object1 : s1){
            for(String object2: s2){
                if(object1.equals(object2)){
                    k++;
                    break;
                    //also do something
                }
            }
        }

        if (s1.size() == k) return true;
        return false;
    }

    public static boolean toBoolean( String target )
    {
        if( target == null ) return false;
        else if (target.equals("1"))  return true;
        else if (target.equals("0")) return false;
        return false;
    }
}