package ir.maktab.project_final_faz2.captcha;

import com.github.cage.GCage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

//@WebServlet("/captcha-servlet")
public class CaptchaServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GCage gCage = new GCage();
        response.setContentType("image/" + gCage.getFormat());
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setHeader("Progma", "no-cache");
        response.setDateHeader("Max-Age", 0);
        String token = gCage.getTokenGenerator().toString();
        HttpSession session = request.getSession();
        session.setAttribute("captcha", token);
        gCage.draw(token, response.getOutputStream());
    }
}
