package com.royallondon.util;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtils {

	static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);

	public FileUtils() {
		// TODO Auto-generated constructor stub
	}

	public long getFileAccessTime(String filename) {
		File f = new File(filename);
		if (!f.exists())
			return -1;
		else
			return f.lastModified();
	}
}
