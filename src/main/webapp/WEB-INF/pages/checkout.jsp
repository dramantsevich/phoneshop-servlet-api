<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Checkout">
  <p>
      Cart: ${cart}, total quantity: ${cart.totalQuantity}
  </p>
  <c:if test="${not empty param.message}">
    <div class="success">
        ${param.message}
    </div>
  </c:if>
  <c:if test="${not empty errors}">
    <div class="error">
        Error occurred while placing order
    </div>
  </c:if>
  <form method="post" action="${pageContext.servletContext.contextPath}/checkout">
      <table>
        <thead>
          <tr>
            <td>Image</td>
            <td>
                Description
            </td>
            <td class="quantity">
                Quantity
            </td>
            <td class="price">
                Price
            </td>
          </tr>
        </thead>
        <c:forEach var="item" items="${order.items}" varStatus="status">
          <tr>
            <td>
              <img class="product-tile" src="${item.product.imageUrl}">
            </td>
            <td>
                <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">
                    ${item.product.description}
                </a>
            </td>
            <td class="quantity">
                <fmt:formatNumber value="${item.quantity}" var="quantity"/>
                ${item.quantity}
            </td>
            <td class="price">
                <a href="${pageContext.servletContext.contextPath}/productPriceHistory/${item.product.id}">
                    <fmt:formatNumber value="${item.product.price}" type="currency" currencySymbol="${item.product.currency.symbol}"/>
                </a>
            </td>
          </tr>
        </c:forEach>
        <tr>
            <td></td>
            <td></td>
            <td class="quantity">Total Quantity:</td>
            <td class="quantity">${cart.totalQuantity}</td>
        </tr>
        <tr>
            <td></td>
            <td></td>
            <td class="quantity">SubTotal:</td>
            <td class="quantity">${order.subTotal}</td>
        </tr>
        <tr>
            <td></td>
            <td></td>
            <td class="quantity">Delivery cost:</td>
            <td class="quantity">${order.deliveryCost}</td>
        </tr>
        <tr>
            <td></td>
            <td></td>
            <td class="quantity">Total cost:</td>
            <td class="quantity">${order.totalCost}</td>
        </tr>
      </table>
      <h2>Your details</h2>
      <table>
        <tags:orderFormField name="firstName" label="First Name" order="${order}" errors="${errors}"></tags:orderFormField>
        <tags:orderFormField name="lastName" label="Last Name" order="${order}" errors="${errors}"></tags:orderFormField>
        <tags:orderFormField name="phone" label="Phone" order="${order}" errors="${errors}"></tags:orderFormField>
        <tr>
            <td>Delivery date<span style="color:red">*</td>
            <td>
                  <link rel="stylesheet" href="//code.jquery.com/ui/1.13.1/themes/base/jquery-ui.css">
                  <script src="https://code.jquery.com/jquery-3.6.0.js"></script>
                  <script src="https://code.jquery.com/ui/1.13.1/jquery-ui.js"></script>
                  <script>
                    $( function() {
                      $( "#datepicker" ).datepicker();
                    } );
                    </script>
                 <input type="text" name="deliveryDate" id="datepicker">
                 <c:set var="error" value="${errors['deliveryDate']}"/>
                 <c:if test="${not empty error}">
                    <div class="error">
                        ${error}
                    </div>
                 </c:if>
            </td>
        </tr>
        <tags:orderFormField name="deliveryAddress" label="Delivery address" order="${order}" errors="${errors}"></tags:orderFormField>
        <tr>
            <td>Payment method<span style="color:red">*</span></td>
            <td>
                <select name="paymentMethod">
                    <option></option>

                    <c:forEach var="paymentMethod" items="${paymentMethods}">
                        <c:choose>
                            <c:when test="${paymentMethod == order['paymentMethod']}">
                                <option selected>${paymentMethod}</option>
                            </c:when>
                            <c:otherwise>
                                <option>${paymentMethod}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
                <c:set var="error" value="${errors['paymentMethod']}"/>
                <c:if test="${not empty error}">
                    <div class="error">
                        ${error}
                    </div>
                </c:if>
            </td>
        </tr>
      </table>
      <p>
        <button>Place order</button>
      </p>
  </form>
  <form id="deleteCartItem" method="post">
  </form>
</tags:master>