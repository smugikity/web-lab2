import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.util.*;
import java.io.*;

public class AreaCheckServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        resp.setContentType("text/html;charset=UTF-8");
        List<String> tableRows = (List) session.getAttribute("tableRows");
        if (tableRows == null) {
            tableRows = new ArrayList<String>();
            session.setAttribute("tableRows", tableRows);
//          tableRows is a json list saving results '{"x":,"y":,"r":,"result":,"time":,"executed_time":}'
        }
        double x = Double.parseDouble(req.getParameter("x"));
        double y = Double.parseDouble(req.getParameter("y"));
        double r = Double.parseDouble(req.getParameter("r"));
        byte isLimit = Byte.parseByte(req.getParameter("islimit"));
        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        if (checkData(x, y, r, isLimit)) {
            String result = new Result(x, y, r).toString();
            tableRows.add(result);
            writer.println(result);
        } else writer.println("{\"error\": \"bad request\"}");
    }

    private boolean checkData(double x, double y, double r, byte isLimit) {
        Double[] xSample = {-3.0,-2.0,-1.0,0.0,1.0,2.0,3.0,4.0,5.0};
        Double[] rSample = {1.0,1.5,2.0,2.5,3.0};
        if (isLimit==1)
            return (Arrays.asList(xSample).contains(x) && (y > -5 && y < 3) && Arrays.asList(rSample).contains(r));
        else if (isLimit==0) return (Arrays.asList(rSample).contains(r));
        else return false;
    }
}