<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<tags:master pageTitle="Cart Details">
  <p>
      Cart: ${cart}
  </p>
  <table>
    <thead>
      <tr>
        <td>Image</td>
        <td>
            Description
        </td>
        <td class="price">
            Price
        </td>
      </tr>
    </thead>
    <c:forEach var="item" items="${cart.items}">
      <tr>
        <td>
          <img class="product-tile" src="${item.product.imageUrl}">
        </td>
        <td>
        <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">
            ${item.product.description}
        </a>
        </td>
        <td class="price">
          <a href="${pageContext.servletContext.contextPath}/productPriceHistory/${item.product.id}">
            <fmt:formatNumber value="${item.product.price}" type="currency" currencySymbol="${item.product.currency.symbol}"/>
          </a>
        </td>
      </tr>
    </c:forEach>
  </table>
</tags:master>