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

    HttpServletRequest request;
    HttpServletResponse response;


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.request = request;
        this.response = response;

        //判断操作
        String action = this.request.getParameter("do");

        if(action.equals("login")){
            String id;
            id = this.request.getParameter("id");
            if(!id.equals("")){
                getMap(id);
                this.request.getRequestDispatcher("home.jsp").forward(this.request,this.response);
            }else {
                this.request.setAttribute("msg","ID不能为空");
                this.request.getRequestDispatcher("index.jsp").forward(this.request,this.response);
            }
        }else if(action.equals("unfollow")){
            String followId;
            followId = this.request.getParameter("followId");
            String userId = this.request.getParameter("id");
            if(!followId.equals("")){
                RelationService service = new RelationService();
                boolean c = service.dounfollow(userId,followId);
                if(c){
                    this.request.setAttribute("check","已取消关注");
                }else{
                    this.request.setAttribute("check","操作失败");
                }
                getMap(userId);

                this.request.getRequestDispatcher("home.jsp").forward(this.request,this.response);
            }
        }
    }

    private void getMap(String id){
        RelationService service = new RelationService();
        TreeMap<String, String> infoMap = service.getUserBaseInfo(id);
        TreeMap<String, String> followMap = service.getFollow(id);
        TreeMap<String, String> fansMap = service.getFans(id);
        TreeMap<String, String> strangerMap = service.getStranger(id);
        request.setAttribute("infoMap", infoMap);
        request.setAttribute("followMap", followMap);
        request.setAttribute("fansMap", fansMap);
        request.setAttribute("noMap", strangerMap);
    }
}
