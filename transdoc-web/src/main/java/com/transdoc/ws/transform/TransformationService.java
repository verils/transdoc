package com.transdoc.ws.transform;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.transdoc.core.Convertor;
import com.transdoc.core.MarkdownConverter;
import com.transdoc.core.WordParser;
import com.transdoc.core.model.WordArticle;
import com.transdoc.core.model.WordPicture;

public class TransformationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransformationService.class);

	private TempFileStore tempFileStore;

	public void transform(String workDirname, String filename, InputStream in) throws IOException {
		LOGGER.info("文件上传成功: {}", filename);

		WordParser parser = WordParser.prepare(in, true);
		WordArticle article = parser.parse();

		Convertor convertor = new MarkdownConverter();
		String markdown = convertor.convert(article);
		LOGGER.info("文件解析完成: {}", filename);

		Path workDir = tempFileStore.tempDir(workDirname);
		Files.createDirectories(workDir);

		String pureFilename = getPureFilename(filename);
		if (article.hasPictures()) {
			Path docDir = workDir.resolve(pureFilename);
			Files.createDirectory(docDir);

			Path docFile = workDir.resolve(pureFilename + ".md");
			Files.write(docFile, markdown.getBytes("UTF-8"));

			for (WordPicture picture : article.getPictures()) {
				Path picFile = docDir.resolve(picture.getRelativePath());
				Files.write(picFile, picture.getData());
			}
		} else {
			Files.write(workDir, markdown.getBytes("UTF-8"));
		}
		LOGGER.info("文件已保存到目录: {}", workDir.toFile().getPath());
	}

	private String getPureFilename(String filename) {
		int lastPoint = filename.lastIndexOf('.');
		String pureFilename = filename;
		if (lastPoint != -1) {
			pureFilename = filename.substring(0, lastPoint);
		}
		return pureFilename;
	}

}
