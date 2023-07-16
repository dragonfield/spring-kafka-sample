package com.example.demo.infrastructure.repository.mapper;

import com.example.demo.infrastructure.repository.entity.EmployeeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

  @Select("SELECT id, first_name, last_name FROM employees WHERE id = #{id}")
  EmployeeEntity find(String id);

}
