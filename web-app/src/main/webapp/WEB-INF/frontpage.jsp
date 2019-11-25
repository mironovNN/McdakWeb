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
<body style="background-color: whitesmoke">
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
    <h2 style="float: left; max-width: 70%" >Menu</h2>
  <div class="row" style = "max-width: 70%; float: left">
  <% for (ProductModel item : (List<ProductModel>) request.getAttribute(Constants.ITEMS)) { %>
    <div class="col-3" style = "max-width: 30%; margin-left: 3%">
      <div class="card mt-3" style = >
        <img src=<%= item.getImageUrl()%> class="card-img-top" alt="...">
        <div class="card-body">
          <h5 class="card-title"><%= item.getName() %></h5>
          <ul class="list-group list-group-flush">
            <li class="list-group-item">Price: <%= item.getPrice() %></li>
          </ul>
          <form action="<%= request.getContextPath() %>" method="post">
            <input name="id" type="hidden" value="<%= item.getId() %>">
            <div class="form group" style = "max-width: max-content">
              <label for="quantity">Quantity</label>
              <input type="number" min="0" id="quantity" name="quantity" value="1">
            </div>
            <button class="btn btn-primary">Add to order</button>
          </form>
        </div>
      </div>
    </div>
  <% } %>
  </div>
</div>
  <h2 style="float: left; max-width: 10%">Order</h2>

  <% List<OrderPositionModel> positions = (List<OrderPositionModel>) request.getAttribute("ordered-items"); %>
  <div class = "row" style = "float: left; margin-left: 5%">
    <div class="col-3" style = "max-width: 100%; margin-left: 15%">
      <div class="card mt-3">
        <div class="card-body" style = "max-width: 100%">
          <% for (OrderPositionModel model: positions) { %>
          <h5 class="card-title"><%= model.getProductName() %>..........<%= model.getProductQuantity() %></h5>

          <ul class="list-group list-group-flush">
            <li class="list-group-item">Price: <%= model.getProductQuantity() * model.getProductPrice() %><a href = "<%= request.getContextPath() %>/del?id= <%= model.getId()%>" class="btn btn-del">Del</a></li>

          </ul>
        <%--  <form action="<%= request.getContextPath() %>" method="post">
            <input name="id" type="hidden" value="<%= item.getId() %>">
            <div class="form group">
              <label for="quantity">Quantity</label>
              <input type="number" min="0" id="quantity" name="quantity" value="1">
            </div> --%>
          <% } %>
          <h5 class="card-title">Total price: <%= request.getAttribute("totalPrice") %> </h5>
          <button class="btn btn-primary"> Buy </button>
          </form>
        </div>
      </div>
    </div>
  </div>
</body>
</html>
