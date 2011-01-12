package org.isolution.androdof;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * User: agwibowo
 * Date: 12/01/11
 * Time: 10:51 PM
 */
public class ApplicationHelper {

     public static String getDeviceDetails(Context context) {
        StringBuilder builder = new StringBuilder();
        builder.append("Andro DOF\n");
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;

            builder.append("Version code: ").append(versionCode).append("\n")
                           .append("Version name: ").append(versionName).append("\n\n");

        } catch (PackageManager.NameNotFoundException e) {
            builder.append("Unable to retrieve application version details\n\n");
        }
        builder.append("Manufacturer: ").append(Build.MANUFACTURER).append("\n")
                            .append("Model: ").append(Build.MODEL).append("\n")
                            .append("Version: ").append(Build.VERSION.RELEASE).append("\n")
                            .append("Version code: ").append(Build.VERSION.SDK_INT).append("\n");
        return builder.toString();
    }
}
