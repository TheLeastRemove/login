package com.example.login;

import java.io.*;
import java.sql.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 注册JDBC驱动
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        System.out.println("username: " + username);
        System.out.println("password: " + password);

        boolean isAuthenticated = authenticateUser(username, password);

        if (isAuthenticated) {
            // 输出
            response.getWriter().println("Hello, " + username + "!");
        } else {
            // 登录失败，转发回登录页面（index.jsp）
            RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
            dispatcher.forward(request, response);
        }
    }

    private boolean authenticateUser(String username, String password) {
        final String JDBC_URL = "jdbc:mysql://localhost:3306/login";
        final String USER = "root";
        final String PASSWORD = "20030521";


        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            String query = "SELECT * FROM users WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    // 验证用户名是否存在
                    if (resultSet.next()) {
                        // 用户名存在，验证密码
                        System.out.println("Username exists");
                        String storedPassword = resultSet.getString("password");
                        return password.equals(storedPassword);
                    } else {
                        System.out.println("Username does not exist");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
