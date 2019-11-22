package ru.rosbank.javaschool.web.repository;

import ru.rosbank.javaschool.web.model.OrderPositionModel;

import java.util.List;
import java.util.Optional;

public interface OrderPositionRepository {
    List<OrderPositionModel> getAll();

    Optional<OrderPositionModel> getById(int id);

    void save(OrderPositionModel model);

    void removeById(int id);

  List<OrderPositionModel> getAllByOrderId(int orderId);
}
