package ru.rosbank.javaschool.web.servlet;

import ru.rosbank.javaschool.util.SQLTemplate;
import ru.rosbank.javaschool.web.constant.Constants;
import ru.rosbank.javaschool.web.model.ProductModel;
import ru.rosbank.javaschool.web.repository.*;
import ru.rosbank.javaschool.web.service.BurgerAdminService;
import ru.rosbank.javaschool.web.service.BurgerUserService;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;

// 1. Instantiation
// 2. init
// 3. service
// 4. destroy (уничтожение)

// Singleton'ы - Servlet*
public class FrontServlet extends HttpServlet {
  private BurgerUserService burgerUserService;
  private BurgerAdminService burgerAdminService;

  // Ctrl + O
  @Override
  public void init() throws ServletException {
    log("Init");
    // Lookup - самостоятельно ищем зависимости
    // JNDI
    try {
      // TODO: неплохо бы: чтобы это было автоматически
      InitialContext initialContext = new InitialContext();
      DataSource dataSource = (DataSource) initialContext.lookup("java:/comp/env/jdbc/db");
      SQLTemplate sqlTemplate = new SQLTemplate();
      ProductRepository productRepository = new ProductRepositoryJdbcImpl(dataSource, sqlTemplate);
      OrderRepository orderRepository = new OrderRepositoryJdbcImpl(dataSource, sqlTemplate);
      OrderPositionRepository orderPositionRepository = new OrderPositionRepositoryJdbcImpl(dataSource, sqlTemplate);
      burgerUserService = new BurgerUserService(productRepository, orderRepository, orderPositionRepository);
      burgerAdminService = new BurgerAdminService(productRepository, orderRepository, orderPositionRepository);

      insertInitialData(productRepository);
    } catch (NamingException e) {
      e.printStackTrace();
    }
  }

  private void insertInitialData(ProductRepository productRepository) {
    productRepository.save(new ProductModel(0, "Burger 1", 100, 1, null));
    productRepository.save(new ProductModel(0, "Burger 2", 200, 2, null));
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // будем перенаправлять запрос
    // resp.getWriter().write("...");
    String rootUrl = req.getContextPath().isEmpty() ? "/" : req.getContextPath();
    String url = req.getRequestURI().substring(req.getContextPath().length());
    // routing
    // в зависимости от url'а вызывать нужные обработчики
    if (url.startsWith("/admin")) {
      if (url.equals("/admin")) {
        // TODO: work with admin panel
        if (req.getMethod().equals("GET")) {
          req.setAttribute(Constants.ITEMS, burgerAdminService.getAll());
          req.getRequestDispatcher("/WEB-INF/admin/frontpage.jsp").forward(req, resp);
          return;
        }

        if (req.getMethod().equals("POST")) {
          // getParameter - POST (BODY FORM)
          int id = Integer.parseInt(req.getParameter("id"));
          String name = req.getParameter("name");
          int price = Integer.parseInt(req.getParameter("price"));
          int quantity = Integer.parseInt(req.getParameter("quantity"));
          // TODO: validation
          burgerAdminService.save(new ProductModel(id, name, price, quantity, null));
          resp.sendRedirect(url);
          return;
        }
      }

      if (url.startsWith("/admin/edit")) {
        if (req.getMethod().equals("GET")) {
          // ?id=value
          int id = Integer.parseInt(req.getParameter("id"));
          req.setAttribute(Constants.ITEM, burgerAdminService.getById(id));
          req.setAttribute(Constants.ITEMS, burgerAdminService.getAll());
          req.getRequestDispatcher("/WEB-INF/admin/frontpage.jsp").forward(req, resp);
          return;
        }
      }

      return;
    }

    if (url.equals("/")) {

      if (req.getMethod().equals("GET")) {
        HttpSession session = req.getSession();
        if (session.isNew()) {
          int orderId = burgerUserService.createOrder();
          session.setAttribute("order-id", orderId);
        }

        int orderId = (Integer) session.getAttribute("order-id");
        req.setAttribute("ordered-items", burgerUserService.getAllOrderPosition(orderId));
        req.setAttribute(Constants.ITEMS, burgerUserService.getAll());
        req.getRequestDispatcher("/WEB-INF/frontpage.jsp").forward(req, resp);
        return;
      }
      if (req.getMethod().equals("POST")) {
        HttpSession session = req.getSession();
        if (session.isNew()) {
          int orderId = burgerUserService.createOrder();
          session.setAttribute("order-id", orderId);
        }

        int orderId = (Integer) session.getAttribute("order-id");
        int id = Integer.parseInt(req.getParameter("id"));
        int quantity = Integer.parseInt(req.getParameter("quantity"));

        burgerUserService.order(orderId, id, quantity);
        resp.sendRedirect(url);
        return;
      }
    }
  }

  @Override
  public void destroy() {
    log("destroy");
  }
}
