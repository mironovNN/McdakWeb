package ru.rosbank.javaschool.web.repository;

import ru.rosbank.javaschool.web.model.OrderModel;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    List<OrderModel> getAll();

    Optional<OrderModel> getById(int id);

    void save(OrderModel model);

    void removeById(int id);
}
