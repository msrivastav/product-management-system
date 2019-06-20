package com.cmt.importer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CsvImporterServiceApplication.class, loader = AnnotationConfigContextLoader.class)
public class CsvImporterServiceApplicationTests {
	
	@Test
	public void contextLoads() {
	}

}
