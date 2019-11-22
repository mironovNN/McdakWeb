<%@ page import="java.util.List" %>
<%@ page import="ru.rosbank.javaschool.web.constant.Constants" %>
<%@ page import="ru.rosbank.javaschool.web.model.ProductModel" %>
<%@ page import="ru.rosbank.javaschool.web.model.OrderPositionModel" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- ! + Tab - emmet --%>
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>Burger Shop</title>
  <%@include file="bootstrap-css.jsp"%>
</head>
<body>
<%-- Jasper --%>
<%-- tag + Tab --%>
<%-- tag{Content} + Tab --%>
<%-- tag{Content} + Tab --%>
<%-- tag#id - уникальный идентификатор на странице --%>
<%-- tag.class - строка, позволяющая логически группировать элементов --%>
<%-- tag[attr=value] - все остальные атрибуты --%>
<%-- null -> for non-existent attribute --%>
<div class="container">
  <%-- ul>li + Tab --%>
  <h1>Burger Shop</h1>

    <% List<OrderPositionModel> positions = (List<OrderPositionModel>) request.getAttribute("ordered-items"); %>
    <p><%= positions.size() %></p>
    <% for (OrderPositionModel model: positions) { %>
    <p><%= model %></p>
    <% } %>

  <div class="row">
  <% for (ProductModel item : (List<ProductModel>) request.getAttribute(Constants.ITEMS)) { %>
    <div class="col-3">
      <div class="card mt-3">
        <img src="..." class="card-img-top" alt="...">
        <div class="card-body">
          <h5 class="card-title"><%= item.getName() %>
          </h5>
          <ul class="list-group list-group-flush">
            <li class="list-group-item">Price: <%= item.getPrice() %></li>
          </ul>
          <form action="<%= request.getContextPath() %>" method="post">
            <input name="id" type="hidden" value="<%= item.getId() %>">
            <div class="form group">
              <label for="quantity">Product Quantity</label>
              <input type="number" min="0" id="quantity" name="quantity" value="1">
            </div>
            <button class="btn btn-primary">Add to card</button>
          </form>
        </div>
      </div>
    </div>
  <% } %>
  </div>
</div>

</body>
</html>
