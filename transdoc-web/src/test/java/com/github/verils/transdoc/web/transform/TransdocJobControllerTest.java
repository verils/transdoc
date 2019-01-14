package com.github.verils.transdoc.web.transform;

import static com.github.verils.transdoc.web.test.matcher.UUIDMatcher.isUUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(TransdocJobController.class)
public class TransdocJobControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TransformationService transformationService;
	@MockBean
	private TempFileStore tempFileStore;

	private String jobId = UUID.randomUUID().toString().replace("-", "");

	@Before
	public void setUp() {
	}

	@Test
	public void testCreateJob() throws Exception {
		mockMvc.perform(post("/jobs")).andExpect(status().isCreated()).andExpect(content().string(isUUID()));
	}

	@Test
	public void testCheckJob() throws Exception {
		mockMvc.perform(get("/jobs/" + jobId + "/state")).andExpect(status().isOk())
				.andExpect(content().string("false"));
		verify(tempFileStore).exists(anyString());
	}

	@Test
	public void testUpload() throws Exception {
		mockMvc.perform(multipart("/jobs/" + jobId + "/upload").file("file", "test content".getBytes()))
				.andExpect(status().isAccepted());
		verify(transformationService).transform(eq(jobId), anyString(), any(InputStream.class));
	}

	@Test
	public void testDownload() throws Exception {
		mockMvc.perform(get("/jobs/" + jobId + "/download")).andExpect(status().isOk())
				.andExpect(content().bytes(new byte[0]));
	}
}
