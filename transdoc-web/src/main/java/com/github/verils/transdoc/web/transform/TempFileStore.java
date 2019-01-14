package com.github.verils.transdoc.web.transform;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TempFileStore {

	private static final Logger LOGGER = LoggerFactory.getLogger(TempFileStore.class);

	@Value("${transdoc.ws.fileupload.max-file-life}")
	private long maxFileLife;

	private File root;

	@Autowired
	public TempFileStore(String path) {
		this.root = new File(path);
	}

	public Path tempDir(String dirname) {
		return root.toPath().resolve(dirname);
	}

	public boolean exists(String dirname) {
		return tempDir(dirname).toFile().exists();
	}

	public void zip(String dirname, OutputStream out) {

	}

	public void clean() {
		try {
			File cleanDir = root;
			String path = cleanDir.getCanonicalPath();
			LOGGER.info("自动清理清理文件上传目录: {}", path);

			if (!cleanDir.exists()) {
				LOGGER.info("未执行清理操作, 目录不存在: {}", path);
				return;
			}

			File[] files = cleanDir.listFiles();
			for (File file : files) {
				Path filePath = file.toPath();
				BasicFileAttributes attrs = Files.readAttributes(filePath, BasicFileAttributes.class);
				FileTime creationTime = attrs.creationTime();
				long duration = System.currentTimeMillis() - creationTime.toMillis();
				if (duration > maxFileLife) {
					FileUtils.forceDelete(file);
				}
			}
			LOGGER.info("\"{}\"目录清理完毕", cleanDir);
		} catch (RuntimeException | IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

}
