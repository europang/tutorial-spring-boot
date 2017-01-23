package gsshop.jbp.dashboard.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import gsshop.jbp.dashboard.dto.Sample;

@Mapper
public interface SampleMapper {
	
	@Select("SELECT * FROM SAMPLE WHERE id = #{id}")
	public Sample findById(@Param("id") long id);
	
}
