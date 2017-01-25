package gsshop.jbp.dashboard.mapper;

import org.apache.ibatis.annotations.Mapper;

import gsshop.jbp.dashboard.dto.Sample;

@Mapper
public interface SampleMapper {
	
	public Sample findById(Long id);
	
}
