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

public class FrontServlet extends HttpServlet {
  private BurgerUserService burgerUserService;
  private BurgerAdminService burgerAdminService;

  @Override
  public void init() throws ServletException {
    log("Init");

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

    productRepository.save(new ProductModel(0, "BigMak", 200, 1, "https://commerage.ru/img/xo/740x480/5/hp33oegsGEOcokWZw6LL-YDDY2iP8sr9.jpg"));
    productRepository.save(new ProductModel(0, "ChessBurger", 120, 1, "https://commerage.ru/img/xo/740x480/5/hp33oegsGEOcokWZw6LL-YDDY2iP8sr9.jpg"));
    productRepository.save(new ProductModel(0, "ChickBurger", 170, 1, "https://commerage.ru/img/xo/740x480/5/hp33oegsGEOcokWZw6LL-YDDY2iP8sr9.jpg"));
    productRepository.save(new ProductModel(0, "French Fry", 100, 1, "https://n1s2.hsmedia.ru/48/83/b2/4883b26ba5458ae653f1955dbe5a4f8e/620x412_1_e6e85d9d54d3c4b1544fab803374958b@800x531_0x59f91261_9474578641381418343.jpeg"));
    productRepository.save(new ProductModel(0, "Coca-cola", 100, 1, "https://pimg.mycdn.me/getImage?disableStub=true&type=VIDEO_S_720&url=http%3A%2F%2Fi.ytimg.com%2Fvi%2Fr7eYj0fSG3M%2F0.jpg&signatureToken=ChD_uOM6Hd4yxLJa3NElQw"));
    productRepository.save(new ProductModel(0, "Fanta", 100, 1, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR-yvwkIlwVHaORAJdblM-2Ai3ZKC4VeDrF5jK1MLtQm6LU7QuZ&s"));

  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    String rootUrl = req.getContextPath().isEmpty() ? "/" : req.getContextPath();
    String url = req.getRequestURI().substring(req.getContextPath().length());

    if (url.startsWith("/admin")) {
      if (url.equals("/admin")) {
        // TODO: work with admin panel
        if (req.getMethod().equals("GET")) {
          req.setAttribute(Constants.ITEMS, burgerAdminService.getAll());
          req.getRequestDispatcher("/WEB-INF/admin/frontpage.jsp").forward(req, resp);
          return;
        }

        if (req.getMethod().equals("POST")) {

          int id = Integer.parseInt(req.getParameter("id"));
          String name = req.getParameter("name");
          int price = Integer.parseInt(req.getParameter("price"));
          int quantity = Integer.parseInt(req.getParameter("quantity"));
          String imageUrl = req.getParameter("imageUrl");

          burgerAdminService.save(new ProductModel(id, name, price, quantity, null));
          resp.sendRedirect(url);
          return;
        }
      }

      if (url.startsWith("/admin/edit")) {
        if (req.getMethod().equals("GET")) {

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
        req.setAttribute("totalPrice", burgerUserService.totalPrice());
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

    if(url.startsWith("/del")){
      delService(req, resp, url);
    }
  }

  @Override
  public void destroy() {
    log("destroy");
  }

  protected void delService(HttpServletRequest req, HttpServletResponse resp, String url) throws ServletException, IOException {
    if (req.getMethod().equals("GET")){
      int id = Integer.parseInt(req.getParameter("id"));
      burgerUserService.getById(id);
      resp.sendRedirect("/");
      return;

    }
  }
}
