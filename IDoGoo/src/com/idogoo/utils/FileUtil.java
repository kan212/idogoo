package com.idogoo.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamCorruptedException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import android.content.Context;
import android.text.TextUtils;

/**
 * @author hushuan
 * 
 */
public class FileUtil {

	/**
	 * 是否存储卡状态
	 * 
	 * @return
	 */
	public static boolean haveSDCard() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	/**
	 * 获取目录文件 如果目录文件不存在则创建
	 * 
	 * @param folderPath
	 *            ：禁用字符 \ | * ? < >
	 * @return File 如果exists证明存在
	 */
	public static File getSdcardDirectory(String folderPath) {
		File sdcardDirectory = android.os.Environment
				.getExternalStorageDirectory();// sdcard
		if (TextUtils.isEmpty(folderPath))
			return sdcardDirectory;
		if (!folderPath.startsWith("/"))
			folderPath = "/" + folderPath;
		String path = sdcardDirectory.getPath() + folderPath;
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	/**
	 * 删除一个文件或目录(包括子目录及文件)
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean deleteFile(File file) {
		if (null == file || !file.exists())
			return false;
		boolean result = true;
		if (file.isDirectory()) {
			File[] list = file.listFiles();
			for (File temp : list) {
				result = result && deleteFile(temp);
			}
			result = result && file.delete();
		} else if (file.isFile()) {
			return file.delete();
		}
		return result;
	}

	public static long getFileSize(File file) {
		long size = 0;
		if (null == file || !file.exists()) {
			return size;
		}
		if (file.isDirectory()) {
			File[] list = file.listFiles();
			for (File temp : list) {
				size += getFileSize(temp);
			}
		} else if (file.isFile()) {
			return file.length();
		}
		return size;
	}

	/**
	 * 将字节数组保到文件
	 * 
	 * @param buffer
	 * @param fileName
	 * @return
	 */
	public static boolean saveBytesToFile(byte[] buffer, File file) {
		return saveBytesToFile(buffer, file, false);
	}

	/**
	 * 将字节数组保到文件
	 * 
	 * @param buffer
	 *            中文可直接使用getBytes方法
	 * @param file
	 *            文件所在目录不存在则写入不成功: java.io.IOException: No such file or
	 *            directory
	 * 
	 * @param append
	 * @return
	 */
	public static boolean saveBytesToFile(byte[] buffer, File file,
			boolean append) {
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		if (file.isFile() && file.canWrite()) {
			try {
				FileOutputStream fos = new FileOutputStream(file, append);
				fos.write(buffer);
				fos.flush();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * 从文件中得到字节数组。注：适用于小文件
	 * 
	 * @param file
	 * @return
	 */
	public static byte[] getBytesFromFile(File file) {
		if (!file.exists())
			return null;
		FileInputStream fis;
		byte[] buffer;
		try {
			fis = new FileInputStream(file);
			buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return buffer;
	}

	/**
	 * 从文件中得到字节数组。注：适用于大文件
	 * 
	 * @param file
	 * @param readlimit
	 * @return
	 */
	public static byte[] getBytesFromFile(File file, int readlimit) {
		if (!file.exists())
			return null;
		FileInputStream fis;
		byte[] buffer;
		try {
			fis = new FileInputStream(file);
			buffer = new byte[readlimit];
			fis.read(buffer, 0, readlimit);
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return buffer;
	}

	/**
	 * 从文件中得到字节数组。注：适用于大文件
	 * 
	 * @param file
	 * @param readlimit
	 * @return
	 */
	public static String getStringFromFile(File file, int readlimit) {
		if (!file.exists())
			return null;
		FileInputStream fis;
		StringBuilder sb = new StringBuilder();
		try {
			fis = new FileInputStream(file);
			InputStreamReader inputReader = new InputStreamReader(fis, "gbk");
			BufferedReader reader = new BufferedReader(inputReader);
			String line;
			int sum = 0;
			while ((line = reader.readLine()) != null && readlimit > sum) {
				sum++;
				sb.append(line).append('\n');
			}
			reader.close();
			inputReader.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return sb.toString();
	}

	public static void saveFile(Context context, String fileName, byte[] buffer) {
		GZIPOutputStream gos = null;
		try {
			FileOutputStream fos = context.openFileOutput(fileName,
					Context.MODE_PRIVATE);
			gos = new GZIPOutputStream(fos);
			gos.write(buffer);
			gos.flush();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			try {
				if (null != gos) {
					gos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static int BUFFER = 1024;

	public static byte[] readFile(Context context, String fileName) {
		GZIPInputStream gis = null;
		byte[] buffer = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			FileInputStream fis = context.openFileInput(fileName);
			gis = new GZIPInputStream(fis);
			byte data[] = new byte[BUFFER];
			int count;
			while ((count = gis.read(data, 0, BUFFER)) != -1) {
				baos.write(data, 0, count);
			}
			buffer = baos.toByteArray();
		} catch (FileNotFoundException e) {
		} catch (StreamCorruptedException e) {
		} catch (IOException e) {
		} finally {
			try {
				if (null != gis) {
					gis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return buffer;
	}
}
