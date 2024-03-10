<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <title>Order</title>
</head>

<body>


  <form method="post" th:object="${orderForm}">
    <div th:if="${orderForm.isPreparing()}">Заказ готовтится</div>
    <div th:if="${orderForm.isReady()}">Заказ готов</div>
    <div th:if="${orderForm.isPaid()}">Заказ оплачен</div>

    <br>


    <table>
    <tr>
            <td>Блюдо</td>
            <td>Время приготовления</td>
            <td>Цена</td>
                <td>Количество</td>

            </tr>
      <tr th:each="dishOrder, pos : ${orderForm.dishOrders}"
        th:if="${orderForm.isNotReady() || dishOrder.getCount() != 0}">
        <td th:text="${dishOrder.getDish().getName()}">
        <td th:text="${dishOrder.getDish().getCookingTimeMinutes()}">

        <td th:text="${dishOrder.getDish().getPrice()}">
        <td>

          <input type="number" th:if="${orderForm.isNotReady()}" th:field="*{dishOrders[__${pos.index}__].count}"
            th:min="${dishOrder.getCount()}" th:value="${dishOrder.getCount()}">
          <div th:unless="${orderForm.isNotReady()}" th:text="${dishOrder.getCount()}"> </div>


        </td>
      </tr>
      <tr>
        <td> Итого: </td>
        <td th:text="${orderForm.sum()}">
      </tr>
      <br>
    </table>

    <br>
    <input th:if="${orderForm.isNotReady()}" type="submit">
  </form>

  <form th:if="${orderForm.isReady()}" method="get" action="create_order/pay">
    <input type="hidden" name="id" th:value="${orderForm.orderId}" />
    <input type="submit" value="Оплатить">
  </form>



</body>

</html>