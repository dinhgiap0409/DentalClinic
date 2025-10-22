<%-- 
    Document   : newjsp
    Created on : 22 thg 10, 2025, 11:31:12
    Author     : vuhieu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <form action="login" method="POST">
            username: <input type="text" name="username" value="" /></br>
            password <input type="text" name="password" value="" /></br>
            <input type="submit" value="login" />
        </form>
    </body>
</html>
