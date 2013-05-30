package mono;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class GameMonopolyServlet extends HttpServlet {
	
	public GameMonopolyServlet(){
		super();
	}
	
	public void destroy(){
		super.destroy();
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		String action = req.getParameter("action");
		
		PassInfo passinfo = new PassInfo();
		
		if (action.equals("register")){
			String username = req.getParameter("username");
			passinfo.Init(action, username, "", "", "");
			
		}else if (action.equals("waiting")){
			passinfo.Init(action, "", "", "", "");
			
		}else if (action.equals("query_place")){
			String place_id = req.getParameter("place_id");
			passinfo.Init(action, place_id, "", "", "");
			
		}else if (action.equals("no_action")){
			String player_id = req.getParameter("player_id");
			String current_pos = req.getParameter("current_pos");
			passinfo.Init(action, player_id, current_pos, "", "");
			
		}else if (action.equals("purchase")){
			String player_id = req.getParameter("player_id");
			String place_id = req.getParameter("place_id");
			String athlete_id = req.getParameter("athlete_id");
			
			passinfo.Init(action, player_id, place_id, athlete_id, "");
			
		}else if (action.equals("upgrade")){
			String player_id = req.getParameter("player_id");
			String place_id = req.getParameter("place_id");
			String athlete_id = req.getParameter("athlete_id");
			passinfo.Init(action, player_id, place_id, athlete_id, "");			
			
		}else if (action.equals("pay")){
			String pay_from = req.getParameter("pay_from");
			String pay_to = req.getParameter("pay_to");
			String actual_payment = req.getParameter("actual_payment");
			String place_id = req.getParameter("place_id");
			passinfo.Init(action, pay_from, pay_to, actual_payment, place_id);
			
		}else if (action.equals("timely_update")){
			String player_id = req.getParameter("player_id");
			passinfo.Init(action, player_id, "", "", "");
			
		}else if (action.equals("initialize")) {
			passinfo.Init(action, "", "", "", "");
		}else if(action.equals("quit")){
			passinfo.Init(action, "", "", "", "");
		}else if (action.equals("money_change")) {
			String player_id = req.getParameter("player_id");
			String current_money = req.getParameter("current_money");
			String place_id = req.getParameter("place_id");
			passinfo.Init(action, player_id, current_money, place_id, "");
		}
		else{
			// Unknown action type
			passinfo.Init(action, "", "", "", "");
		}
		
		String result = Monopoly_Server.ProcessAjaxReq(passinfo);
		
		//System.out.println("Out of ProcessAjaxReq");
		
		res.setContentType("text/xml");
		res.setHeader("Cache-Control", "no-cache");
		PrintWriter out=res.getWriter();
		out.write(result);
		out.close();
		
	}
	
	
	
	
	
	
	
	
}





