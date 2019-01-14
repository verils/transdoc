package com.github.verils.transdoc.web.transform;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.github.verils.transdoc.core.Convertor;
import com.github.verils.transdoc.core.MarkdownConverter;
import com.github.verils.transdoc.core.WordParser;
import com.github.verils.transdoc.core.model.Article;
import com.github.verils.transdoc.core.model.Picture;
import com.github.verils.transdoc.web.web.Response;

@CrossOrigin
@RestController
public class TransdocController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransdocController.class);

	@Value("${transdoc.ws.fileupload.temp-dir}")
	private String tempDir = "./temp";

	@PostMapping("/upload")
	public Response<?> upload(@RequestParam String token, @RequestParam MultipartFile file) throws IOException {
		String filename = file.getOriginalFilename();
		String pureFilename = getPureFilename(filename);
		LOGGER.info("文件上传成功: {}", filename);

		WordParser parser = WordParser.prepare(file.getInputStream());
		parser.setSavePictures(true);
		Article article = parser.parse();
		List<Picture> pictures = article.getPictures();

		Convertor convertor = new MarkdownConverter();
		String markdown = convertor.convert(article);
		LOGGER.info("文件解析完成: {}", filename);

		File markdownDir = new File(tempDir, token);
		FileUtils.forceMkdir(markdownDir);

		if (article.hasPictures()) {
			markdownDir = new File(markdownDir, pureFilename);
			FileUtils.forceMkdir(markdownDir);

			for (Picture picture : pictures) {
				File pictureFile = new File(markdownDir, picture.getRelativePath());
				FileUtils.forceMkdirParent(pictureFile);
				FileCopyUtils.copy(picture.getData(), pictureFile);
			}
		}

		File markdownFile = new File(markdownDir, pureFilename + ".md");
		FileCopyUtils.copy(markdown.getBytes("UTF-8"), markdownFile);
		LOGGER.info("文件保存到本地目录: {}", markdownDir.getPath());

		return Response.success();
	}

	@GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<StreamingResponseBody> download(@RequestParam("token") String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment;filename=transdoc-result.zip");
		return new ResponseEntity<StreamingResponseBody>((OutputStream out) -> {
			ZipOutputStream zos = new ZipOutputStream(new CheckedOutputStream(out, new CRC32()));
			zipFiles(zos, new File(tempDir, token), null);
			zos.closeEntry();
			zos.close();
		}, headers, HttpStatus.OK);
	}

	@PostMapping("/transform")
	public ResponseEntity<StreamingResponseBody> batchProcess(@RequestParam("file") MultipartFile[] files) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment;filename=transdoc-batch-result.zip");
		LOGGER.info("批量处理文件, 文件个数: {}", files.length);

		return new ResponseEntity<StreamingResponseBody>((OutputStream out) -> {
			ZipOutputStream zos = new ZipOutputStream(new CheckedOutputStream(out, new CRC32()));
			for (MultipartFile file : files) {
				String filename = file.getOriginalFilename();
				String pureFilename = getPureFilename(filename);

				WordParser parser = WordParser.prepare(file.getInputStream());
				parser.setSavePictures(true);
				Article article = parser.parse();
				List<Picture> pictures = article.getPictures();

				Convertor convertor = new MarkdownConverter();
				String markdown = convertor.convert(article);

				String base = "";
				if (article.hasPictures()) {
					base = pureFilename + File.separator;
					for (Picture picture : pictures) {
						String picturePath = base + picture.getRelativePath();
						zos.putNextEntry(new ZipEntry(picturePath));
						zos.write(picture.getData());
					}
				}

				String markdownPath = base + pureFilename + ".md";
				zos.putNextEntry(new ZipEntry(markdownPath));
				zos.write(markdown.getBytes("UTF-8"));
				zos.closeEntry();
				LOGGER.info("\"{}\"文件处理完成", filename);
			}
			zos.close();
		}, headers, HttpStatus.OK);
	}

	private String getPureFilename(String filename) {
		int lastPoint = filename.lastIndexOf('.');
		String pureFilename = filename;
		if (lastPoint != -1) {
			pureFilename = filename.substring(0, lastPoint);
		}
		return pureFilename;
	}

	private void zipFiles(ZipOutputStream zos, File rootDir, String rootBase) throws IOException {
		File[] files = rootDir.listFiles();
		rootBase = rootBase == null ? "" : rootBase + File.separator;
		for (File file : files) {
			String path = rootBase + file.getName();
			if (file.isDirectory()) {
				zipFiles(zos, file, path);
			} else if (file.isFile()) {
				zos.putNextEntry(new ZipEntry(path));
				IOUtils.copy(new FileInputStream(file), zos);
			}
		}
	}
}
