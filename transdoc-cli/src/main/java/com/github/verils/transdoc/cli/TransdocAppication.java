package com.github.verils.transdoc.cli;

import com.github.verils.transdoc.cli.logging.TransdocLogger;
import com.github.verils.transdoc.core.Convertor;
import com.github.verils.transdoc.core.MarkdownConverter;
import com.github.verils.transdoc.core.WordParser;
import com.github.verils.transdoc.core.model.Article;
import com.github.verils.transdoc.core.model.Picture;
import org.apache.poi.util.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransdocAppication {

	private static final TransdocLogger LOGGER = TransdocLogger.getLog(TransdocAppication.class);

	static final FileFilter WORD_FILE_FILTER = new FileFilter() {
		@Override
		public boolean accept(File file) {
			String filename = file.getName();
			return file.isFile() && (filename.endsWith(".doc") || filename.endsWith(".docx"))
					&& !filename.startsWith("~");
		}
	};

	static final String DEFAULT_DIR = "./docs";

	/**
	 * 程序入口函数,通过cmd执行传入word文档的路径,可直接读取文件进行转换操作.<br>
	 * 
	 * @param paths
	 *            带转换的doc文件路径
	 */
	public static void main(String[] paths) {
		LOGGER.info("========== Transdoc ==========");

		List<File> files = null;
		if (paths == null || paths.length <= 0) {
			files = getFilesFromDirectory(DEFAULT_DIR);
		} else {
			files = getFilesFromPathParams(paths);
		}

		if (files != null) {
			process(files);
		}
	}

	/**
	 * 获取命令执行参数中的文件
	 * 
	 * @param paths
	 * @return 文件列表
	 */
	static List<File> getFilesFromPathParams(String[] paths) {
		List<File> files = new ArrayList<File>();
		for (String path : paths) {
			files.add(new File(path));
		}
		return files;
	}

	/**
	 * 从默认文件夹中获取文件
	 * 
	 * @param dirPath
	 * @return 文件列表
	 */
	static List<File> getFilesFromDirectory(String dirPath) {
		List<File> files = new ArrayList<File>();
		File srcDir = new File(dirPath);
		String dirName = srcDir.getName();
		if (!srcDir.exists()) {
			LOGGER.error(dirName + "目录不存在,未执行文件扫描");
		} else if (!srcDir.isDirectory()) {
			LOGGER.error(dirName + "不是有效的目录,未执行文件扫描");
		} else {
			File[] listFiles = srcDir.listFiles(WORD_FILE_FILTER);
			if (listFiles != null && listFiles.length > 0) {
				files = Arrays.asList(listFiles);
			} else {
				LOGGER.error(dirName + "目录中没有可转换的文件");
			}
		}
		return files;
	}

	static void process(List<File> files) {
		for (final File file : files) {
			try {
				long startTime = System.currentTimeMillis();
				transform(file);
				long spendTime = System.currentTimeMillis() - startTime;
				LOGGER.info(file.getName() + " - 转换成功，用时: " + spendTime + "ms");
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	static void transform(File file) throws IOException {
		String docFilename = file.getName();
		docFilename = docFilename.substring(0, docFilename.lastIndexOf("."));

		File destDir = new File(DEFAULT_DIR, docFilename);
		destDir.mkdirs();

		WordParser parser = WordParser.prepare(new FileInputStream(file));
		parser.setSavePictures(true);
		Article article = parser.parse();

		// 将通用数据对象内容转换为md格式
		Convertor convertor = new MarkdownConverter();
		String markdown = convertor.convert(article);

		File mdFile = new File(destDir, docFilename + ".md");
		FileOutputStream fos = new FileOutputStream(mdFile);
		fos.write(markdown.getBytes("UTF-8"));
		IOUtils.closeQuietly(fos);

		List<Picture> pictures = article.getPictures();
		for (Picture picture : pictures) {
			File picFile = new File(destDir, picture.getRelativePath());
			if (!picFile.getParentFile().exists()) {
				picFile.getParentFile().mkdirs();
			}
			picture.writeTo(new FileOutputStream(picFile));
		}
	}
}
