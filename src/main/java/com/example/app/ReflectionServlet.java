package com.example.app;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

@WebServlet("/reflect")
public class ReflectionServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String methodName = request.getParameter("methodName");
        String paramValue = request.getParameter("paramValue");
        String result = "";

        try {
            methodName = methodName.replaceAll("\\([^)]*\\)", "");
            Class<?> clazz = Class.forName("com.example.app.TestMethods");
            Object obj = clazz.newInstance();

            if (paramValue != null && !paramValue.isEmpty()) {
                if (methodName.equals("add")) {
                    // 处理add方法的数字参数
                    String[] numbers = paramValue.split(",");
                    if (numbers.length == 2) {
                        Method method = clazz.getMethod(methodName, int.class, int.class);
                        int num1 = Integer.parseInt(numbers[0].trim());
                        int num2 = Integer.parseInt(numbers[1].trim());
                        result = String.valueOf(method.invoke(obj, num1, num2));
                    } else {
                        result = "Error: add方法需要两个整数参数";
                    }
                } else {
                    // 处理其他带String参数的方法
                    Method method = clazz.getMethod(methodName, String.class);
                    result = String.valueOf(method.invoke(obj, paramValue));
                }
            } else {
                // 无参数方法调用
                Method method = clazz.getMethod(methodName);
                result = String.valueOf(method.invoke(obj));
            }
        } catch (NumberFormatException e) {
            result = "Error: 请输入有效的整数";
        } catch (Exception e) {
            result = "Error: " + e.getMessage();
        }

        request.setAttribute("result", result);
        request.getRequestDispatcher("/reflect.jsp").forward(request, response);
    }
}