package control;

import service.RelationService;

import javax.jws.WebService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

@WebServlet("/home")
public class ShowUser extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("do");
        if(action.equals("login")){
            String id = "";
            id = request.getParameter("id");
            if(!id.equals("")){
                RelationService service = new RelationService();
                TreeMap<String, String> infoMap = service.getUserBaseInfo(id);
                TreeMap<String, String> followMap = service.getFollow(id);
                TreeMap<String, String> fansMap = service.getFans(id);
                TreeMap<String, String> strangerMap = service.getStranger(id);
                request.setAttribute("infoMap", infoMap);
                request.setAttribute("followMap", followMap);
                request.setAttribute("fansMap", fansMap);
                request.setAttribute("noMap", strangerMap);
                request.getRequestDispatcher("home.jsp").forward(request,response);
            }else {
                request.setAttribute("msg","ID不能为空");
                request.getRequestDispatcher("index.jsp").forward(request,response);
            }
        }

    }
}
