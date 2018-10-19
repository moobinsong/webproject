package model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.BoardDAO;

public class BoardDeleteAction implements Action {
	private String path;	
	
	public BoardDeleteAction(String path) {
		super();
		this.path = path;
	}
	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// PwdCheck.do에서 넘어오는 값 가져오기
		int bno=Integer.parseInt(req.getParameter("bno"));
		//페이지 나누기 후 사용자가 현재 보던 페이지 가져오기
		String page=req.getParameter("page");
		
		// 삭제한 후 리스트로 경로 이동
		BoardDAO dao=new BoardDAO();
		int result=dao.deleteRow(bno);
		
		if(result>0) {
			path+="?page="+page;
		}
		
		return new ActionForward(path, true);
	}
}
