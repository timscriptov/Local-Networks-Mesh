package org.servalproject.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;

public class Utils {
    // Метод для проверки: установлен ли апк
    @SuppressLint("WrongConstant")
    public static boolean isInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 1);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
