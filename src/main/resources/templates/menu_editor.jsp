<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>

<body>
    <a th:href="logout">Выйти</a>

    <table>
        <tr>
        <td>Блюдо</td>
        <td>Время приготовления</td>
        <td>Цена</td>

        </tr>

        <tr th:each="dish : ${dishes}">
            <td th:text="${dish.name}">
            <td th:text="${dish.cookingTimeMinutes}">
            <td th:text="${dish.price}">
            <td>
                <a th:href="'/menu_editor/delete?id=' + ${dish.id}" data-method="delete">Удалить блюдо</a>

            </td>
        </tr>
        <br>

    </table>
    <br>
    <a href="/create_dish">Добавить блюдо</a>

</body>

</html>