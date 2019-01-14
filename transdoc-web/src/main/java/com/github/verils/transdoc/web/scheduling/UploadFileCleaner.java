package com.github.verils.transdoc.web.scheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.github.verils.transdoc.web.transform.TempFileStore;

@Component
public class UploadFileCleaner {

	private static final Logger LOGGER = LoggerFactory.getLogger(UploadFileCleaner.class);

	@Autowired
	private TempFileStore tempFileStore;

	@Value("${transdoc.ws.filecleaner.max-file-life}")
	private long maxFileLife;

	public UploadFileCleaner() {
		LOGGER.info("启动定时器: 自动清理已上传文件");
	}

	@Scheduled(fixedRate = 1000 * 60 * 15)
	public void cleanTempDir() {
		tempFileStore.clean();
	}

}
