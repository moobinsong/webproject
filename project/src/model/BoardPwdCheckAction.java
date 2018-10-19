package model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.BoardDAO;

public class BoardPwdCheckAction implements Action {
	private String path;	
	
	public BoardPwdCheckAction(String path) {
		super();
		this.path = path;
	}

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		//qna_board_pwdCheck.jsp에서 넘긴 값 가져오기
		int bno=Integer.parseInt(req.getParameter("bno"));
		String password=req.getParameter("password");
		
		//페이지 나누기 후 사용자가 현재 보던 페이지 가져오기
		String page=req.getParameter("page");
		
		
		
		BoardDAO dao=new BoardDAO();
		//bno와 password가 일치하면 게시글 삭제하는 액션으로 넘기기
		boolean flag=dao.pwdCheck(bno, password);
		if(flag) {//qDelete.do?bno=1
			path+="?bno="+bno+"&page="+page;
		}else {
			path="view/pwdCheckError.jsp";
		}
		return new ActionForward(path, true);
	}
}



