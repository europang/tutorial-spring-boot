package gsshop.jbp.dashboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gsshop.jbp.dashboard.dto.Sample;
import gsshop.jbp.dashboard.mapper.SampleMapper;

@RestController
public class DashboardAPIController {
	
	@Autowired SampleMapper sampleMapper;
	
	@RequestMapping("/api/sample/{id}")
	public Sample sample(@PathVariable long id) {
		Sample sampleObject = sampleMapper.findById(id); 
		return sampleObject;
	}

}
