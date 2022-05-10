<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
  <p>
    Welcome to Expert-Soft training!
  </p>
  <c:if test="${not empty param.message}">
      <div class="success">
          ${param.message}
      </div>
    </c:if>
    <c:if test="${not empty errors}">
      <div class="error">
          There were errors updating cart
      </div>
    </c:if>
  <form>
    <input name="query" value=${param.query}>
    <button>Search</button>
  </form>
  <form method="get" action="${pageContext.servletContext.contextPath}/products/addProduct/${product.id}">
      <table>
        <thead>
          <tr>
            <td>Image</td>
            <td>
                Description
                <tags:sortLink sort="description" order="asc"/>
                <tags:sortLink sort="description" order="desc"/>
            </td>
            <td>
                Quantity
            </td>
            <td class="price">
                Price
                <tags:sortLink sort="price" order="asc"/>
                <tags:sortLink sort="price" order="desc"/>
            </td>
            <td></td>
          </tr>
        </thead>
        <c:forEach var="product" items="${products}">
          <tr>
            <td>
              <img class="product-tile" src="${product.imageUrl}">
            </td>
            <td>
                <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                    ${product.description}
                </a>
            </td>
            <td>
                <c:set var="error" value="${errors[product.id]}"/>
                <input class="quantity" name="quantity">
                <c:if test="${not empty error}">
                                    <div class="error">
                                        ${errors[product.id]}
                                    </div>
                                </c:if>
                <input type="hidden" name="productId" value="${product.id}"/>
            </td>
            <td class="price">
              <a href="${pageContext.servletContext.contextPath}/productPriceHistory/${product.id}">
                <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
              </a>
            </td>
            <td>
                <button>
                    Add to cart
                </button>
            </td>
          </tr>
        </c:forEach>
      </table>
  </form>
</tags:master>