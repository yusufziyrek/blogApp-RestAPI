package com.yusufziyrek.blogApp.utilites.mappers;

import org.modelmapper.ModelMapper;

public interface IModelMapperService {

	ModelMapper forResponse();

	ModelMapper forRequest();

}
