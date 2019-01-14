package com.github.verils.transdoc.web.transform;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@CrossOrigin
@RestController
@RequestMapping("/jobs")
public class TransdocJobController {

	private static final Pattern UUID_PATTERN = Pattern.compile("[a-zA-Z0-9\\-]{36}");

	@Autowired
	private TransformationService transformationService;

	@Autowired
	private TempFileStore tempFileStore;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public String createJob() {
		return UUID.randomUUID().toString().toLowerCase().replace("-", "");
	}

	@GetMapping("/{jobId}/state")
	public boolean checkJob(@PathVariable String jobId) {
		boolean isValidId = UUID_PATTERN.matcher(jobId).matches();
		boolean dirExists = tempFileStore.exists(jobId);
		return isValidId && dirExists;
	}

	@PostMapping("/{jobId}/upload")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void uploadAndTransform(@PathVariable String jobId, @RequestParam MultipartFile file) throws IOException {
		transformationService.transform(jobId, file.getOriginalFilename(), file.getInputStream());
	}

	@GetMapping("/{jobId}/download")
	public ResponseEntity<StreamingResponseBody> zipAndDownload(@PathVariable String jobId) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment;filename=transdoc-result.zip");
		return new ResponseEntity<StreamingResponseBody>(out -> tempFileStore.zip(jobId, out), headers, HttpStatus.OK);
	}
}
