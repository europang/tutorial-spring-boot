package gsshop.jbp.dashboard.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gsshop.jbp.dashboard.dto.Sample;

@RestController
public class DashboardAPIController {
	
	@RequestMapping("/api/sample")
	public Sample sample() {
		
		Sample sampleObject = new Sample();
		sampleObject.setEmail("yohany@gmail.com");
		sampleObject.setName("John Kim");
		
		return sampleObject;
	}

}
