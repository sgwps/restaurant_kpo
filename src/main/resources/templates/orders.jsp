<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>

<body>
    <a th:href="logout">Выйти</a>

    <table>

        <tr th:each="order : ${orders}">

            <td>
                <a th:href="'/create_order?id=' + ${order.id}">Заказ </a>

            </td>
        </tr>
        <br>

    </table>
    <a th:href="create_order">Новый заказ </a>

</body>

</html>