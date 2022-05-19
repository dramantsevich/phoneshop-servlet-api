<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product Price History">
  <p>
    ${product.description}
  </p>
  <tr>
    <td>
        <img src="${product.imageUrl}">
    </td>
  </tr>
  <table>
      <tr>
        <td>Date</td>
            <td>
                <c:forEach items="${product.priceHistory}" var="priceHistory">
                    ${priceHistory.startDate}
                </c:forEach>
            </td>
      </tr>
      <tr>
        <td>Price</td>
            <td>
                <c:forEach items="${product.priceHistory}" var="priceHistory">
                    <fmt:formatNumber value="${priceHistory.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
                </c:forEach>
            </td>
      </tr>
  </table>
</tags:master>