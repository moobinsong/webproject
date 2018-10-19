package model;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.BoardDAO;
import vo.BoardVO;
import vo.PageVO;
import vo.SearchVO;

public class BoardSearchAction implements Action {
	private String path;
	
	public BoardSearchAction(String path) {
		this.path=path;
	}

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		//qna_board_list.jsp의 search form에서 넘긴 값 가져오기
		String criteria = req.getParameter("criteria");
		String keyword = req.getParameter("keyword");
		
		SearchVO vo=new SearchVO();
		vo.setCriteria(criteria);
		vo.setKeyword(keyword);
		
		
		BoardDAO dao=new BoardDAO();	
		
		/***** 페이지 나누기 ******/
		int page=1;    //만일 사용자가 선택한 페이지가 없다면 페이지값 기본 세팅
		if(req.getParameter("page")!=null) //페이지값 넘어온 경우
			page=(Integer.parseInt(req.getParameter("page")));
		
		int limit=10;  //한 페이지당 출력될 게시글 목록 수 지정
		
		/* qna_board_list.jsp 하단의 페이지 나누기를 위한 정보 구하기 */
		//전체 목록 갯수
		int totalRows=dao.getSearchRows(criteria,keyword);
		//총 페이지 수
		int totalPage=(int)((double)totalRows/limit+0.95);
		//현재 화면의 시작 페이지 수
		// 1 2 3 4...10 [다음]
		int startPage=(((int)((double)page/10+0.9))-1)*10+1;
		//현재 화면의 마지막 페이지 수
		int endPage=startPage+10-1;
		
		//보여줄 목록이 작은 경우 현재 화면에 보여지는 나열된 페이지 번호가
		//totalPage를 넘지 않도록 하기
		if(endPage>totalPage)
			endPage=totalPage;
		/* qna_board_list.jsp 하단의 페이지 나누기를 위한 정보 구하기 */
		PageVO info=new PageVO();
		info.setEndPage(endPage);
		info.setTotalPage(totalPage);
		info.setPage(page);  //사용자가 선택한 페이지 값
		info.setStartPage(startPage);		
		
		ArrayList<BoardVO> list=dao.getSearchList(page,limit,criteria,keyword);
		
		if(list.isEmpty()) {
			path="index.html";
		}else {
			req.setAttribute("list", list);
			req.setAttribute("info", info);
			req.setAttribute("search", vo);			
		}		
		return new ActionForward(path,false);
	}
}







