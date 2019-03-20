package com.ariix.caremawx;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.ariix.caremawx.utils.PolyvStorageUtils;
import com.easefun.polyvsdk.PolyvDevMountInfo;
import com.easefun.polyvsdk.PolyvDownloaderManager;
import com.easefun.polyvsdk.PolyvSDKClient;
import com.easefun.polyvsdk.screencast.PolyvScreencastHelper;

import java.io.File;
import java.util.ArrayList;

public class CaremaApplication extends Application {


    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        initPolyvSDK();
    }

    public void initPolyvSDK() {
        initPolyvCilent();
        setDownloadDir();

    }


    public void initPolyvCilent() {
        //网络方式取得SDK加密串，（推荐）
        //网络获取到的SDK加密串可以保存在本地SharedPreference中，下次先从本地获取
//		new LoadConfigTask().execute();
        PolyvSDKClient client = PolyvSDKClient.getInstance();
        //使用SDK加密串来配置
        client.setConfig("fh6aJzDx45Y8rNEXvJgJ4k+czSpHwzP/J6kmv9LhU4CFBuAEOuIoYtaDbqKB0G2jxAkU+j+FEmGAZkB43OrONmAMJKSweD7GElUTvERyBEtg8n74IlURj3inmXHHCg//+0W4ByNXbIkP4tdpmtw2qQ==", "VXtlHmwfS2oYm0CZ", "2u9gDPKdX6GyQJKU", context);

//        client.setConfig("CMWht3MlpVkgpFzrLNAebYi4RdQDY/Nhvk3Kc+qWcck6chwHYKfl9o2aOVBvXVTRZD/14XFzVP7U5un43caq1FXwl0cYmTfimjTmNUYa1sZC1pkHE8gEsRpwpweQtEIiTGVEWrYVNo4/o5jI2/efzA==", aeskey, iv, getApplicationContext());
        //初始化SDK设置
        client.initSetting(getApplicationContext());
        //启动Bugly
        client.initCrashReport(getApplicationContext());
        //启动Bugly后，在学员登录时设置学员id
//		client.crashReportSetUserId(userId);
        setDownloadDir();
        // 设置下载队列总数，多少个视频能同时下载。(默认是1，设置负数和0是没有限制)
        PolyvDownloaderManager.setDownloadQueueCount(1);
    }

    /**
     * 设置下载视频目录
     */
    private void setDownloadDir() {
        String rootDownloadDirName = "polyvdownload";
        ArrayList<File> externalFilesDirs = PolyvStorageUtils.getExternalFilesDirs(getApplicationContext());
        if (externalFilesDirs.size() == 0) {
            // TODO 没有可用的存储设备,后续不能使用视频缓存功能
            Log.e("CaremaApplication", "没有可用的存储设备,后续不能使用视频缓存功能");
            return;
        }

        //SD卡会有SD卡接触不良，SD卡坏了，SD卡的状态错误的问题。
        //我们在开发中也遇到了SD卡没有权限写入的问题，但是我们确定APP是有赋予android.permission.WRITE_EXTERNAL_STORAGE权限的。
        //有些是系统问题，有些是SD卡本身的问题，这些问题需要通过重新拔插SD卡或者更新SD卡来解决。所以如果想要保存下载视频至SD卡请了解这些情况。
        File downloadDir = new File(externalFilesDirs.get(0), rootDownloadDirName);
        PolyvSDKClient.getInstance().setDownloadDir(downloadDir);

        //兼容旧下载视频目录，如果新接入SDK，无需使用以下代码
        //获取SD卡信息
        PolyvDevMountInfo.getInstance().init(this, new PolyvDevMountInfo.OnLoadCallback() {

            @Override
            public void callback() {
                //是否有可移除的存储介质（例如 SD 卡）或内部（不可移除）存储可供使用。
                if (!PolyvDevMountInfo.getInstance().isSDCardAvaiable()) {
                    return;
                }

                //可移除的存储介质（例如 SD 卡），需要写入特定目录/storage/sdcard1/Android/data/包名/。
                //现在PolyvDevMountInfo.getInstance().getExternalSDCardPath()默认返回的目录路径就是/storage/sdcard1/Android/data/包名/。
                //跟PolyvDevMountInfo.getInstance().init(Context context, final OnLoadCallback callback)接口有区别，请保持同步修改。
                ArrayList<File> subDirList = new ArrayList<>();
                String externalSDCardPath = PolyvDevMountInfo.getInstance().getExternalSDCardPath();
                if (!TextUtils.isEmpty(externalSDCardPath)) {
                    StringBuilder dirPath = new StringBuilder();
                    dirPath.append(externalSDCardPath).append(File.separator).append("polyvdownload");
                    File saveDir = new File(dirPath.toString());
                    if (!saveDir.exists()) {
                        saveDir.mkdirs();//创建下载目录
                    }

                    //设置下载存储目录
//					PolyvSDKClient.getInstance().setDownloadDir(saveDir);
//					return;
                    subDirList.add(saveDir);
                }

                //如果没有可移除的存储介质（例如 SD 卡），那么一定有内部（不可移除）存储介质可用，都不可用的情况在前面判断过了。
                File saveDir = new File(PolyvDevMountInfo.getInstance().getInternalSDCardPath() + File.separator + "polyvdownload");
                Log.d("saveDir is exist", saveDir.exists() + "-----");
                if (!saveDir.exists()) {
                    boolean successed = saveDir.mkdirs();//创建下载目录
                    Log.d("is make dir", successed + "--------");
                }

                //设置下载存储目录
//				PolyvSDKClient.getInstance().setDownloadDir(saveDir);
                subDirList.add(saveDir);
                PolyvSDKClient.getInstance().setSubDirList(subDirList);
            }
        }, true);
    }

}
