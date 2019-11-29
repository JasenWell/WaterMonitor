package com.hjh.baselib.utils;


import android.content.Context;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Descroption:
 * Created by hjh on 2015/9/6.
 */
public class PathUtil {

    public static String pathPrefix;
    private static File storageDir = null;
    private static PathUtil instance = null;
    private File voicePath = null;
    private File imagePath = null;
    private File videoPath = null;
    private File filePath;
    private File tempPath;

    public static void init(Context context){
        if (instance == null)
            instance = new PathUtil(context);
    }

    public static PathUtil getInstance(){
        return instance;
    }

    private PathUtil(Context context){
        initDirs(context);
    }

    private void initDirs(Context paramContext){
        String str = paramContext.getPackageName();
        pathPrefix = "/Android/data/" + str + "/";
        this.voicePath = generatePath(paramContext, "audio/");
        if (!this.voicePath.exists())
            this.voicePath.mkdirs();
        this.imagePath = generatePath(paramContext, "image/");
        if (!this.imagePath.exists())
            this.imagePath.mkdirs();

        this.videoPath = generatePath(paramContext, "video/");
        if (!this.videoPath.exists())
            this.videoPath.mkdirs();
        this.filePath = generatePath(paramContext, "file/");
        if (!this.filePath.exists())
            this.filePath.mkdirs();

        this.tempPath = generatePath(paramContext,"temp/");
        if(!this.tempPath.exists()){
            this.tempPath.mkdirs();
        }
    }

    public File getImagePath(){
        return this.imagePath;
    }

    public File getVoicePath(){
        return this.voicePath;
    }

    public File getFilePath(){
        return this.filePath;
    }

    public File getVideoPath(){
        return this.videoPath;
    }

    public File getTempPath() {
        return tempPath;
    }

    private static File getStorageDir(Context paramContext) {
        if (storageDir == null) {
            File localFile = Environment.getExternalStorageDirectory();
            if (localFile.exists())
                return localFile;
            storageDir = paramContext.getFilesDir();
        }
        return storageDir;
    }

    public File generatePath(Context context, String pathName){
        String path = pathPrefix + pathName;
        return new File(getStorageDir(context), path);
    }

    public void deleteFile(File dir){
    	if(dir == null)return;
       File[] files =  dir.listFiles();
        if(files == null || files.length  == 0){
            return;
        }

        for(File file : files){
            file.delete();
        }
    }

    public boolean isImgExist(String fileName){
        File file = new File(getImagePath(),fileName+".jpg");
        if (file.exists()) {
            return true;
        }
        return false;
    }

    public void deleteFromSD(String fileName){
        File file = new File(getImagePath(),fileName+".jpg");
        if (file.exists()) {
            file.delete();
        }
    }

    public File newImgToSD(String fileName){
        File imgFile = new File(getImagePath(),fileName+".jpg");
        if (imgFile.exists()) {
            imgFile.delete();
        }
        imgFile = new File(getImagePath(),fileName+".jpg");
        return imgFile;
    }

    public String getImagePath(String fileName){
        File imgFile = new File(getImagePath(),fileName+".jpg");
        if(!imgFile.exists())return null;//表示本地无此文件
        return imgFile.getAbsolutePath();
    }

    /**
     * 将图片转化为Base64数据格式
     * @return
     */
    public String encodeImgToBase64(String path){

        File file = new File(path);
        if (file != null) {
            try {
                DataInputStream is = new DataInputStream(new FileInputStream(file));
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int b = -1;
                byte[] c = new byte[1024];
                while ((b = is.read(c)) > -1) {
                    bos.write(c, 0, b);
                }
                bos.flush();
                String str = Base64.encodeToString(bos.toByteArray(),
                        Base64.DEFAULT);
                bos.close();
                is.close();
                return str;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将服务器获取的Base64数据转化为图片，以用户ID命名并保存在SD卡上的CHDoctor文件夹中
     * @param base64Str
     * @param imgName
     */
    public String decodeBase64ToImg(String base64Str, String imgName) {
        if (base64Str != null){
            byte[] bytes = Base64.decode(base64Str, Base64.DEFAULT);
            try {
                for(int i=0;i<bytes.length;++i)
                {
                    if(bytes[i]<0){//调整异常数据
                        bytes[i]+=256;
                    }
                }
                //生成jpg图片
                String imgFilePath = getTempPath()+imgName+".jpg";//新生成的图片
                File imgFile= new File(imgFilePath);
                if (imgFile.exists()) {
                    imgFile.delete();
                }
                OutputStream out = new FileOutputStream(imgFilePath);
                out.write(bytes);
                out.flush();
                out.close();
                return imgFilePath;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        return null;
    }
}
