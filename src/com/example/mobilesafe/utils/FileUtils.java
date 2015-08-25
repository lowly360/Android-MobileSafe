package com.example.mobilesafe.utils;

import java.io.File;

public class FileUtils {

	/**
	 * 找到程序的cache文件夹
	 * 
	 * @param sourceDir
	 * @return
	 */
	public static File GetCacheFileDir(String sourceDir) {
		System.out.println("<<<<<<<<<< "+sourceDir);
		File file = new File(sourceDir);
		if (file.exists()) {
			System.out.println("+++++++++ "+file.getAbsolutePath());
			File[] listFiles = file.listFiles();
			if (listFiles == null) {
				System.out.println("不是文件夹");
				return null;
			}
			for (File file2 : listFiles) {
				System.out.println("-----------");
				System.out.println("--- " + file2.getName());
				System.out.println("-----------");

				if (file2.isDirectory()) {
					if (file2.getName().equals("cache")) {
						System.out.println("-------->   Has cachedir!!!");
						return file2;
					}
				}

			}
		}
		return null;
	}

	/**
	 * 获取到文件夹的总大小
	 * 
	 * @param sourceDir
	 * @return
	 */
	public static long GetSizeFromCache(String sourceDir) {
		// 获取到cache文件夹
		File cacheFileDir = GetCacheFileDir(sourceDir);
		if (cacheFileDir != null) {
			long total = getSizefromDir(cacheFileDir, 0);
			System.out.println("total " + total);
			return total;
		}
		return 0;
	}

	/**
	 * 遍历文件夹内所有文件,文件夹,计算总大小
	 * 
	 * @param file
	 * @param total
	 * @return
	 */
	public static long getSizefromDir(File file, long total) {
		if (file.isDirectory()) {
			File[] listFiles = file.listFiles();
			if (listFiles != null) {
				for (File file2 : listFiles) {
					if (file2.isDirectory()) {
						getSizefromDir(file2, total);
					} else if (file2.isFile()) {
						total += file2.length();
					}
				}
			}
		}
		return total;
	}

	public static void DeleteFileDir(String sourceDir) {
		// 获取到cache文件夹
		File cacheFileDir = GetCacheFileDir(sourceDir);
		File[] listFiles = cacheFileDir.listFiles();
		if (listFiles != null) {
			for (File file : listFiles) {
				file.delete();
			}
		}
	}
}
