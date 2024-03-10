<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>New Dish</title>
</head>

<body>

    <form method="post" th:object="${dish}">
        <label for="name">Name:</label><br>
        <input type="text" th:field="*{name}" id="name" required>
        </br>

        <label for="cookingTime">Time in minutes:</label><br>
        <input type="number" th:field="*{cookingTimeMinutes}" id="name" th:min=1>
        </br>

        <label for="price">Price:</label><br>
        <input type="number" th:field="*{price}" id="name" th:min=1>
        </br>


        <input type="submit">
    </form>
</body>

</html>