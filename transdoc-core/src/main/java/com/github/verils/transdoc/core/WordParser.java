package com.github.verils.transdoc.core;

import com.github.verils.transdoc.core.model.Article;
import com.github.verils.transdoc.core.model.Picture;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.UnsupportedFileFormatException;
import org.apache.poi.util.IOUtils;

public abstract class WordParser {
	boolean savePictures = false;
	String pictureDir = "picture";
	String pictureNamePattern = "pic_%d";

	public static WordParser prepare(InputStream stream) throws TransdocException, IOException {
		return prepare(stream, false);
	}

	public static WordParser prepare(InputStream stream, boolean savePictures) throws TransdocException, IOException {
		if (stream == null) {
			throw new NullPointerException("InputStream can not be null");
		}

		byte[] data = IOUtils.toByteArray(stream);
		IOUtils.closeQuietly(stream);

		WordParser wordParser = null;
		try {
			wordParser = new DocxParser(new ByteArrayInputStream(data));
		} catch (UnsupportedFileFormatException e) {
			wordParser = tryDocParser(new ByteArrayInputStream(data));
		} catch (Exception e) {
			wordParser = tryDocParser(new ByteArrayInputStream(data));
		}

		wordParser.setSavePictures(savePictures);
		return wordParser;
	}

	private static WordParser tryDocParser(InputStream stream) {
		try {
			return new DocParser(stream);
		} catch (IOException e) {
			throw new WordParsingException("不是有效的doc文件或docx文件", e);
		} catch (Exception e) {
			throw new WordParsingException("文件解析错误", e);
		}
	}

	public abstract Article parse();

	String getImagePath(Picture image) {
		int index = image.getIndex();
		String indexStr = ((index < 10) ? "0" : "") + index;
		return pictureDir + "/" + pictureNamePattern.replace("%d", indexStr) + "." + image.getExtension();
	}

	public boolean isSavePictures() {
		return savePictures;
	}

	public void setSavePictures(boolean savePictures) {
		this.savePictures = savePictures;
	}

	public String getPictureDir() {
		return pictureDir;
	}

	public void setPictureDir(String pictureDir) {
		this.pictureDir = pictureDir;
	}

	public String getPictureNamePattern() {
		return pictureNamePattern;
	}

	public void setPictureNamePattern(String pictureNamePattern) {
		this.pictureNamePattern = pictureNamePattern;
	}

}