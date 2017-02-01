package gsshop.web.jbp.mapper;

import org.apache.ibatis.annotations.Mapper;

import gsshop.web.jbp.dto.Sample;

@Mapper
public interface SampleMapper {

	public Sample findById(long id);

}
