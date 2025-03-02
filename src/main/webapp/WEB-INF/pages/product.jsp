<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product Details">
<c:if test="${not empty param.message}">
    <div class="success">
        ${param.message}
    </div>
</c:if>
<c:if test="${not empty error}">
    <div class="error">
        There was an error adding to cart
    </div>
</c:if>
<p>
    ${product.description}
</p>
  <form method="post" action="${pageContext.servletContext.contextPath}/products/${product.id}">
      <table>
          <tr>
            <td>Image</td>
                <td>
                    <img src="${product.imageUrl}">
                </td>
          </tr>
          <tr>
            <td>Code</td>
                <td>
                    ${product.code}
                </td>
          </tr>
          <tr>
            <td>Stock</td>
                <td>
                    ${product.stock}
                </td>
          </tr>
          <tr>
            <td>Price</td>
                <td class="price">
                    <a href="${pageContext.servletContext.contextPath}/productPriceHistory/${product.id}">
                        <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
                    </a>
                </td>
            </td>
          </tr>
          <tr>
            <td>Quantity</td>
                <td>
                    <input class="quantity" name="quantity" value="${not empty error ? param.quantity : 1}">
                    <c:if test="${not empty error}">
                        <div class="error">
                            ${error}
                        </div>
                    </c:if>
                </td>
          </tr>
      </table>
      <p>
        <button>Add to cart</button>
      </p>
  </form>
  <p>Recently viewed</p>
    <table>
        <c:forEach var="viewHistory" items="${viewedProducts.items}">
            <tr>
                <td>
                    <img class="viewed" src="${viewHistory.imageUrl}">
                </td>
            </tr>
            <tr>
                <td>
                    <a href="${pageContext.servletContext.contextPath}/products/${viewHistory.id}">
                        ${viewHistory.description}
                    </a>
                </td>
            </tr>
            <tr>
                <td class="price">
                    <fmt:formatNumber value="${viewHistory.price}" type="currency" currencySymbol="${viewHistory.currency.symbol}"/>
                </td>
            </tr>
        </c:forEach>
    </table>
</tags:master>