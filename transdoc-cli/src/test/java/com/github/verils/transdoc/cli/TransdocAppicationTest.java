package com.github.verils.transdoc.cli;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

public class TransdocAppicationTest {

	@Test
	public void testMain() throws IOException {
		TransdocAppication.main(null);
	}

	@Test
	public void testTransformForDoc() throws IOException, URISyntaxException {
		TransdocAppication
				.transform(new File(TransdocAppicationTest.class.getResource("/测试.doc").toURI()));
	}

	@Test
	public void testTransformForDocx() throws IOException, URISyntaxException {
		TransdocAppication
				.transform(new File(TransdocAppicationTest.class.getResource("/测试.docx").toURI()));
	}

}
