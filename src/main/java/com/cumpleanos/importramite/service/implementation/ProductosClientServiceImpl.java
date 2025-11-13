package com.cumpleanos.importramite.service.implementation;

import com.cumpleanos.importramite.persistence.api.ProductoApi;
import com.cumpleanos.importramite.service.exception.HttpResponseHandler;
import com.cumpleanos.importramite.service.http.ProductosClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ProductosClientServiceImpl {

    private final ProductosClient productosClient;

    public ProductoApi getProduct(long bodega, String data) {
        return HttpResponseHandler.handle(() -> productosClient.buscarProdBod(bodega, data),
                "Error al obtener el producto :" + data);
    }

    public String getMatches(long bodega, String data, String item) {
        return HttpResponseHandler.handle(() -> productosClient.getMatches(bodega, data, item),
                "Error al obtener informacion del producto :" + data);
    }

    public String exitInCompany(Long empresa, String barra, String item) {
        return HttpResponseHandler.handle(() -> productosClient.existIntoCompany(empresa, barra, item),
                "Error al obtener el producto :" + item);
    }

}
