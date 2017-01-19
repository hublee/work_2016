package com.zeustel.cp.utils;

import android.content.Context;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * 本地缓存
 * @author Administrator
 *
 */
public class NetCache {
	public static final String KEY_CONFIG = "channelconfig";
	public static final String KEY_CRASH_INFO = "crashinfo";

	private static DiskLruCache cache;
	
	private static NetCache netCache = new NetCache();
	
	public static final String DIR_OBJECT = "object";//对象目录
	public static final String DIR_STRING = "string";//串
	public static final String DIR_BITMAP = "bitmap";//图片
	public static final String DIR_CRASH = "crash";//异常
	
	private NetCache(){
		
	}
	
	public static NetCache getInstance(){
		return netCache;
	}
	
	public void addCache(String key,Object value){
		try {
			DiskLruCache.Editor editor = cache.edit(key);
			OutputStream outputStream = editor.newOutputStream(0);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject(value);
			editor.commit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		flushCache();
	}
	
	public void addCache(String key,String value){
		try {
			DiskLruCache.Editor editor = cache.edit(key);
			OutputStream outputStream = editor.newOutputStream(0);
			outputStream.write(value.getBytes());
			editor.commit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addCrash(String key,Object obj){
		addCache(key,obj);
		String lastCache = getStringCache(KEY_CRASH_INFO);
		addCache(KEY_CRASH_INFO, lastCache == null ? key + "#" : lastCache + key + "#");//保存所有的crash key
	}
	
	public void openObjectCache(Context context){
		openDiskCache(context,DIR_OBJECT);
	}
	
	public void openBitmapCache(Context context){
		openDiskCache(context,DIR_BITMAP);
	}
	
	public void openStringCache(Context context){
		openDiskCache(context,DIR_STRING);
	}
	
	public void openCrashCache(Context context){
		openDiskCache(context,DIR_CRASH);
	}
	
	private void openDiskCache(Context context,String dir){
		cache = null;
		File file = getDiskCacheDir(context,dir);
		if(!file.exists()){
			file.mkdirs();
		}
		try {
			cache = DiskLruCache.open(file, 100, 1, 10*1024*1024);
			
			long size = cache.size();
			int count = 0;
			while(size>1024){
				size = size/1024;
				count++;
			}
			
			SdkUtils.LogD("缓存大小：",size+(count==0?"B":"")+(count==1?"K":"")+(count==2?"M":""));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private File getDiskCacheDir(Context context, String uniqueName) {
		String cachePath = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {
			if(context.getExternalCacheDir()!=null){
				cachePath = context.getExternalCacheDir().getPath();
			}
		} else {
			cachePath = context.getCacheDir().getPath();
		}
		return new File(cachePath + File.separator + uniqueName);
	}
	
	public String getStringCache(String key){
		try {
			DiskLruCache.Snapshot snapshot = cache.get(key);
			if(snapshot!=null){
				InputStream inputStream = snapshot.getInputStream(0);
				
				ByteArrayOutputStream baos =new ByteArrayOutputStream(); 
			    int  i=-1; 
			    while((i=inputStream.read())!=-1){ 
			       baos.write(i); 
			    } 
			    return baos.toString(); 
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 所有的crash key
	 * @return
	 */
	public String[] getCrashKeyList(){
		String keyString = getStringCache(KEY_CRASH_INFO);
		return keyString.split("#");
	}
	
	public InputStream getInputStreamCache(String key){
		try {
			DiskLruCache.Snapshot snapshot = cache.get(key);
			if(snapshot!=null){
				InputStream inputStream = snapshot.getInputStream(0);
				return inputStream;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Object getObjectCache(String key){
		try {
			DiskLruCache.Snapshot snapshot = cache.get(key);
			if(snapshot!=null){
				InputStream inputStream = snapshot.getInputStream(0);
				try {
					ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
				
					return objectInputStream.readObject();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void closeCache(){
		try {
			cache.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void flushCache(){
		try {
			cache.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public long getCacheSize(){
		return cache.size();
	}
	
	public void delete(){
		try {
			cache.delete();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void remove(String key){
		try {
			cache.remove(key);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
