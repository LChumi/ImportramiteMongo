package com.cumpleanos.importramite.service.implementation;

import com.cumpleanos.importramite.service.http.productosClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ProductosClientServiceImpl {

    private final productosClient productosClient;


}
