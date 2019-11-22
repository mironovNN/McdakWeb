package ru.rosbank.javaschool.web.repository;


import ru.rosbank.javaschool.util.RowMapper;
import ru.rosbank.javaschool.util.SQLTemplate;
import ru.rosbank.javaschool.web.exception.DataAccessException;
import ru.rosbank.javaschool.web.model.OrderPositionModel;
import ru.rosbank.javaschool.web.model.ProductModel;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class OrderPositionRepositoryJdbcImpl implements OrderPositionRepository {
    private final DataSource ds;
    private final SQLTemplate template;
    private final RowMapper<OrderPositionModel> mapper = rs -> new OrderPositionModel(
            rs.getInt("id"),
            rs.getInt("order_id"),
            rs.getInt("product_id"),
            rs.getString("product_name"),
            rs.getInt("product_price"),
            rs.getInt("product_quantity")
    );

    public OrderPositionRepositoryJdbcImpl(DataSource ds, SQLTemplate template) {
        this.ds = ds;
        this.template = template;

        try {
            template.update(ds, "CREATE TABLE IF NOT EXISTS orders_positions (\n" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "order_id INTEGER NOT NULL REFERENCES orders,\n" +
                    "product_id INTEGER NOT NULL REFERENCES products,\n" +
                    "product_name TEXT NOT NULL,\n" +
                    "product_price INTEGER NOT NULL CHECK (product_price >= 0),\n" +
                    "product_quantity INTEGER NOT NULL DEFAULT 0 CHECK (product_quantity >= 0)" +
                    ");");
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public List<OrderPositionModel> getAll() {
        try {
            return template.queryForList(ds, "SELECT id, order_id, product_id, product_name, product_price, product_quantity FROM orders_positions;", mapper);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public Optional<OrderPositionModel> getById(int id) {
        try {
            return template.queryForObject(ds, "SELECT id, order_id, product_id, product_name, product_price, product_quantity FROM orders_positions WHERE id = ?;", stmt -> {
                stmt.setInt(1, id);
                return stmt;
            }, mapper);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void save(OrderPositionModel model) {
        try {
            if (model.getId() == 0) {
                int id = template.<Integer>updateForId(ds, "INSERT INTO orders_positions(order_id, product_id, product_name, product_price, product_quantity) VALUES (?, ?, ?, ?, ?);", stmt -> {
                    int nextIndex = 1;
                    stmt.setInt(nextIndex++, model.getOrderId());
                    stmt.setInt(nextIndex++, model.getProductId());
                    stmt.setString(nextIndex++, model.getProductName());
                    stmt.setInt(nextIndex++, model.getProductPrice());
                    stmt.setInt(nextIndex++, model.getProductQuantity());
                    return stmt;
                });
                model.setId(id);
            } else {
                template.update(ds, "UPDATE orders_positions SET order_id = ? product_id = ?, product_name = ?, product_price = ?, product_quantity = ? WHERE id = ?;", stmt -> {
                    int nextIndex = 1;
                    stmt.setInt(nextIndex++, model.getOrderId());
                    stmt.setInt(nextIndex++, model.getProductId());
                    stmt.setString(nextIndex++, model.getProductName());
                    stmt.setInt(nextIndex++, model.getProductPrice());
                    stmt.setInt(nextIndex++, model.getProductQuantity());
                    return stmt;
                });
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void removeById(int id) {
        try {
            template.update(ds, "DELETE FROM orders_positions WHERE id = ?;", stmt -> {
                stmt.setInt(1, id);
                return stmt;
            });
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public List<OrderPositionModel> getAllByOrderId(int orderId) {
        try {
            return template.queryForList(ds, "SELECT id, order_id, product_id, product_name, product_price, product_quantity FROM orders_positions WHERE order_id = ?;", mapper, stmt -> {
                stmt.setInt(1, orderId);
                return stmt;
            });
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}
