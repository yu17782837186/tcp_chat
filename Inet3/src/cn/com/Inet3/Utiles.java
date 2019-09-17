package cn.com.Inet3;

import java.io.Closeable;

public class Utiles {
    //释放资源
    public static void close(Closeable...targets) {
        for (Closeable target:targets) {
            try {
                if (target != null) {
                    target.close();
                }
            }catch(Exception e) {

            }
        }
    }
}
