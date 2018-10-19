package model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.BoardDAO;
import vo.BoardVO;
import vo.SearchVO;

public class BoardViewAction implements Action {
	private String path;	

	public BoardViewAction(String path) {
		super();
		this.path = path;
	}
	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		//bno 가져오기
		int bno=Integer.parseInt(req.getParameter("bno"));
		
		//페이지 나누기 후 사용자가 현재 보던 페이지 가져오기
		String page=req.getParameter("page");
		
		/********검색어 처리 부분때문에 추가**********/
		
		SearchVO search=null;
		if(req.getParameter("criteria")!=null) {
			search=new SearchVO();
			search.setCriteria(req.getParameter("criteria")); 
			search.setKeyword(req.getParameter("keyword"));
		}
		/*********************/
			
		
		//db getRow()호출
		BoardDAO dao=new BoardDAO();		
		BoardVO vo=dao.getRow(bno);
		
		//값 담은 후 이동(qna_board_view.jsp)
		if(vo!=null) {
			req.setAttribute("vo", vo);
			req.setAttribute("page", page);
			req.setAttribute("search", search);
		}
		return new ActionForward(path,false);
	}

}



