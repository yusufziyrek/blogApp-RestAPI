package com.yusufziyrek.blogApp.shared.mapper;

import org.modelmapper.ModelMapper;

public interface IModelMapperService {

	ModelMapper forResponse();

	ModelMapper forRequest();

}
