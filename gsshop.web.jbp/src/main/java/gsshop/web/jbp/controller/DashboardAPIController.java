package gsshop.web.jbp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gsshop.web.jbp.dto.Sample;
import gsshop.web.jbp.mapper.SampleMapper;

@RestController
public class DashboardAPIController {

	private static final Logger logger = LoggerFactory.getLogger(DashboardAPIController.class);

	@Autowired SampleMapper sampleMapper;

	@RequestMapping("/api/sample/string")
	public String sampleString() {
		return "Hello Workd";
	}

	@RequestMapping("/api/sample/object")
	public Sample sampleObject() {
		Sample sampleObject = new Sample();
		sampleObject.setEmail("yohany@gmail.com");
		sampleObject.setName("John Kim");
		return sampleObject;
	}

	@RequestMapping("/api/user/{id}")
	public Sample user(@PathVariable long id) {
		Sample sampleObject = sampleMapper.findById(id); 
		return sampleObject;
	}

	@RequestMapping("/api/username")
	public String currentUserNameSimple(Authentication authentication) {

		//logger.debug(authentication.getCredentials().toString());
		//logger.debug(authentication.getDetails().toString());
		return authentication.getName();
	}
	
	@RequestMapping("/TEST")
	public String test() {
		return "SSSSS";
		
	}
}
