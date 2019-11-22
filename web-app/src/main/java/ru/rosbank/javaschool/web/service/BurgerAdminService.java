package ru.rosbank.javaschool.web.service;

import lombok.RequiredArgsConstructor;
import ru.rosbank.javaschool.web.exception.NotFoundException;
import ru.rosbank.javaschool.web.model.ProductModel;
import ru.rosbank.javaschool.web.repository.OrderPositionRepository;
import ru.rosbank.javaschool.web.repository.OrderRepository;
import ru.rosbank.javaschool.web.repository.ProductRepository;

import java.util.List;

@RequiredArgsConstructor
public class BurgerAdminService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderPositionRepository orderPositionRepository;

    public void save(ProductModel model) {
        productRepository.save(model);
    }

    public List<ProductModel> getAll() {
        return productRepository.getAll();
    }

    public ProductModel getById(int id) {
        return productRepository.getById(id).orElseThrow(NotFoundException::new);
    }
}
